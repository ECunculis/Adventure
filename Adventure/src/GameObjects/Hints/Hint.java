package GameObjects.Hints;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


import GameObjects.GameObject;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class Hint extends GameObject {
	
	private BufferedImage image;
	private BufferedImageLoader loader;
	private float scale;

	public Hint(float x, float y, Camera camera, BufferedImageLoader loader) {
		super(x, y, camera);
		this.loader = loader;
		init();
	}

	public void init() {
		scale = 2f;
	}
	
	public void loadSubimage(int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		image = loader.getSubImage(x, y, width,height);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();
		
		g.drawImage(image, (int) (x - xMap), (int) (y - yMap), (int) (width * scale),(int) (height * scale), null);
	}

}
