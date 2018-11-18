package GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Tools.KeyInput;
import game.Game;

public class LevelFinish extends GameState {
	
	private Font font1, font2;
	private GameStateManager gameStateManager;
	private GameStateNames gameState;
	private GameState currentState;
	
	public LevelFinish(GameStateManager gameStateManager, GameStateNames gameState, GameState currentState) {
		this.gameStateManager = gameStateManager;
		this.gameState = gameState;
		this.currentState = currentState;
		init();
	}

	public void init() {
		font1 = new Font("Times New Roman", 0, 50);
		font2 = new Font("Times New Roman", 0, 30);
	}

	public void tick() {
		currentState.tick();
		if (KeyInput.isENTER()) {
			switch (gameState) {
				case LEVEL1_STATE:
					gameStateManager.setCurrentState(GameStateNames.LEVEL2_STATE);
					break;
					
			}
		}
		
	}

	public void render(Graphics g) {
		currentState.render(g);
		g.setColor(Color.GREEN);
		g.setFont(font1);
		g.drawString("Congratulations!", (Game.getWidthOfCanvas() - g.getFontMetrics().stringWidth("Congratulations!")) /  2, Game.getHeightOfCanvas() / 2);
		g.setFont(font2);
		g.drawString("<press enter to continue>", (Game.getWidthOfCanvas() - g.getFontMetrics().stringWidth("<press enter to continue>")) /  2, Game.getHeightOfCanvas() / 2 + 30);
		
	}
	

}
