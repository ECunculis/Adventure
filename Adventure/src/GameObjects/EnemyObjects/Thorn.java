package GameObjects.EnemyObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import GameObjects.Player;
import TileMap.TileMap;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class Thorn extends EnemyObjects {

	private BufferedImageLoader loader;
	private BufferedImage image;
	private String path;
	private Player player;

	public Thorn(int x, int y, Camera camera, Player player) {
		super(x, y, camera);
		this.player = player;
		init();
	}

	public void init() {
		VelX = 0;
		VelY = 0;
		width = 12;
		height = 26;
		cwidth = width;
		cheight = height;
		hitPower = 50;
		path = "/Thorns.png";
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		image = loader.getSubImage(0, 0, 12, 26);
	}

	public void tick() {
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
	}

}
