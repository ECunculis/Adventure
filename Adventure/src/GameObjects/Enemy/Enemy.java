package GameObjects.Enemy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import org.w3c.dom.css.ElementCSSInlineStyle;

import GameObjects.GameObject;
import GameObjects.Player;
import TileMap.TileMap;
import Tools.Animation;
import Tools.BufferedImageLoader;
import Tools.Camera;

public abstract class Enemy extends GameObject {
	protected boolean movingRight, movingLeft;
	protected boolean facingRight;
	protected LinkedList<Animation> sprites; // Arraylist of animations
	protected BufferedImage spriteSheet;
	protected BufferedImageLoader loader;
	protected int speed;
	protected int startX;
	protected int endX;
	protected int health;
	protected int hitPower;
	protected float gravity;
	protected Player player;

	protected long lastTime;

	protected boolean shouldRemove;
	protected boolean dying;

	// Map
	protected TileMap map;

	public Enemy(float x, float y, int startX, int endX, boolean facingRight, Camera camera, Player player) {
		super(x, y, camera);
		this.startX = startX;
		this.endX = endX;
		this.facingRight = facingRight;
		this.map = map;
		this.player = player;
		loader = new BufferedImageLoader();
	}

	public void checkCollisionWithPlayer() {
		if (!dying) {
			if (player.getBounds().intersects(getBoundsTop()) && player.getVelY() > 0) {
				health = 0;
				player.setJumping(true);
				player.setVelY(player.getJumpVel());
			} else if (!player.isHitted()) {
				if ((player.getFullBounds().intersects(getFullBounds())) && !hitted) {
					player.setLastTimeHitted(System.currentTimeMillis());
					player.hit(hitPower);
					player.setHitted(true);
				}
			}
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
					}
				}
			}
		}
	}

	public Rectangle getBoundsTop() {
		return new Rectangle((int) ((x + (width / 100) * 15) - xMap) + 2, (int) ((y) - yMap),
				(int) (((width / 100) * 70)) - 4, (int) ((height / 2)));

	}

	public boolean isShouldRemove() {
		return shouldRemove;
	}

	public int hit(int x) {
		health -= x;
		return health;
	}

	public boolean isDying() {
		return dying;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}

	public int getHitPower() {
		return hitPower;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

}
