package GameObjects.Bullet;

import java.awt.Graphics;
import java.awt.font.TextHitInfo;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import GameObjects.GameObject;
import TileMap.TileMap;
import Tools.Animation;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class FlyingGunBullet extends Bullet {
	
	private static float speed = 5;

	public FlyingGunBullet(float x, float y, boolean movingRight, TileMap map, Camera camera,
			BufferedImageLoader loader, float VelX, float VelY) {
		super(x, y, movingRight, map, camera, loader);
		this.VelX = VelX;
		this.VelY = VelY;
		init();
	}

	public void init() {
		speed = 5;
		damage = 20;
		exploding = false;
		bullet = loader.getSubImage(59, 8, 10, 10);
		width = 10;
		height = 10;
		cwidth = width;
		cheight = height;
		scale = 0.7f;
		shouldRemove = false;

		// explosion animation init
		BufferedImage[] sprites = new BufferedImage[3];
		sprites[0] = loader.getSubImage(53, 23, 22, 20);
		sprites[1] = loader.getSubImage(53, 46, 22, 20);
		sprites[2] = loader.getSubImage(53, 71, 22, 20);
		explotion = new Animation(sprites);
		explotion.setDelay(80);
	}

	public void tick() {
		x += VelX;
		y += VelY;
		
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
			g.drawImage(bullet, (int) (x - xMap), (int) (y - yMap), (int) (width * scale), (int) (height * scale),
					null);
		} else if (!explotion.isHasPlayedOnce()) {
			explotion.drawAnimation((int) (x - xMap), (int) (y - yMap), 22, 20, g);
		}
	}
	
	public static float getSpeed() {
		return speed;
	}

}
