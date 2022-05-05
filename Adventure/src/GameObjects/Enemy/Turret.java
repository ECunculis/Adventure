package GameObjects.Enemy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

//import com.sun.xml.internal.ws.policy.spi.AbstractQNameValidator;

import GameObjects.GameObject;
import GameObjects.Player;
import GameObjects.Bullet.Bullet;
import GameObjects.Bullet.MainPlayerBullet;
import GameObjects.Bullet.TurretBullet;
import TileMap.TileMap;
import Tools.Animation;
import Tools.BufferedImageLoader;
import game.Game;
import Tools.Camera;
//import sun.util.logging.resources.logging_zh_TW;

public class Turret extends Enemy {

	// Shooting
	private ArrayList<Bullet> allBullets;
	private long lastTimeShooted;
	private long shootingDelay;
	private long shootingAnimationDelay;
	private long lastTimeShootingAnimation;
	private boolean shooting;
	private boolean shootedOnce;

	// Animation
	private int currentAnimation;

	private static final int FACING_RIGHT = 0;
	private static final int FACING_LEFT = 1;
	private static final int SHOOTING_RIGHT = 2;
	private static final int SHOOTING_LEFT = 3;

	public Turret(float x, float y, boolean facingRight, TileMap map, Camera camera, Player player, long shootingDelay,
			ArrayList<Bullet> allBullets) {
		super(x, y, (int) x, (int) x, facingRight, camera, player);
		this.shootingDelay = shootingDelay;
		this.allBullets = allBullets;
		this.map = map;
		init();
	}

	public void init() {
		health = 30;
		width = 50;
		height = 40;
		cwidth = width;
		cheight = height;
		shouldRemove = false;
		shootedOnce = false;
		dying = false;
		shooting = false;
		hitPower = 20;
		shootingAnimationDelay = 150;
		loader = new BufferedImageLoader();
		loader.loadImage("/EnemyTurret.png");
		spriteInit();
		lastTimeShooted = System.currentTimeMillis();
	}

	public void tick() {
		checkCollisionWithPlayer();
		checkIfPlayerHit();

		if (health <= 0) {
			shouldRemove = true;
		}
		if (System.currentTimeMillis() - lastTimeShooted > shootingDelay) {
			lastTimeShooted = System.currentTimeMillis();
			lastTimeShootingAnimation = lastTimeShooted;
			shooting = true;
		}
		if (shooting) {
			if (System.currentTimeMillis() - lastTimeShootingAnimation > shootingAnimationDelay)
				shooting = false;
			if (facingRight) {
				currentAnimation = SHOOTING_RIGHT;
				if (!shootedOnce)
					allBullets.add(new TurretBullet(x, y, facingRight, map, camera, loader));

			} else {
				currentAnimation = SHOOTING_LEFT;
				if (!shootedOnce)
					allBullets.add(new TurretBullet(x, y, facingRight, map, camera, loader));
			}
			shootedOnce = true;
		} else {
			if (shootedOnce)
				shootedOnce = false;
			if (facingRight) {
				currentAnimation = FACING_RIGHT;
			} else {
				currentAnimation = FACING_LEFT;
			}
		}
		sprites.get(currentAnimation);

		if (hitted) {
			flicker();
		}
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();

		if (render) {
			sprites.get(currentAnimation).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width, (int) height,
					g);
		}

	}

	public void spriteInit() {
		sprites = new LinkedList<>();

		// Facing right
		BufferedImage[] tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(12, 31, 18, 15);
		sprites.add(new Animation(tempImages));
		sprites.get(FACING_RIGHT).setDelay(300);

		// Facing left
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(12, 9, 18, 15);
		sprites.add(new Animation(tempImages));
		sprites.get(FACING_LEFT).setDelay(300);

		// Shooting right
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(36, 31, 18, 15);
		sprites.add(new Animation(tempImages));
		sprites.get(SHOOTING_RIGHT).setDelay(300);

		// Shooting left
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(33, 9, 18, 15);
		sprites.add(new Animation(tempImages));
		sprites.get(SHOOTING_LEFT).setDelay(300);
	}

}
