package GameObjects.BonusObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import GameObjects.GameObject;
import GameObjects.Player;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class BonusBullet extends GameObject{
	
	private Player player;
	private BufferedImageLoader loader;
	private BufferedImage image;
	private String path;
	private int numOfBullets;
	
	private float moveSpeed;
	private float yTop, yBottom;
	private boolean movingUp;

	public BonusBullet(float x, float y, Camera camera, Player player, int numOfBullets) {
		super(x, y, camera);
		this.player = player;
		this.numOfBullets = numOfBullets;
		init();
	}

	public void init() {
		path = "/Bonus.png";
		width = 21;
		height = 22;
		cwidth = width;
		cheight = height;
		scale = 1;
		
		moveSpeed = 0.2f;
		yTop = y - 8;
		yBottom = y;
		movingUp = true;
		
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		image = loader.getSubImage(1, 1, 21, 22);
	}

	public void tick() {
		if (player.getFullBounds().intersects(getFullBounds())) {
			player.addBullets(numOfBullets);
			shouldRemove = true;
		}
		
		if (movingUp) {
			y -= moveSpeed;
			if (y < yTop) {
				y = yTop;
				movingUp = false;
			} 
		} else {
			y += moveSpeed;
			if (y > yBottom) {
				y = yBottom;
				movingUp = true;
			}
		}
		
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();
		
		g.drawImage(image, (int) (x - xMap), (int) (y - yMap), (int) width,(int) height, null);
	}
	
}
