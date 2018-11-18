package GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.sun.glass.ui.TouchInputSupport;

import game.Game;

public class Score {
	
	private Font font;
	private static int score;
	
	public Score() {
		init();
	}
	
	public void init() {
		score = 0;
		font = new Font("TimesRoman", 0, 30);
	}
	
	public void render(Graphics g) {
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString("Score: " + score, Game.getWidthOfCanvas() - g.getFontMetrics().stringWidth("Score: " + score) - 50, 50);

	}

	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		Score.score = score;
	}
	
	public static void addScore(int x) {
		Score.score = score + x;
	}
}
