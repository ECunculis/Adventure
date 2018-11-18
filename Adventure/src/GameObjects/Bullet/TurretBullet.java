package GameObjects.Bullet;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import GameObjects.Enemy.Enemy;
import TileMap.TileMap;
import Tools.BufferedImageLoader;
import game.Game;
import Tools.Camera;
import Tools.Animation;

public class TurretBullet extends Bullet {

	private BufferedImage bulletRight, bulletLeft;

	public TurretBullet(float x, float y, boolean movingRight, TileMap map, Camera camera, BufferedImageLoader loader) {
		super(x, y, movingRight, map, camera, loader);
		init();
	}
	
	public void init() {
		exploding = false;
		width = 16;
		height = 7;
		cwidth = width;
		cheight = height;
		speed = 5;
		damage = 20;
		shouldRemove = false;
		bulletRight = loader.getSubImage(9, 52, 12, 5);
		bulletLeft = loader.getSubImage(79, 6, 12, 5);
		if (movingRight)
			VelX = speed;
		else
			VelX = -speed;
		
		BufferedImage[] sprites = new BufferedImage[3];
		sprites[0] = loader.getSubImage(33, 50, 10, 8);
		sprites[1] = loader.getSubImage(49, 49, 10, 8); 
		sprites[2] = loader.getSubImage(64, 50, 10, 8);
		explotion = new Animation(sprites);
		explotion.setDelay(80);
	}

	public void tick() {
		x += VelX;

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

		// Graphics2D g2d = (Graphics2D) g;
		// g2d.setColor(Color.RED);
		// g2d.draw(getFullBounds());

		if (!exploding) {
			if (movingRight)
				g.drawImage(bulletRight, (int) (x - xMap) + (int) width, (int) (y - yMap) + 2, (int) width, (int) height, null);
			else 
				g.drawImage(bulletLeft, (int) (x - xMap) + 2, (int) (y - yMap) + 2, (int) width, (int) height, null);
		} else if (!explotion.isHasPlayedOnce()) {
			explotion.drawAnimation((int) (x - xMap), (int) (y - yMap), 25, 22, g);
		}
	}
	

}
