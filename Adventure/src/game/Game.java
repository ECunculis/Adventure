package game;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

//import com.sun.java.swing.plaf.windows.resources.windows;

import GameStateManager.GameStateManager;
import Tools.KeyInput;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private Thread thread;
	private boolean running;
	private static final int WIDTH = 800, HEIGHT = 600;
	private GameStateManager gameStateManager;

	public void init() {
		requestFocus();
		addKeyListener(new KeyInput());
		gameStateManager = new GameStateManager();
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		gameStateManager.getState().tick();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		gameStateManager.getState().render(g);

		g.dispose();

		bs.show();
	}

	public void run() {
		init();
		double NsPerTick = 1000000000D / 60D; // Nanoseconds per tick
		double delta = 0; // How much ticks should be done

		long lastTimeInNano = System.nanoTime();
		long lastTimeInMili = System.currentTimeMillis();
		long currentTimeInNano = 0;
		long ticks = 0; // Number of ticks
		long frames = 0; // Number of frames

		boolean shouldRender = false;

		while (running) {
			requestFocus();
			currentTimeInNano = System.nanoTime();
			delta += (currentTimeInNano - lastTimeInNano) / NsPerTick;
			lastTimeInNano = currentTimeInNano;
			while (delta >= 1) {
				delta--;
				tick();
				ticks++;
			}
			
			render();
			frames++;

			if (System.currentTimeMillis() - lastTimeInMili >= 1000) {
				lastTimeInMili += 1000;
				Window.getFrame().setTitle("Ticks: " + ticks + " Frames: " + frames);
				frames = 0;
				ticks = 0;
			}
		}
	}

	public static void main(String args[]) {
		@SuppressWarnings("unused")
		Window window = new Window(WIDTH, HEIGHT, "Adventure", new Game());
	}

	public static int getWidthOfCanvas() {
		return WIDTH;
	}

	public static int getHeightOfCanvas() {
		return HEIGHT;
	}

}
