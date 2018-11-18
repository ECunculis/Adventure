package TileMap;

import java.awt.image.BufferedImage;

public class Tile {

	BufferedImage image;
	
	public Tile(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	
}
