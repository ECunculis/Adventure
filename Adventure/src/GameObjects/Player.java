package GameObjects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardUpLeftHandler;

import org.w3c.dom.css.ElementCSSInlineStyle;

import com.sun.corba.se.spi.activation._ActivatorImplBase;
import com.sun.jndi.url.dns.dnsURLContext;
import com.sun.org.apache.xpath.internal.operations.Gt;
import com.sun.prism.impl.BaseMesh.FaceMembers;

import GameObjects.Bullet.Bullet;
import GameObjects.Bullet.MainPlayerBullet;
import GameObjects.Enemy.Enemy;
import TileMap.TileMap;
import Tools.Animation;
import Tools.BufferedImageLoader;
import Tools.KeyInput;
import game.Game;
import javafx.scene.chart.XYChart;
import Tools.Camera;
import GameObjects.EnemyObjects.*;

public class Player extends GameObject {

	// Moving, jumping, falling
	private boolean movingRight, movingLeft;
	private boolean facingRight;
	private boolean falling, jumping;
	private boolean jumping2, wReleased; // For jumping 2nd time need to check if w was released
	private boolean duck;
	private float gravity, acceleration, jumpVel;
	private float maxVelX, maxVelXInJump;

	// Health
	private float health;
	private float maxHealth;
	private boolean dying;

	private TileMap tileMap;
	private boolean hitting;

	private boolean hittedOnce; // Hit once per each key press
	private int hittingPower;
	private LinkedList<Animation> sprites; // Arraylist of animations
	private BufferedImage spriteSheet;
	private BufferedImageLoader loader;

	// Shooting
	private ArrayList<Bullet> allBullets;
	private long lastTimeShooted;
	private long shootingDelay;
	private int numOfBullets;
	private boolean shooting;

	private Camera camera;

	private final static int FACING_RIGHT = 0;
	private final static int FACING_LEFT = 1;
	private final static int RUNNING_RIGHT = 2;
	private final static int RUNNING_LEFT = 3;
	private final static int JUMPING_RIGHT = 4;
	private final static int JUMPING_LEFT = 5;
	private final static int FALLING_RIGHT = 6;
	private final static int FALLING_LEFT = 7;
	private final static int SHOOTING_RIGHT = 8;
	private final static int SHOOTING_LEFT = 9;
	private final static int SHOOTING_RIGHT_45 = 10;
	private final static int SHOOTING_LEFT_45 = 11;
	private final static int SHOOTING_RIGHT_UP = 12;
	private final static int SHOOTING_LEFT_UP = 13;
	private final static int HITTING_RIGHT = 14;
	private final static int HITTING_LEFT = 15;
	private final static int DUCK_RIGHT = 16;
	private final static int DUCK_LEFT = 17;

	private int currentAnimation;

	public Player(float x, float y, TileMap tileMap, Camera camera, ArrayList<Bullet> allBullets) {
		super(x, y, camera);
		this.tileMap = tileMap;
		this.allBullets = allBullets;
		this.camera = camera;
		init();
	}

	public void init() {
		render = true;
		width = 50;
		height = 80;

		// collision box
		cwidth = width - 20;
		cheight = height - 10;
		cx = 10;
		cy = 5;

		gravity = 1.2f;
		maxVelX = 8;
		maxVelXInJump = 2;
		acceleration = maxVelX;
		jumpVel = -18;

		numOfBullets = 0;
		shootingDelay = 150000000l;

		hittingPower = 100;
		maxHealth = 200;

		hittedOnce = false;
		health = maxHealth;
		lastTimeShooted = System.nanoTime();
		loader = new BufferedImageLoader();
		loader.loadImage("/PlayerSheet.png");
		spriteSetup();
	}

