package GameStateManager;

import java.util.ArrayList;

public class GameStateManager {

	private GameStateNames currentStateName;
	private static GameState currentState;

	public GameStateManager() {
		currentState = new Menu(this);
	}

	public GameStateNames getCurrentStateName() {
		return currentStateName;
	}

	public void setCurrentState(GameStateNames state) {
		GameState tempState = currentState;
		switch (state) {
		case MENU_STATE:
			this.currentState = new Menu(this);
			break;
		case GAMEOVER_STATE:
			this.currentState = new GameOver(this, tempState);
			break;
		case LEVEL_CHOOSE_STATE:
			this.currentState = new LevelChoose(this);
			break;
		case LEVEL_FINISH_STATE:
			this.currentState = new LevelFinish(this, currentStateName, tempState);
			break;
		case LEVEL1_STATE:
			this.currentState = new Level1(this);
			break;
		case LEVEL2_STATE:
			this.currentState = new Level2(this);
			break;
	
		}
		currentStateName = state;
	}

	public static GameState getState() {
		return currentState;
	}
	

	
}
