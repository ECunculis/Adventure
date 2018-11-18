package GameObjects.BonusObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import GameObjects.GameObject;
import GameObjects.Player;
import GamePanel.Score;
import Tools.Animation;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class Coin extends GameObject {

	private BufferedImageLoader loader;
	private String path;
	private ArrayList<Animation> sprites;
	private Player player;

	private static final int SPINNING = 0;
	private int currentAnimation;

	public Coin(float x, float y, Camera camera, Player player) {
		super(x, y, camera);
		this.player = player;
		init();
	}

	public void init() {
		path = "/Coin.png";
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		currentAnimation = SPINNING;
		width = 7;
		height = 8;
		cwidth = width;
		cheight = height;
		scale = 2.5f;
		spriteInit();
	}

	public void tick() {
		sprites.get(currentAnimation).runAnimation();

		if (player.getFullBounds().intersects(getFullBounds())) {
			if (!shouldRemove) {
				shouldRemove = true;
				Score.addScore(5);
			}
		}
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();

		sprites.get(currentAnimation).drawAnimation((int) (x - xMap), (int) (y - yMap), (int) (scale * width),
				(int) (scale * height), g);
	}

	public void spriteInit() {
		sprites = new ArrayList<>();
		BufferedImage[] tempImages;
		tempImages = new BufferedImage[4];
		tempImages[0] = loader.getSubImage(7, 8, 21, 21);
		tempImages[1] = loader.getSubImage(34, 8, 21, 21);
		tempImages[2] = loader.getSubImage(58, 8, 21, 21);
		tempImages[3] = loader.getSubImage(78, 8, 21, 21);
		sprites.add(new Animation(tempImages));
		sprites.get(SPINNING).setDelay(120);
	}

}
