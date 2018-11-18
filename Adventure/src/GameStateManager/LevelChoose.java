package GameStateManager;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import Tools.KeyInput;
import game.Game;

public class LevelChoose extends GameState {

	private int select;
	private int numOfLevels;
	private Font font;
	private GameStateManager gameStateManager;
	private boolean keyIsReleased; // Need to check if key is not pressed
	// Otherwise the level will be chosen too fast

	public LevelChoose(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		init();
	}

	public void init() {
		font = new Font("Times New Roman", 0, 40);
		select = 1;
		numOfLevels = 2;
	}

	public void tick() {
		if (keyIsReleased) {
			if (KeyInput.isD()) {
				select++;
				KeyInput.setD(false);
			} else if (KeyInput.isA()) {
				select--;
				KeyInput.setA(false);
			} else if (KeyInput.isENTER()) {
				switch (select) {
				case 1:
					gameStateManager.setCurrentState(GameStateNames.LEVEL1_STATE);
					break;
				case 2:
					gameStateManager.setCurrentState(GameStateNames.LEVEL2_STATE);
					break;
				}
			} else if (KeyInput.isESC()) gameStateManager.setCurrentState(GameStateNames.MENU_STATE);
		} else {
			if (!KeyInput.isKeyIsPressed()) {
				keyIsReleased = true;
			}
		}

		if (select > numOfLevels)
			select = 1;
		else if (select < 1)
			select = 2;
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Game.getWidthOfCanvas(), Game.getHeightOfCanvas());
		g.setFont(font);
		for (int i = 1; i <= numOfLevels; i++) {
			if (select == i) {
				g.setColor(Color.RED);
			} else
				g.setColor(Color.BLACK);
			switch (i) {
			case 1:
				g2d.setStroke(new BasicStroke(5));
				g2d.drawRect(50, 50, 50, 50);
				g.drawString("1", 65, 88);
				break;
			case 2:
				g2d.setStroke(new BasicStroke(5));
				g2d.drawRect(125, 50, 50, 50);
				g.drawString("2", 140, 88);
				break;

			}
		}
	}

}