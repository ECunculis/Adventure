package GameObjects.Bullet;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import GameObjects.GameObject;
import TileMap.TileMap;
import Tools.Animation;
import Tools.BufferedImageLoader;
import Tools.Camera;
//import sun.awt.image.OffScreenImage;

public abstract class Bullet extends GameObject {

	protected BufferedImageLoader loader;
	protected BufferedImage bullet;
	protected boolean movingRight;
	protected static float speed;
	protected TileMap map;
	protected int damage;
	protected Animation explotion;
	protected boolean exploding;

	public Bullet(float x, float y, boolean movingRight, TileMap map, Camera camera, BufferedImageLoader loader) {
		super(x, y, camera);
		this.movingRight = movingRight;
		this.map = map;
		this.loader = loader;
	}

	public void tileCollisionCheck() {
		int tilesize = map.getTileSize();
		int xStart = (int) (x / tilesize) - 2;
		int yStart = (int) (y / tilesize) - 2;
		int xEnd = (int) ((x + width) / tilesize) + 2;
		int yEnd = (int) ((y + height) / tilesize) + 2;

		 if (xStart < 0) {
			 xStart = 0;
		 }
		 if (xEnd > map.getWidth()) {
			 xEnd = map.getWidth();
		 }
		
		 if (yStart < 0)  {
			 yStart = 0;
		 }
		 if (yEnd > map.getHeight()) { 
			 yEnd = map.getHeight();
		 }
		 

		Rectangle tempRect; // Tile rectangle for collision check

		// Check collision with tiles
		for (int i = xStart; i < xEnd; i++) {
			for (int j = yStart; j < yEnd; j++) {
				if (map.getMap(i, j) <= 7) {
					tempRect = new Rectangle((int) ((i * tilesize) - xMap), (int) ((j * tilesize) - yMap), tilesize,
							tilesize);
					if (getFullBounds().intersects(tempRect)) {
						if (!exploding) {
							exploding = true;
							VelX = 0;
							VelY = 0;
							x -= 5;
							y -= 8;
						}
					}
				}
			}
		}
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public boolean isExploding() {
		return exploding;
	}

	public void setExploding(boolean exploding) {
		this.exploding = exploding;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
