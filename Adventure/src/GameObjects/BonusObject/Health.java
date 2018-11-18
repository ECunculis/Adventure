package GameObjects.BonusObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import GameObjects.GameObject;
import GameObjects.Player;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class Health extends GameObject{
	
	private Player player;
	private BufferedImageLoader loader;
	private BufferedImage image;
	private String path;
	private int healingPower;
	
	private float moveSpeed;
	private float yTop, yBottom;
	private boolean movingUp;

	public Health(float x, float y, Camera camera, Player player, int healingPower) {
		super(x, y, camera);
		this.player = player;
		this.healingPower = healingPower;
		init();
	}

	public void init() {
		path = "/HealthBar.png";
		width = 15;
		height = 12;
		cwidth = width;
		cheight = height;
		scale = 1;
		
		moveSpeed = 0.2f;
		yTop = y - 8;
		yBottom = y;
		movingUp = true;
		
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		image = loader.getSubImage(4, 5, 15, 12);
	}

	public void tick() {
		if (player.getFullBounds().intersects(getFullBounds())) {
			player.addHealth(healingPower);
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
		
		g.drawImage(image, (int) (x - xMap), (int) (y - yMap), (int) (width * scale),(int) (height * scale), null);
	}
	
}