	public void tick() {
		if (health <= 0) {
			dying = true;
			hitted = false; // Stop flickering
		}

		if (KeyInput.isS()) {
			duck = true;
		} else
			duck = false;

		// If pressing D, go to the right
		if ((KeyInput.isD() && !KeyInput.isA()) || (KeyInput.isA() && KeyInput.isD())) {
			if (!hitting)
				facingRight = true;
			movingRight = true;
			movingLeft = false;
			// If pressing A go to the left
		} else if (KeyInput.isA() && !KeyInput.isD()) {
			if (!hitting)
				facingRight = false;
			movingRight = false;
			movingLeft = true;
		} else {
			// Otherwise player is not moving
			movingRight = false;
			movingLeft = false;
		}
		// If player is not jumping then jump
		if (KeyInput.isW() && !jumping) {
			jumping = true;
			VelY = jumpVel;
			wReleased = false;
			if (falling) jumping2 = true;
		} else if (KeyInput.isW() && !jumping2 && wReleased) {
			jumping2 = true;
			VelY = jumpVel;
			wReleased = false;
		}
		if (!KeyInput.isW())
			wReleased = true;

		// If player is not jumping or falling then hit
		if (KeyInput.isCTRL()) {
			hitting = true;
		} else if (!KeyInput.isCTRL() && !hitting) {
			// Set hitting to false
			if (facingRight)
				sprites.get(HITTING_RIGHT).setHasPlayedOnce(false);
			else
				sprites.get(HITTING_LEFT).setHasPlayedOnce(false);
		}

		// If player is not jumping, falling, hitting or moving then shoot
		if (KeyInput.isSPACE() && numOfBullets != 0) {
			shooting = true;
		} else {
			shooting = false;
		}
		if (movingRight) {
			if (VelX < maxVelXInJump) {
				VelX += acceleration;
			}
		} else if (movingLeft) {
			if (VelX > -maxVelXInJump) {
				VelX -= acceleration;
			}
		} else {
			VelX = 0;
		}
		if ((jumping || falling)) {
			VelY += gravity;
			if (!hitting) {
			if (facingRight) {
				if (VelY < 0)
					// sprites.get(JUMPING_RIGHT).runAnimation();
					currentAnimation = JUMPING_RIGHT;
				else
					// sprites.get(FALLING_RIGHT).runAnimation();
					currentAnimation = FALLING_RIGHT;
			} else if (!facingRight)
				if (VelY < 0)
					// sprites.get(JUMPING_LEFT).runAnimation();
					currentAnimation = JUMPING_LEFT;
				else
					// sprites.get(FALLING_LEFT).runAnimation();
					currentAnimation = FALLING_LEFT;
			}

		} 
		if (hitting) {
			if (facingRight && !sprites.get(HITTING_RIGHT).isHasPlayedOnce()) {
				// sprites.get(HITTING_RIGHT).runAnimation();
				currentAnimation = HITTING_RIGHT;
			} else if (!facingRight && !sprites.get(HITTING_LEFT).isHasPlayedOnce()) {
				// sprites.get(HITTING_LEFT).runAnimation();
				currentAnimation = HITTING_LEFT;
			} else if (facingRight && sprites.get(HITTING_RIGHT).isHasPlayedOnce()) {
				hitting = false;
				hittedOnce = false;
			} else if (!facingRight && sprites.get(HITTING_LEFT).isHasPlayedOnce()) {
				hitting = false;
				hittedOnce = false;
			}

		} else if (!jumping && !falling && !duck) {
			VelY = 0;
			if (movingRight) {
				if (VelX < 0)
					VelX = 0;
				VelX += acceleration;
				if (VelX > maxVelX)
					VelX = maxVelX;
				currentAnimation = RUNNING_RIGHT;
				// sprites.get(RUNNING_RIGHT).runAnimation();
			} else if (movingLeft) {
				if (VelX > 0)
					VelX = 0;
				VelX -= acceleration;
				if (VelX < -maxVelX)
					VelX = -maxVelX;
				// sprites.get(RUNNING_LEFT).runAnimation();
				currentAnimation = RUNNING_LEFT;
			} else {
				VelX = 0;
				if (facingRight)
					// sprites.get(FACING_RIGHT);
					currentAnimation = FACING_RIGHT;
				else
					// sprites.get(FACING_LEFT);
					currentAnimation = FACING_LEFT;
			}
		}
		if (shooting && !hitting && !duck) {
			VelX = 0;
			if (!KeyInput.isARROW_UP()) {
				if (facingRight) {
					currentAnimation = SHOOTING_RIGHT;
					// sprites.get(SHOOTING_RIGHT).runAnimation();
					if (System.nanoTime() - lastTimeShooted > shootingDelay) {
						lastTimeShooted = System.nanoTime();
						numOfBullets--;
						allBullets
								.add(new MainPlayerBullet(x + width, y + (height / 2) - 3, 0, tileMap, camera, loader));
					}
				} else {
					currentAnimation = SHOOTING_LEFT;
					sprites.get(SHOOTING_LEFT).runAnimation();
					if (System.nanoTime() - lastTimeShooted > shootingDelay) {
						lastTimeShooted = System.nanoTime();
						numOfBullets--;
						allBullets.add(new MainPlayerBullet(x - 12, y + (height / 2) - 3, 1, tileMap, camera, loader));
					}
				}
				if (KeyInput.isARROW_RIGHT())
					facingRight = true;
				else if (KeyInput.isARROW_LEFT())
					facingRight = false;
			} else if (KeyInput.isARROW_UP()) {
				if (KeyInput.isARROW_RIGHT()) {
					currentAnimation = SHOOTING_RIGHT_45;
					if (System.nanoTime() - lastTimeShooted > shootingDelay) {
						lastTimeShooted = System.nanoTime();
						numOfBullets--;
						allBullets.add(new MainPlayerBullet(x + 58, y + (height / 2) - 23, 2, tileMap, camera, loader));
					}
					facingRight = true;
				} else if (KeyInput.isARROW_LEFT()) {
					currentAnimation = SHOOTING_LEFT_45;
					if (System.nanoTime() - lastTimeShooted > shootingDelay) {
						lastTimeShooted = System.nanoTime();
						numOfBullets--;
						allBullets.add(new MainPlayerBullet(x - 20, y + (height / 2) - 25, 3, tileMap, camera, loader));
					}
					facingRight = false;
				} else if (facingRight) {
					currentAnimation = SHOOTING_RIGHT_UP;
					if (System.nanoTime() - lastTimeShooted > shootingDelay) {
						lastTimeShooted = System.nanoTime();
						numOfBullets--;
						allBullets.add(new MainPlayerBullet(x + 47, y + (height / 2) - 43, 4, tileMap, camera, loader));
					}
				} else {
					currentAnimation = SHOOTING_LEFT_UP;
					if (System.nanoTime() - lastTimeShooted > shootingDelay) {
						lastTimeShooted = System.nanoTime();
						numOfBullets--;
						allBullets.add(new MainPlayerBullet(x - 4, y + (height / 2) - 50, 4, tileMap, camera, loader));
					}
				}
			}
		}
		if (duck) {
			VelX = 0;
			if (facingRight)
				currentAnimation = DUCK_RIGHT;
			else
				currentAnimation = DUCK_LEFT;
		}

		sprites.get(currentAnimation).runAnimation();

		// Set current frames to 0 and lastTime to 0 if not hitting
		// because otherwise the animation will start from second frame instead of first
		if (!hitting) {
			sprites.get(HITTING_LEFT).setCurrentFrame(0);
			sprites.get(HITTING_RIGHT).setCurrentFrame(0);
			sprites.get(HITTING_LEFT).setLastTime(0);
			sprites.get(HITTING_RIGHT).setLastTime(0);
		}

		// Check collision with tiles
		checkCollision();

		// Start flickering if hitted
		if (hitted) {
			flicker();
		}

	}

