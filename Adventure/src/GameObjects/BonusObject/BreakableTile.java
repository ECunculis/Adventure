package GameObjects.BonusObject;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import GameObjects.GameObject;
import GameObjects.Player;
import TileMap.TileMap;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class BreakableTile extends GameObject{
	
	private Player player;
	private BufferedImageLoader loader;
	private BufferedImage image;
	private String path;
	private int health;
	private boolean dying;
	private TileMap map;
	private boolean shaking;
	private long shakingStarted, shakingDelay;
	private int xChange, yChange, xDelta, yDelta;

	public BreakableTile(float x, float y, Camera camera, Player player, TileMap map) {
		super(x, y, camera);
		this.player = player;
		this.map = map;
		init();
	}

	public void init() {
		health = 300;
		path = "/BreakableTile.png";
		width = 40;
		height = 80;
		cwidth = width;
		cheight = height;
		scale = 1;
		
		shakingDelay = 300;
		xDelta = 5;
		yDelta = xDelta;
	
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		image = loader.getSubImage(0, 0, 32, 64);
	}

	public void tick() {
		checkIfPlayerHit();
		checkShaking();
		if (health <= 0) {
			shouldRemove = true;
			map.setMap(13, 27, 99);
			map.setMap(12, 27, 99);
			map.setMap(12, 28, 99);
			map.setMap(12, 29, 14);
			map.setMap(13, 29, 14);
			camera.setMinX(0);
			camera.cameraMoveAlongX(200);
		}
	}
	
	public void checkIfPlayerHit() {
		if (player.isHitting()) { 
			if (!player.isHittedOnce()) {
				Rectangle hittingBounds;
				float xPlayer = player.getX();
				float yPlayer = player.getY();
				float playerHeight = player.getHeight();
				float playerWidth = player.getWidth();
				if (player.isFacingRight()) {
					hittingBounds = new Rectangle((int) (xPlayer - xMap), (int) (yPlayer - yMap),
							(int) playerWidth + 30, (int) playerHeight);
				} else
					hittingBounds = new Rectangle((int) (xPlayer - xMap) - 30, (int) (yPlayer - yMap),
							(int) playerWidth + 30, (int) playerHeight);
				if (!dying) {
					if (hittingBounds.intersects(getFullBounds())) {
						health -= player.getHittingPower();
						player.setHittedOnce(true);
						shaking = true;
						shakingStarted = System.currentTimeMillis();
					}
				}
			}
		}
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();
		
		g.drawImage(image, (int) (x - xMap + xChange), (int) (y - yMap + yChange), (int) (width * scale),(int) (height * scale), null);
	}
	
	public void checkShaking() {
		if (shaking) {
			xChange = (int) (Math.random() * xDelta) - (xDelta / 2);
			yChange = (int) (Math.random() * yDelta) - (yDelta / 2);
			if (System.currentTimeMillis() - shakingStarted > shakingDelay) {
				shaking = false;
				xChange = 0;
				yChange = 0;
			}
		}
	}
	
}
