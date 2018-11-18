package Tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.w3c.dom.css.ElementCSSInlineStyle;

import TileMap.Tile;
import game.Game;

public class Camera {

	// Position
	private int x, y;

	// Offset
	private int xOffSet, yOffSet;

	// Camera shake
	private int xChange, yChange;
	private long shakingStarted;
	private int shakingDelay;
	private int xDelta, yDelta;
	private boolean shaking;

	// Camera move along x axis
	private boolean shouldMove;
	private int xDest, xTemp;
	private boolean movingRight;

	// Bounds
	private int minX, minY;
	private int maxX, maxY;

	public Camera() {
		minX = 0;
		minY = 0;
		xOffSet = 0;
		yOffSet = 0;

		// Camera shaking
		xChange = 0;
		yChange = 0;
		xDelta = 10;
		yDelta = 10;
		shaking = false;
		shakingDelay = 300;

	}

	public void tick() {
		// Check if camera is not out of the map
		checkBounds();
		checkShaking();
		checkMove();
	}

	public void checkShaking() {
		if (shaking) {
			if (System.currentTimeMillis() - shakingStarted > shakingDelay) {
				shaking = false;
			}
			xChange = (int) (Math.random() * xDelta);
			yChange = (int) (Math.random() * yDelta);
			xOffSet += xChange - (xDelta / 2);
			yOffSet += yChange - (yDelta / 2);
		}
	}

	public void cameraMoveAlongX(int xEnd) {
		shouldMove = true;
		xDest = xEnd;
		if (xEnd < xOffSet)
			movingRight = false;
		else
			movingRight = true;
		xTemp = xOffSet;
	}

	public void checkMove() {
			if (shouldMove) {
				if (movingRight) {
					xTemp += 6;
					xOffSet = xTemp;
					if (xOffSet >= xDest) {
						shouldMove = false;
					}
				} else {
					xTemp -= 6;
					xOffSet = xTemp;
					if (xOffSet <= xDest) {
						shouldMove = false;
					}
				}
			}
		}

	public void checkBounds() {
		if (xOffSet < minX)
			xOffSet = minX;
		if (yOffSet < minY)
			yOffSet = minY;
	}

	public void move(int x, int y) {
		xOffSet = x;
		yOffSet = y;
	}

	public void startShaking(int shakingDelay, int xDelta, int yDelta) {
		shaking = true;
		shakingStarted = System.currentTimeMillis();
		this.xDelta = xDelta;
		this.yDelta = yDelta;
		this.shakingDelay = shakingDelay;
	}

	public int getxOffSet() {
		return xOffSet;
	}

	public int getyOffSet() {
		return yOffSet;
	}

	public void setxOffSet(int xOffSet) {
		this.xOffSet = xOffSet;
	}

	public void setyOffSet(int yOffSet) {
		this.yOffSet = yOffSet;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isShaking() {
		return shaking;
	}

	public void setShaking(boolean shaking) {
		this.shaking = shaking;
	}

	public void setShakingStarted(long shakingStarted) {
		this.shakingStarted = shakingStarted;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

}