	public void checkCollision() {

		int tileSize = tileMap.getTileSize();

		float tempVelX = Math.abs(VelX);
		float tempVelY = Math.abs(VelY);

		float tempX = x + cx;
		float tempY = y + cy;

		float tempWidth = cwidth;
		float tempHeight = cheight;

		float finalX = tempX;
		float finalY = tempY;

		float VelDeltaX = 0;
		float VelDeltaY = 0;

		int velIterations = 0;

		if (tempVelX > tempVelY) {
			velIterations = (int) tempVelX;
			VelDeltaX = VelX / velIterations;
			VelDeltaY = VelY / velIterations;
		} else {
			velIterations = (int) tempVelY;
			VelDeltaX = VelX / velIterations;
			VelDeltaY = VelY / velIterations;
		}

		boolean movingUp = false;
		if (VelY < 0) {
			movingUp = true;
		}

		for (int k = 0; k < (int) velIterations; k++) {

			finalX += VelDeltaX;
			finalY += VelDeltaY;

			// Check for x collision
			for (int i = ((int) tempX / tileSize) - 1; i < ((int) (tempX + tempWidth) / tileSize) + 1; i++) {
				for (int j = ((int) tempY / tileSize) - 1; j < ((int) (tempY + tempHeight) / tileSize) + 1; j++) {

					if (i < 0)
						i = 0;
					if (j < 0)
						j = 0;

					int tileType = tileMap.getMap(i, j);
					if (tileType <= 50) {

						Rectangle playerRect = new Rectangle((int) finalX, (int) tempY, (int) tempWidth,
								(int) tempHeight);
						Rectangle tileRect = new Rectangle((int) ((i * tileSize)), (int) ((j * tileSize)), tileSize,
								tileSize);

						if (playerRect.intersects(tileRect)) {

							// Tile corners
							int xTileLeft = (int) ((i * tileSize));
							int xTileRight = (int) ((i * tileSize) + tileSize);

							// if moving right
							if (movingRight) {
								finalX = xTileLeft - tempWidth;
								VelX = 0;
								VelDeltaX = 0;
								// if moving left
							} else if (movingLeft) {
								finalX = xTileRight;
								VelX = 0;
								VelDeltaX = 0;
							}
						}

					}
				}
			}
			tempX = finalX;

			// Check for y collision
			for (int i = ((int) tempX / tileSize) - 1; i < ((int) (tempX + tempWidth) / tileSize) + 1; i++) {
				for (int j = ((int) tempY / tileSize) - 1; j < ((int) (tempY + tempHeight) / tileSize) + 1; j++) {

					if (i < 0)
						i = 0;
					if (j < 0)
						j = 0;

					int tileType = tileMap.getMap(i, j);
					if (tileType <= 50) {

						Rectangle playerRect = new Rectangle((int) tempX, (int) finalY, (int) tempWidth,
								(int) tempHeight);
						Rectangle tileRect = new Rectangle((int) ((i * tileSize)), (int) ((j * tileSize)), tileSize,
								tileSize);

						if (playerRect.intersects(tileRect)) {

							// Tile corners
							int yTileTop = (int) ((j * tileSize));
							int yTileBottom = (int) ((j * tileSize) + tileSize);

							// if moving down
							if (!movingUp) {
								finalY = yTileTop - tempHeight - 1;
								jumping = false;
								jumping2 = false;
								falling = false;
								VelY = 0;
								VelDeltaY = 0;
								// if moving up
							} else if (movingUp) {
								finalY = yTileBottom;
								falling = true;
								VelY = 0;
								VelDeltaY = 0;
							}

						}
					}
				}
			}
			tempY = finalY;

		}

		// Check if should fall
		falling = true;
		int j = (int) ((tempY + tempHeight) / tileSize);
		for (int i = ((int) tempX / tileSize) - 1; i < ((int) (tempX + tempWidth) / tileSize) + 1; i++) {
			if (i < 0)
				i = 0;
			if (j < 0)
				j = 0;

			int tileType = tileMap.getMap(i, j);
			if (tileType <= 50) {

				Rectangle playerRect = new Rectangle((int) tempX, (int) tempY, (int) tempWidth, (int) tempHeight + 1);
				Rectangle tileRect = new Rectangle((int) ((i * tileSize)), (int) ((j * tileSize)), tileSize, tileSize);

				if (playerRect.intersects(tileRect)) {
					falling = false;
					break;
				}
			}
		}
		y = tempY - cy;
		x = tempX - cx;
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();

		if (render) {
			switch (currentAnimation) {
			case RUNNING_RIGHT:
				sprites.get(RUNNING_RIGHT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width, (int) height,
						g);
				break;
			case RUNNING_LEFT:
				sprites.get(RUNNING_LEFT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width, (int) height,
						g);
				break;
			case FACING_RIGHT:
				sprites.get(FACING_RIGHT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width, (int) height,
						g);
				break;
			case FACING_LEFT:
				sprites.get(FACING_LEFT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width, (int) height,
						g);
				break;
			case JUMPING_RIGHT:
				sprites.get(JUMPING_RIGHT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width,
						(int) height + 9, g);
				break;
			case JUMPING_LEFT:
				sprites.get(JUMPING_LEFT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width,
						(int) height + 9, g);
				break;
			case FALLING_RIGHT:
				sprites.get(FALLING_RIGHT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width,
						(int) height + 20, g);
				break;
			case FALLING_LEFT:
				sprites.get(FALLING_LEFT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width,
						(int) height + 20, g);
				break;
			case SHOOTING_RIGHT:
				sprites.get(SHOOTING_RIGHT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width + 43,
						(int) height, g);
				break;
			case SHOOTING_LEFT:
				sprites.get(SHOOTING_LEFT).drawAnimation((int) (x - xMap) - 43, (int) (y - yMap), (int) width + 43,
						(int) height, g);
				break;
			case SHOOTING_RIGHT_45:
				sprites.get(SHOOTING_RIGHT_45).drawAnimation((int) (x - xMap), (int) (y - yMap) - 1, (int) width + 40,
						(int) height + 1, g);
				break;
			case SHOOTING_LEFT_45:
				sprites.get(SHOOTING_LEFT_45).drawAnimation((int) (x - xMap) - 40, (int) (y - yMap) - 1,
						(int) width + 40, (int) height + 1, g);
				break;
			case SHOOTING_RIGHT_UP:
				sprites.get(SHOOTING_RIGHT_UP).drawAnimation((int) (x - xMap), (int) (y - yMap) - 27, (int) width + 5,
						(int) height + 27, g);
				break;
			case SHOOTING_LEFT_UP:
				sprites.get(SHOOTING_LEFT_UP).drawAnimation((int) (x - xMap) - 5, (int) (y - yMap) - 27,
						(int) width + 5, (int) height + 27, g);
				break;
			case HITTING_RIGHT:
				if (facingRight)
					sprites.get(HITTING_RIGHT).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) width + 15,
							(int) height, g);
				break;
			case HITTING_LEFT:
				sprites.get(HITTING_LEFT).drawAnimation((int) (x - xMap) - 15, (int) (y - yMap), (int) width + 15,
						(int) height, g);
				break;
			case DUCK_RIGHT:
				sprites.get(DUCK_RIGHT).drawAnimation((int) (x - xMap), (int) (y - yMap) + 28, (int) width,
						(int) height - 28, g);
				break;
			case DUCK_LEFT:
				sprites.get(DUCK_LEFT).drawAnimation((int) (x - xMap), (int) (y - yMap) + 28, (int) width,
						(int) height - 28, g);
				break;
			}
		}

		// g.setColor(Color.BLUE);
		// g.fillRect((int) (x - xMap), (int) (y - yMap), (int) width, (int) height);
		//
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor(Color.RED);
//		g2d.draw(getFullBounds());
		// g2d.draw(getBounds());
		// g2d.draw(getBoundsTop());
		// g2d.draw(getBoundsLeft());
		// g2d.draw(getBoundsRight());
	}

	public void spriteSetup() {
		sprites = new LinkedList<>();

		// Get Sprite sheet
		spriteSheet = loader.getSubImage(0, 0, 1023, 1023);
		// Create array of temp sprites

		// Heading right
		BufferedImage[] tempImages = new BufferedImage[4];
		tempImages[0] = loader.getSubImage(12, 0, 41, 64);
		tempImages[1] = loader.getSubImage(77, 0, 41, 64);
		tempImages[2] = loader.getSubImage(141, 0, 41, 64);
		tempImages[3] = loader.getSubImage(205, 0, 41, 64);
		sprites.add(new Animation(tempImages));
		sprites.get(FACING_RIGHT).setDelay(80);

		// Heading left
		tempImages = new BufferedImage[4];
		tempImages[0] = loader.getSubImage(12, 64, 41, 64);
		tempImages[1] = loader.getSubImage(77, 64, 41, 64);
		tempImages[2] = loader.getSubImage(141, 64, 41, 64);
		tempImages[3] = loader.getSubImage(205, 64, 41, 64);
		sprites.add(new Animation(tempImages));
		sprites.get(FACING_LEFT).setDelay(80);

		// Moving right
		tempImages = new BufferedImage[4];
		tempImages[0] = loader.getSubImage(12, 128, 43, 64);
		tempImages[1] = loader.getSubImage(77, 128, 43, 64);
		tempImages[2] = loader.getSubImage(141, 128, 43, 64);
		tempImages[3] = loader.getSubImage(205, 128, 43, 64);
		sprites.add(new Animation(tempImages));
		sprites.get(RUNNING_RIGHT).setDelay(80);

		// Moving left
		tempImages = new BufferedImage[4];
		tempImages[0] = loader.getSubImage(7, 192, 43, 64);
		tempImages[1] = loader.getSubImage(74, 192, 43, 64);
		tempImages[2] = loader.getSubImage(136, 192, 43, 64);
		tempImages[3] = loader.getSubImage(200, 192, 43, 64);
		sprites.add(new Animation(tempImages));
		sprites.get(RUNNING_LEFT).setDelay(80);

		// Jumping right
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(68, 379, 40, 73);
		sprites.add(new Animation(tempImages));
		sprites.get(JUMPING_RIGHT).setDelay(80);

		// Jumping left
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(337, 512, 40, 73);
		sprites.add(new Animation(tempImages));
		sprites.get(JUMPING_LEFT).setDelay(80);

		// Falling Right
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(329, 370, 40, 84);
		sprites.add(new Animation(tempImages));
		sprites.get(FALLING_RIGHT).setDelay(80);

		// Falling left
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(76, 503, 40, 84);
		sprites.add(new Animation(tempImages));
		sprites.get(FALLING_LEFT).setDelay(80);

		// Shooting right
		tempImages = new BufferedImage[2];
		tempImages[0] = loader.getSubImage(366, 172, 80, 64);
		tempImages[1] = loader.getSubImage(457, 171, 80, 64);
		sprites.add(new Animation(tempImages));
		sprites.get(SHOOTING_RIGHT).setDelay(80);

		// Shooting left
		tempImages = new BufferedImage[2];
		tempImages[0] = loader.getSubImage(353, 251, 80, 64);
		tempImages[1] = loader.getSubImage(262, 250, 80, 64);
		sprites.add(new Animation(tempImages));
		sprites.get(SHOOTING_LEFT).setDelay(80);

		// Shooting right 45
		tempImages = new BufferedImage[2];
		tempImages[0] = loader.getSubImage(276, 630, 77, 65);
		tempImages[1] = loader.getSubImage(380, 631, 77, 65);
		sprites.add(new Animation(tempImages));
		sprites.get(SHOOTING_RIGHT_45).setDelay(80);

		// Shooting left 45
		tempImages = new BufferedImage[2];
		tempImages[0] = loader.getSubImage(518, 733, 77, 65);
		tempImages[1] = loader.getSubImage(414, 734, 77, 65);
		sprites.add(new Animation(tempImages));
		sprites.get(SHOOTING_LEFT_45).setDelay(80);

		// Shooting right up
		tempImages = new BufferedImage[2];
		tempImages[0] = loader.getSubImage(474, 610, 47, 86);
		tempImages[1] = loader.getSubImage(548, 611, 47, 86);
		sprites.add(new Animation(tempImages));
		sprites.get(SHOOTING_RIGHT_UP).setDelay(80);

		// Shooting left up
		tempImages = new BufferedImage[2];
		tempImages[0] = loader.getSubImage(350, 713, 47, 86);
		tempImages[1] = loader.getSubImage(276, 714, 47, 86);
		sprites.add(new Animation(tempImages));
		sprites.get(SHOOTING_LEFT_UP).setDelay(80);

		// Hitting right
		tempImages = new BufferedImage[3];
		tempImages[0] = loader.getSubImage(22, 608, 56, 64);
		tempImages[1] = loader.getSubImage(95, 608, 56, 64);
		tempImages[2] = loader.getSubImage(170, 608, 56, 64);
		sprites.add(new Animation(tempImages));
		sprites.get(HITTING_RIGHT).setDelay(100);

		// Hitting left
		tempImages = new BufferedImage[3];
		tempImages[0] = loader.getSubImage(8, 691, 56, 64);
		tempImages[1] = loader.getSubImage(88, 691, 56, 64);
		tempImages[2] = loader.getSubImage(163, 691, 56, 64);
		sprites.add(new Animation(tempImages));
		sprites.get(HITTING_LEFT).setDelay(100);

		// Duck right
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(92, 823, 36, 36);
		sprites.add(new Animation(tempImages));
		sprites.get(DUCK_RIGHT).setDelay(100);

		// Duck left
		tempImages = new BufferedImage[1];
		tempImages[0] = loader.getSubImage(147, 823, 36, 36);
		sprites.add(new Animation(tempImages));
		sprites.get(DUCK_LEFT).setDelay(100);

	}

	public Rectangle getBounds() {
		Rectangle tempRect;
		if (duck) {
			tempRect = new Rectangle((int) ((x + ((width / 100) * 15) - xMap) + 6),
					(int) ((y) - yMap + (height / 2)) + 20, (int) (((width / 100) * 70)) - 12,
					(int) ((height / 2) - 17));
		} else {
			tempRect = new Rectangle((int) ((x + ((width / 100) * 15) - xMap) + 6), (int) ((y) - yMap + (height / 2)),
					(int) (((width / 100) * 70)) - 12, (int) ((height / 2) + 3));
		}
		return tempRect;
	}

	public Rectangle getBoundsRight() {
		Rectangle tempRect;
		if (duck) {
			tempRect = new Rectangle((int) ((x + (width / 100) * 85) - xMap) - 6,
					(int) (y + ((height / 100) * 6) - yMap + 28), (int) ((width / 100) * 15),
					(int) ((height / 100) * 88) - 31);
		} else {
			tempRect = new Rectangle((int) ((x + (width / 100) * 85) - xMap) - 6,
					(int) (y + ((height / 100) * 6) - yMap), (int) ((width / 100) * 15),
					(int) ((height / 100) * 88) - 5);
		}
		return tempRect;

	}

	public Rectangle getBoundsLeft() {
		Rectangle tempRect;
		if (duck) {
			tempRect = new Rectangle((int) (x - xMap) + 6, (int) (y + ((height / 100) * 6) - yMap + 28),
					(int) ((width / 100) * 15) - 1, (int) ((height / 100) * 88) - 31);
		} else {
			tempRect = new Rectangle((int) (x - xMap) + 6, (int) (y + ((height / 100) * 6) - yMap),
					(int) ((width / 100) * 15) - 1, (int) ((height / 100) * 88) - 5);
		}
		return tempRect;

	}

	public Rectangle getFullBounds() {
		Rectangle tempRect;
		if (duck) {
			tempRect = new Rectangle((int) (x - xMap) + 10, (int) (y - yMap) + 33, (int) (width) - 20,
					(int) (height) - 33);
		} else {
			tempRect = super.getFullBounds();
		}
		return tempRect;
	}

	public Rectangle getBoundsTop() {
		Rectangle tempRect;
		if (duck) {
			tempRect = new Rectangle((int) ((x + (width / 100) * 15) - xMap) + 6, (int) ((y) - yMap) + 31,
					(int) (((width / 100) * 70)) - 12, (int) ((height / 2)) - 17);
		} else
			tempRect = super.getBoundsTop();
		return tempRect;
	}

	public float getHealth() {
		return health;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public int getNumOfBullets() {
		return numOfBullets;
	}

	public float hit(float x) {
		health -= x;
		return health;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public float getJumpVel() {
		return jumpVel;
	}

	public void setJumpVel(float jumpVel) {
		this.jumpVel = jumpVel;
	}

	public boolean isHittedOnce() {
		return hittedOnce;
	}

	public void setHittedOnce(boolean hittedOnce) {
		this.hittedOnce = hittedOnce;
	}

	public boolean isHitting() {
		return hitting;
	}

	public void setHitting(boolean hitting) {
		this.hitting = hitting;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

	public int getHittingPower() {
		return hittingPower;
	}

	public void setHittingPower(int hittingPower) {
		this.hittingPower = hittingPower;
	}

	public boolean isDying() {
		return dying;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}

	public void addBullets(int x) {
		numOfBullets += x;
	}
	public void addHealth(int x) {
		health += x;
		if (health > maxHealth) {
			health = maxHealth;
		}
	}

}
