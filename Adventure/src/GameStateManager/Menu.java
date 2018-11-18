package GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Tools.KeyInput;
import game.Game;

public class Menu extends GameState {

	private static final int PLAY = 0;
	private static final int LEVEL_CHOOSE = 1;
	private static final int EXIT = 2;

	private int select = 0;
	private Font font;
	private GameStateManager gameStateManager;
	private boolean keyIsReleased; // Need to check if key is not pressed
	// Otherwise the level will be chosen too fast

	public Menu(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		keyIsReleased = false;
		init();
	}

	public void init() {
		font = new Font("Times New Roman", 0, 40);
	}

	public void tick() {
		if (keyIsReleased) {
			if (KeyInput.isS()) {
				select++;
				KeyInput.setS(false);
			} else if (KeyInput.isW()) {
				select--;
				KeyInput.setW(false);
			} else if (KeyInput.isENTER()) {
				switch (select) {
				case PLAY:
					gameStateManager.setCurrentState(GameStateNames.LEVEL1_STATE);
					break;
				case LEVEL_CHOOSE:
					gameStateManager.setCurrentState(GameStateNames.LEVEL_CHOOSE_STATE);
					break;
				case EXIT:
					System.exit(0);
					break;
				}
			} else if (KeyInput.isESC()) System.exit(0);
		} else {
			if (!KeyInput.isKeyIsPressed()) {
				keyIsReleased = true;
			}
		}

		if (select > 2)
			select = 0;
		else if (select < 0)
			select = 2;
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Game.getWidthOfCanvas(), Game.getHeightOfCanvas());
		g.setFont(font);
		for (int i = 0; i <= 2; i++) {
			if (select == i) {
				g.setColor(Color.RED);
			} else
				g.setColor(Color.BLACK);
			switch (i) {
			case PLAY:
				g.drawString("Play", 350, 100);
				break;
			case LEVEL_CHOOSE:
				g.drawString("LEVELS", 350, 250);
				break;
			case EXIT:
				g.drawString("Exit", 350, 400);
				break;
			}
		}
	}

}
