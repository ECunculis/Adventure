package Tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

//import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;
//
//import sun.util.logging.resources.logging_zh_TW;

public class Animation {

	private BufferedImage[] images; // Array of frames
	private BufferedImage currentImage; // Current frame
	private int numOfFrames;
	private int currentFrame;
	private int delay;
	private int counter;
	private long lastTime;
	long currentTime;

	private boolean hasPlayedOnce;

	public Animation(BufferedImage[] images) {
		this.images = images;
		numOfFrames = images.length;
		currentImage = images[0];
		currentFrame = 0;
		counter = 0;
		lastTime = 0;
	}

	// Increase counter till it reaches delay
	public void runAnimation() {
		hasPlayedOnce = false;
		currentTime = System.currentTimeMillis();
		if (lastTime == 0) {
			lastTime = currentTime;
		}
		counter += currentTime - lastTime;
		lastTime = currentTime;
		if (counter > delay) {
			counter = 0;
			nextFrame(); // Change current frame to next frame
		}
	}

	public void nextFrame() {
		currentFrame++;
		if (currentFrame == numOfFrames) {
			currentFrame = 0;
			hasPlayedOnce = true;
		}
		currentImage = images[currentFrame];
	}

	// Draw current frame on the screen
	public void drawAnimation(int x, int y, Graphics g) {
		g.drawImage(currentImage, x, y, null);
	}

	public void drawAnimation(int x, int y, int scaleX, int scaleY, Graphics g) {
		g.drawImage(currentImage, x, y, scaleX, scaleY, null);
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public boolean isHasPlayedOnce() {
		return hasPlayedOnce;
	}

	public void setHasPlayedOnce(boolean hasPlayedOnce) {
		this.hasPlayedOnce = hasPlayedOnce;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
	

}
