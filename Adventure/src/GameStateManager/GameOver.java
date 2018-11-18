package GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Tools.KeyInput;
import game.Game;

public class GameOver extends GameState {
	
	private GameStateManager gameStateManager;
	private Font gameOverFont, pressEnterFont;
	private GameState currentState;
	
	public GameOver(GameStateManager gameStateManager, GameState currentState) {
		this.gameStateManager = gameStateManager;
		this.currentState = currentState;
		init();
	}

	public void init() {
		gameOverFont = new Font("Times New Roman", 0, 70);
		pressEnterFont = new Font("Times New Roman", 0, 30);
	}

	public void tick() {
		currentState.tick();
		if (KeyInput.isENTER()) {
			if (currentState instanceof Level1)
			gameStateManager.setCurrentState(GameStateNames.LEVEL1_STATE);
			if (currentState instanceof Level2)
				gameStateManager.setCurrentState(GameStateNames.LEVEL2_STATE);
		} else if (KeyInput.isESC()) {
			System.exit(1);
		}
	}

	public void render(Graphics g) {
		currentState.render(g);
		g.setColor(Color.RED);
		g.setFont(gameOverFont);
		g.drawString("GAME OVER!", (Game.getWidthOfCanvas() - g.getFontMetrics().stringWidth("GAME OVER!")) / 2, Game.getHeightOfCanvas() / 2);
		g.setFont(pressEnterFont);
		g.drawString("<press enter to play again>", (Game.getWidthOfCanvas() - g.getFontMetrics().stringWidth("<press enter to play again>")) / 2, Game.getHeightOfCanvas() / 2 + 50);
	}

}
