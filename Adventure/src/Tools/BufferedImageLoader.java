package Tools;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class BufferedImageLoader {
	
	BufferedImage image;
	String path;
	
	public BufferedImage loadImage(String path) {
		this.path = path;
		
		try {
			image = ImageIO.read(BufferedImageLoader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		};
		return image;
	}
	
	public BufferedImage getSubImage(int x, int y, int w, int h) {
		return image.getSubimage(x, y, w, h);
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	
}
