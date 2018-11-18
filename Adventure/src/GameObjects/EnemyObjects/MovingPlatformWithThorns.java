package GameObjects.EnemyObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.sun.javafx.collections.MappingChange.Map;

import GameObjects.Player;
import TileMap.TileMap;
import Tools.BufferedImageLoader;
import Tools.Camera;
import game.Game;

public class MovingPlatformWithThorns extends EnemyObjects {

	// Speed of moving down and up
	private float VelYDown, VelYUp;
	// Start coordinate
	private float yStart;

	private String path;

	// Amplitude of moving in y axis
	private float amplitude;

	// States
	private static final int MOVING_DOWN = 0;
	private static final int WAITING_1 = 1;
	private static final int MOVING_UP = 2;
	private static final int WAITING_2 = 3;

	// Pause between moves
	private float pause_1, pause_2;
	private float tempPause;

	// Pause measuring
	private long lastTime;

	// Current state
	private int currentState;

	private BufferedImage image;
	private BufferedImageLoader loader;
	private Player player;

	public MovingPlatformWithThorns(float x, float y, Camera camera, float width, float amplitude, float VelYDown,
			float VelYUp, float pause_1, float pause_2, Player player) {
		super(x, y, camera);
		this.width = width; // Set the width of the platform
		this.amplitude = amplitude;
		this.VelYDown = VelYDown;
		this.VelYUp = VelYUp;
		this.pause_1 = pause_1;
		this.pause_2 = pause_2;
		this.player = player;
		init();
	}

	public void init() {
		height = 49;
		cwidth = width;
		cheight = height;
		hitPower = 50;
		path = "/Objects.png";
		yStart = y;
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		image = loader.getSubImage(2, 2, (int) width, (int) height);
		currentState = WAITING_2;
		lastTime = System.currentTimeMillis();
	}

	public void tick() {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();
		switch (currentState) {
		case MOVING_DOWN:
			y += VelYDown;
			if (y > yStart + amplitude) {
				y = yStart + amplitude;
				currentState = WAITING_1;
				lastTime = System.currentTimeMillis();
				if (x + width > xMap && x < xMap + Game.getWidthOfCanvas()) {
					if (y > yMap && y < yMap + Game.getHeightOfCanvas()) {
						camera.startShaking(300, 10, 10);
					}
				}
			}
			break;
		case WAITING_1:
			if (System.currentTimeMillis() - lastTime > pause_1) {
				currentState = MOVING_UP;
			}
			break;
		case MOVING_UP:
			y -= VelYUp;
			if (y < yStart) {
				y = yStart;
				currentState = WAITING_2;
				lastTime = System.currentTimeMillis();
			}
			break;
		case WAITING_2:
			if (System.currentTimeMillis() - lastTime > pause_2) {
				currentState = MOVING_DOWN;
			}
			break;
		}

		if (!player.isHitted()) {
			if (getFullBounds().intersects(player.getFullBounds())) {
				player.setLastTimeHitted(System.currentTimeMillis());
				player.hit(hitPower);
				player.setHitted(true);
			}
		}
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();

		g.drawImage(image, (int) (x - xMap), (int) (y - yMap), (int) width, (int) height, null);

		// Graphics2D g2d = (Graphics2D) g;
		// g2d.setColor(Color.RED);
		// g2d.draw(getFullBounds());

	}

	public Rectangle getFullBounds() {
		return new Rectangle((int) (x - xMap) + 5, (int) (y - yMap), (int) (width) - 10, (int) (height) - 5);

	}

}
