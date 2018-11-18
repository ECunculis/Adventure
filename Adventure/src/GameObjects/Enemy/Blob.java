package GameObjects.Enemy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import GameObjects.GameObject;
import GameObjects.Player;
import TileMap.Tile;
import TileMap.TileMap;
import Tools.Animation;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class Blob extends Enemy {

	private static final int WALKING_RIGHT = 0;
	private static final int WALKING_LEFT = 1;
	private static final int DYING_RIGHT = 2;
	private static final int DYING_LEFT = 3;

	public Blob(float x, float y, int startX, int endX, boolean facingRight, Camera camera, Player player) {
		super(x, y, startX, endX, facingRight, camera, player);
		this.player = player;
		init();
	}
	
	public void init() {
		VelX = 2;
		health = 30;
		width = 35;
		height = 50;
		cwidth = width;
		cheight = height;
		shouldRemove = false;
		dying = false;
		gravity = 0.5f;
		hitPower = 20;

		loader = new BufferedImageLoader();
		loader.loadImage("/Blob.png");

		spriteInit();
	}

	public void tick() {
		checkCollisionWithPlayer();
		checkIfPlayerHit();

		if (!dying) {
			if (facingRight) {
				x += VelX;
				sprites.get(WALKING_RIGHT).runAnimation();
			} else {
				x -= VelX;
				sprites.get(WALKING_LEFT).runAnimation();
			}

			// Move enemy between two points (startX, endX)
			if (x >= endX) {
				x = endX;
				facingRight = false;
			} else if (x <= startX) {
				x = startX;
				facingRight = true;
			}

			if (health <= 0) {
				lastTime = System.currentTimeMillis();
				dying = true;
			}
		} else {
			// if health is 0 or less, run animation and set remove to true
			if (facingRight) {
				sprites.get(DYING_RIGHT).runAnimation();
				if (System.currentTimeMillis() - lastTime > 500)
					shouldRemove = true;
			} else {
				sprites.get(DYING_LEFT).runAnimation();
				if (System.currentTimeMillis() - lastTime > 500)
					shouldRemove = true;
			}
		}

	flicker();

	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();


		if (dying) {
			if (facingRight)
				sprites.get(DYING_RIGHT).drawAnimation((int) (x - xMap) - 20, (int) (y - yMap) + 2, (int) width + 40,
						(int) height - 5, g);
			else
				sprites.get(DYING_LEFT).drawAnimation((int) (x - xMap) - 20, (int) (y - yMap) + 2, (int) width + 40,
						(int) height - 5, g);

		} else if (render){
			if (facingRight) {
				sprites.get(WALKING_RIGHT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width, (int) height,
						g);
			} else {
				sprites.get(WALKING_LEFT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width, (int) height,
						g);
			}
		}
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor(Color.RED);
//		g2d.draw(getFullBounds());
//		g2d.draw(getBoundsTop());
	}

	public void spriteInit() {
		sprites = new LinkedList<>();

		// Walking right
		BufferedImage[] tempImages = new BufferedImage[4];
		tempImages[0] = loader.getSubImage(6, 9, 31, 36);
		tempImages[1] = loader.getSubImage(45, 9, 31, 36);
		tempImages[2] = loader.getSubImage(85, 9, 31, 36);
		tempImages[3] = loader.getSubImage(128, 9, 31, 36);
		sprites.add(new Animation(tempImages));
		sprites.get(WALKING_RIGHT).setDelay(120);

		// Walking left
		tempImages = new BufferedImage[4];
		tempImages[0] = loader.getSubImage(130, 56, 31, 36);
		tempImages[1] = loader.getSubImage(91, 56, 31, 36);
		tempImages[2] = loader.getSubImage(51, 56, 31, 36);
		tempImages[3] = loader.getSubImage(8, 56, 31, 36);
		sprites.add(new Animation(tempImages));
		sprites.get(WALKING_LEFT).setDelay(120);

		// Dying right
		tempImages = new BufferedImage[5];
		tempImages[0] = loader.getSubImage(34, 109, 59, 33);
		tempImages[1] = loader.getSubImage(125, 109, 59, 33);
		tempImages[2] = loader.getSubImage(216, 109, 59, 33);
		tempImages[3] = loader.getSubImage(310, 109, 59, 33);
		tempImages[4] = loader.getSubImage(407, 109, 59, 33);
		sprites.add(new Animation(tempImages));
		sprites.get(DYING_RIGHT).setDelay(120);

		// Dying left
		tempImages = new BufferedImage[5];
		tempImages[0] = loader.getSubImage(405, 159, 59, 33);
		tempImages[1] = loader.getSubImage(317, 159, 59, 33);
		tempImages[2] = loader.getSubImage(224, 159, 59, 33);
		tempImages[3] = loader.getSubImage(131, 159, 59, 33);
		tempImages[4] = loader.getSubImage(34, 159, 59, 33);
		sprites.add(new Animation(tempImages));
		sprites.get(DYING_LEFT).setDelay(120);
	}

	public Rectangle getFullBounds() {
		return new Rectangle((int) (x - xMap) + 5, (int) (y - yMap) + 5, (int) (width) - 10, (int) (height) - 10);

	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle((int) (x - xMap) + 5, (int) (y - yMap),(int) (width) - 10,(int) ((height / 4)));

	}

}
