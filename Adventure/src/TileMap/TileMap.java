package TileMap;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Tools.Camera;

//import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.PrivateKeyResolver;
//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeHost;

import Tools.BufferedImageLoader;
import game.Game;

public class TileMap {
	
	private Camera camera;

	// Map
	private int width, height;
	private int numOfColumns, numOfRows;
	private int[][] map;
	private String pathToMap;
	BufferedImage level;

	// Position
	private int x, y;

	// Offset
	private int xColumnOffSet, yColumnOffSet; 
	private int numColumnsToDraw, numRowsToDraw;

	// Tiles
	private Tile[] tiles; // set of tiles
	private BufferedImage image; // image with tiles
	private int tileSize; // size of the tile (square)
	private String pathToTiles;
	private int numTilesRow;
	private int numTilesColumn;

	public TileMap(int tileSize, Camera camera) {
		this.tileSize = tileSize;
		this.camera = camera;
		
		xColumnOffSet = camera.getxOffSet() / tileSize;
		yColumnOffSet = camera.getyOffSet() / tileSize;
		
		numColumnsToDraw = (Game.getWidthOfCanvas() / tileSize) + 1;
		numRowsToDraw = (Game.getHeightOfCanvas() / tileSize) + 2;
	}

	public void loadTiles(String pathToTiles, int numTilesRow, int numTilesColumn) {
		this.pathToTiles = pathToTiles;
		this.numTilesRow = numTilesRow;
		this.numTilesColumn = numTilesColumn;
		

		tiles = new Tile[numTilesRow * numTilesColumn];

		BufferedImageLoader loader = new BufferedImageLoader();
		loader.loadImage(pathToTiles);
		
		// Get subimage for each tile and store in array
		for (int i = 0; i < numTilesColumn; i++) {
			for (int j = 0; j < numTilesRow; j++) {
				tiles[(i * numTilesRow) + j] = new Tile(
						loader.getSubImage(j * 32, i * 32, 32, 32));
			}
		}
	}

	public void loadLevel(String pathToMap) {
		this.pathToMap = pathToMap;

		BufferedImageLoader loader = new BufferedImageLoader();
		level = loader.loadImage(pathToMap);

		height = level.getHeight();
		width = level.getWidth();

		map = new int[height][width];

		numOfColumns = width / tileSize;
		numOfRows = height / tileSize;


		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = level.getRGB(i, j);

				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel >> 0) & 0xff;
				
				map[i][j] = 99;

				if (red == 0 && green == 0 && blue == 0) {
					map[i][j] = 4;
				} else if (red == 76 && green == 255 && blue == 0) {
					map[i][j] = 0; 
				} else if (red == 0 && green == 255 && blue == 144) {
					map[i][j] = 5; 
				} else if (red == 0 && green == 255 && blue == 255) {
					map[i][j] = 3; 
				} else if (red == 0 && green == 148 && blue == 255) {
					map[i][j] = 1; 
				} else if (red == 0 && green == 38 && blue == 255) {
					map[i][j] = 2; 
				} else if (red == 255 && green == 0 && blue == 110) {
					map[i][j] = 50;
				} else if (red == 182 && green == 255 && blue == 0) {
					map[i][j] = 14;
				}
			}
		}
	}

	public void drawMap(Graphics g) {
		xColumnOffSet = camera.getxOffSet() / tileSize;
		yColumnOffSet = camera.getyOffSet() / tileSize;
		for (int i = xColumnOffSet; i < xColumnOffSet + numColumnsToDraw; i++) {
			for (int j = yColumnOffSet; j < yColumnOffSet + numRowsToDraw; j++) {
				if (map[i][j] != 99)
					g.drawImage(tiles[map[i][j]].getImage(), (int) (i * tileSize) - camera.getxOffSet(), (int) (j * tileSize) - camera.getyOffSet() , tileSize, tileSize, null);
			}
		}
	}

	public int getMap(int i, int j) {
		return map[i][j];
	}

	public int getxColumnOffSet() {
		return xColumnOffSet;
	}

	public int getyColumnOffSet() {
		return yColumnOffSet;
	}

	public int getNumColumnsToDraw() {
		return numColumnsToDraw;
	}

	public int getNumRowsToDraw() {
		return numRowsToDraw;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setMap(int i, int j, int value) {
		this.map[i][j] = value;
	}
	

	
	
}
