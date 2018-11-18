package GameObjects.Hints;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.LongAdder;

import GameObjects.GameObject;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class TutorialHints {
	
	private Hint[] hints; 
	private BufferedImageLoader loader;
	private String path;
	private Camera camera;

	public TutorialHints(Camera camera) {
		this.camera = camera;
		init();
	}

	public void init() {
		hints = new Hint[8];
		loader = new BufferedImageLoader();
		path = "/Tutorial.png";
		loader.loadImage(path);
		addHints();
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		for (int i = 0; i < hints.length; i++) {
			hints[i].render(g);
		}
	}
	
	public void addHints() {
		hints[0] = new Hint(500, 1350, camera, loader);
		hints[0].loadSubimage(1, 1, 119, 18); // press W to jump
		hints[1] = new Hint(4700, 1350, camera, loader);
		hints[1].loadSubimage(1, 22, 146, 19); // press space to shoot
		hints[2] = new Hint(2399, 1365, camera, loader);
		hints[2].loadSubimage(2, 44, 111, 17); // press Ctrl to hit
		hints[3] = new Hint(200, 1350, camera, loader);
		hints[3].loadSubimage(2, 65, 111, 17); // press S to dug
		hints[4] = new Hint(1280, 1350, camera, loader);
		hints[4].loadSubimage(2, 84, 192, 19); // press W twice
		hints[5] = new Hint(3400, 1350, camera, loader);
		hints[5].loadSubimage(2, 105, 181, 16); // Jump on top of the enemy
		hints[6] = new Hint(4250, 1340, camera, loader);
		hints[6].loadSubimage(3, 123, 124, 15); // pick up the bullets
		hints[7] = new Hint(5500, 1340, camera, loader);
		hints[7].loadSubimage(1, 139, 245, 16); // move the gun
	}

}
