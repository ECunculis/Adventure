package GameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.w3c.dom.css.Rect;

import com.sun.javafx.collections.MappingChange.Map;

import TileMap.Tile;
import TileMap.TileMap;
import Tools.Camera;
import javafx.scene.chart.XYChart;

public abstract class GameObject {

	protected float x, y;
	protected float width, height;
	protected float scale;
	protected float VelX, VelY;
	protected float xMap, yMap; // Tiles position
	protected Camera camera;
	protected boolean shouldRemove;
	
	// collision box
	protected float cx, cy; // Relatively to x and y of object
	protected float cwidth, cheight;
	
	protected boolean hitted, render; // Object starts flickering if hit
	protected long lastTimeHitted; // Timer for animation of object being hitted

	public GameObject(float x, float y, Camera camera) {
		this.x = x;
		this.y = y;
		this.camera = camera;
		// By default
		cx = 0;
		cy = 0;
		VelX = 0;
		VelY = 0;
		render = true;
	}
	
	public abstract void init();

	public abstract void tick();

	public abstract void render(Graphics g);
	
	public void flicker() {
		if (System.currentTimeMillis() - lastTimeHitted > 1000) {
			hitted = false;
			render = true;
		} else {
			if ((int) ((int) ((System.currentTimeMillis() - lastTimeHitted) / 100)) % 2 == 0) {
				render = false;
			} else {
				render = true;
			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) ((x + ((width / 100) * 15) - xMap) + 6), (int) ((y) - yMap + (height / 2)),(int)  (((width / 100) * 70)) - 12,(int) ((height / 2) - 1));
	}

	public Rectangle getBoundsTop() {
		return new Rectangle((int) ((x + (width / 100) * 15) - xMap) + 6, (int) ((y) - yMap),(int) (((width / 100) * 70)) - 12,(int) ((height / 2)));

	}

	public Rectangle getBoundsRight() {
		return new Rectangle((int) ((x + (width / 100) * 85) - xMap) - 6,(int) (y + ((height / 100) * 6) - yMap),(int) ((width / 100) * 15) - 1,(int) ((height / 100) * 88));

	}

	public Rectangle getBoundsLeft() {
		return new Rectangle((int) (x - xMap) + 6,(int) (y + ((height / 100) * 6) - yMap),(int) ((width / 100) * 15) - 1,(int) ((height / 100) * 88));

	}
	
	public Rectangle getFullBounds() {
		return new Rectangle((int) (x + cx - xMap),(int) (y + cy - yMap),(int) (cwidth) ,(int) (cheight));

	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getVelX() {
		return VelX;
	}

	public void setVelX(float velX) {
		VelX = velX;
	}

	public float getVelY() {
		return VelY;
	}

	public void setVelY(float velY) {
		VelY = velY;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public boolean isShouldRemove() {
		return shouldRemove;
	}

	public void setShouldRemove(boolean shouldRemove) {
		this.shouldRemove = shouldRemove;
	}
	
	public boolean isHitted() {
		return hitted;
	}

	public void setHitted(boolean hitted) {
		this.hitted = hitted;
	}

	public void setLastTimeHitted(long lastTimeHitted) {
		this.lastTimeHitted = lastTimeHitted;
	}
	
	
	

}
