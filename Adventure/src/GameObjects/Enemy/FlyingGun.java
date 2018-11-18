package GameObjects.Enemy;

import java.awt.Graphics;
import java.awt.datatransfer.FlavorTable;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import com.sun.javafx.css.CalculatedValue;
import com.sun.org.apache.xpath.internal.operations.And;

import GameObjects.GameObject;
import GameObjects.Player;
import GameObjects.Bullet.Bullet;
import GameObjects.Bullet.FlyingGunBullet;
import TileMap.Tile;
import TileMap.TileMap;
import Tools.Animation;
import Tools.BufferedImageLoader;
import Tools.Camera;
import game.Game;

public class FlyingGun extends Enemy {

	private String path;
	private BufferedImageLoader loader;
	private LinkedList<Animation> sprites;
	private int currentAnimation;
	private float scale;

	private ArrayList<Bullet> allBullets;
	private int shootingDelay;
	private long lastTimeShooted;

	private final int FLYING = 0;

	public FlyingGun(float x, float y, int startX, int endX, boolean facingRight, TileMap map, Camera camera,
			Player player, int shootingDelay, ArrayList<Bullet> allBullets) {
		super(x, y, startX, endX, facingRight, camera, player);
		this.shootingDelay = shootingDelay;
		this.allBullets = allBullets;
		this.map = map;
		init();
	}

	public void init() {
		path = "/FlyingGun.png";
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		health = 30;
		VelX = 2;
		width = 41;
		height = 20;
		cwidth = width;
		cheight = height;
		scale = 1.5f;
		currentAnimation = FLYING;
		hitPower = 30;
		spriteInit();
		lastTimeShooted = System.currentTimeMillis();
	}

	public void tick() {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();

		checkCollisionWithPlayer();
		checkIfPlayerHit();
		checkIfNeedToShoot();

		if (hitted) {
			flicker();
		}

		if (health <= 0) {
			shouldRemove = true;
		}

		if (!dying) {
			if (facingRight) {
				x += VelX;
				currentAnimation = FLYING;
			} else {
				x -= VelX;
				currentAnimation = FLYING;
			}

			// Move enemy between two points (startX, endX)
			if (x >= endX) {
				x = endX;
				facingRight = false;
			} else if (x <= startX) {
				x = startX;
				facingRight = true;
			}
		}

		sprites.get(currentAnimation).runAnimation();
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();

		if (render) {
			sprites.get(FLYING).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) (width * scale),
					(int) (height * scale), g);
		}
	}

	public void checkIfNeedToShoot() {
		// Check if located within screen
		if (x - xMap > 0 && x - xMap < Game.getWidthOfCanvas() && y - yMap > 0 && y - yMap < Game.getHeightOfCanvas()) {
			if (shootingDelay <= System.currentTimeMillis() - lastTimeShooted) {
				lastTimeShooted = System.currentTimeMillis();
				// Calculate coordinates to which FlyingGun need to shoot
				float playerX = player.getX() + (player.getWidth() / 2);
				float playerY = player.getY() + (player.getHeight() / 2);

				// Calculate coordinates from which Flying Gun need to shoot
				float gunX = x + (width / 2);
				float gunY = y + height;

				float deltaX = playerX - gunX;
				float deltaY = playerY - gunY;

				// Calculate the distance
				double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

				// Calculate time
				double time = distance / FlyingGunBullet.getSpeed();

				// Calculate velX and velY
				float bulletVelX = (float) (deltaX / time);
				float bulletVelY = (float) (deltaY / time);

				System.out.println(FlyingGunBullet.getSpeed());
				allBullets.add(new FlyingGunBullet(gunX, gunY, false, map, camera, loader, bulletVelX, bulletVelY));
			}
		}
	}

	public void spriteInit() {
		sprites = new LinkedList<>();

		// Animation
		BufferedImage[] tempImages = new BufferedImage[4];
		tempImages[0] = loader.getSubImage(4, 5, 41, 20);
		tempImages[1] = loader.getSubImage(4, 31, 41, 20);
		tempImages[2] = loader.getSubImage(4, 57, 41, 20);
		tempImages[3] = loader.getSubImage(4, 80, 41, 20);
		sprites.add(new Animation(tempImages));
		sprites.get(FLYING).setDelay(120);
	}

}
