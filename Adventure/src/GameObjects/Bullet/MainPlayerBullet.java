package GameObjects.Bullet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import GameObjects.GameObject;
import GameObjects.Enemy.Enemy;
import TileMap.TileMap;
import Tools.BufferedImageLoader;
import game.Game;
import Tools.Camera;
import Tools.Animation;

public class MainPlayerBullet extends Bullet {

	private int currentPosition; // Either left, right, right up, left up or up

	private final static int RIGHT = 0;
	private final static int LEFT = 1;
	private final static int RIGHT_UP = 2;
	private final static int LEFT_UP = 3;
	private final static int UP = 4;

	public MainPlayerBullet(float x, float y, int currentPosition, TileMap map, Camera camera,
			BufferedImageLoader loader) {
		super(x, y, false, map, camera, loader);
		this.currentPosition = currentPosition;
		this.map = map;
		this.loader = loader;
		init();
	}

	public void init() {
		speed = 10;
		damage = 10;
		scale = 1.5f;
		shouldRemove = false;
		switch (currentPosition) {
		case RIGHT:
			bullet = loader.getSubImage(340, 114, 12, 5);
			break;
		case LEFT:
			bullet = loader.getSubImage(340, 122, 12, 5);
			break;
		case RIGHT_UP:
			bullet = loader.getSubImage(373, 133, 9, 8);
			break;
		case LEFT_UP:
			bullet = loader.getSubImage(359, 133, 9, 8);
			break;
		case UP:
			bullet = loader.getSubImage(343, 132, 5, 12);
			break;

		}
		width = bullet.getWidth();
		height = bullet.getHeight();
		cwidth = width;
		cheight = height;

		switch (currentPosition) {
		case RIGHT:
			VelX = speed;
			break;
		case LEFT:
			VelX = -speed;
			break;
		case UP:
			VelY = -speed;
			break;
		case RIGHT_UP:
			VelX = (float) Math.sin(45) * speed;
			VelY = (float) Math.sin(45) * -speed;
			break;
		case LEFT_UP:
			VelX = (float) Math.sin(45) * -speed;
			VelY = (float) Math.sin(45) * -speed;
			break;
		}
		bulletExplosionSetup();
	}

	public void tick() {
		x += VelX;
		y += VelY;

		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();

		if (exploding) {
			if (explotion.isHasPlayedOnce())
				shouldRemove = true;
			else
				explotion.runAnimation();
		}
		tileCollisionCheck();
	}



	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();

		if (!exploding) {
			g.drawImage(bullet, (int) (x - xMap), (int) (y - yMap), (int) (width * scale), (int) (height * scale),
					null);

		} else if (!explotion.isHasPlayedOnce()) {
			explotion.drawAnimation((int) (x - xMap), (int) (y - yMap), 25, 22, g);
		}

		// Graphics2D g2d = (Graphics2D) g;
		// g2d.setColor(Color.RED);
		// g2d.draw(getFullBounds());
	}

	public void bulletExplosionSetup() {
		BufferedImage[] sprites = new BufferedImage[3];
		sprites[0] = loader.getSubImage(364, 112, 10, 8);
		sprites[1] = loader.getSubImage(380, 111, 10, 8);
		sprites[2] = loader.getSubImage(395, 112, 10, 8);
		explotion = new Animation(sprites);
		explotion.setDelay(80);
	}

	public boolean isExploding() {
		return exploding;
	}

	public void setExploding(boolean exploding) {
		this.exploding = exploding;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	

}
