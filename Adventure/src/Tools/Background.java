package Tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import TileMap.TileMap;
import game.Game;
import Tools.Camera;

public class Background {

	private int width, height;
	private float x, y;
	private float xTemp;
	private float x2, y2; // Coordinates for absolute positioning
	private float scrollSpeed;
	private float moveSpeed;
	private BufferedImageLoader loader;
	private BufferedImage background;
	Camera camera;

	public Background(float x, float y, String path, Camera camera, float scrollSpeed, float moveSpeed) {
		this.x2 = x;
		this.y2 = y;
		this.camera = camera;
		this.scrollSpeed = scrollSpeed;
		this.moveSpeed = moveSpeed;

		loader = new BufferedImageLoader();
		background = loader.loadImage(path);

		height = background.getHeight();
		width = background.getWidth();
	}

	public void tick() {
		// Update background coordinated
		x = -(camera.getxOffSet() / scrollSpeed) + x2;
//		y = -(camera.getyOffSet() / scrollSpeed) + y2;

		x2 -= moveSpeed;
		
		// Move clouds to the right 
		if (x2 + width < camera.getxOffSet() / scrollSpeed)
			x2 += width;
		// Move clouds to the left 
		if (camera.getxOffSet() / scrollSpeed < x2)
			x2 -= width;

	}

	public void render(Graphics g) {
		g.drawImage(background, (int) x, (int) y, width, height, null);
		// Draw new clouds on the right if right side of the
		// clouds is on the screen
		if (x + width < camera.getxOffSet() + Game.getWidthOfCanvas() * 1.5) {
			g.drawImage(background, (int) x + width, (int) y, width, height, null);
		} 
	}
}
