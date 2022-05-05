package GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.w3c.dom.css.ElementCSSInlineStyle;

//import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;

import GameObjects.Player;

public class GamePanel {
	private HealthBar healthBar;
	private Score score;
	private Player player;
	private Font numOfBulletsFont;
	private int xBullets, yBullets;
	private PopText popText;

	public GamePanel(Player player) {
		this.player = player;
		init();
	}

	public void init() {
		healthBar = new HealthBar(player);
		score = new Score();
		numOfBulletsFont = new Font("TimesRoman", 0, 30);
		xBullets = 40;
		yBullets = 60;
	}

	public void tick() {
		if (popText != null) {
			popText.tick();
			if (popText.isShouldRemove()) {
				popText = null;
			} 
		} 
	}

	public void render(Graphics g) {
		healthBar.render(g);
		score.render(g);
		if (popText != null) {
			popText.render(g);
		}
		g.setColor(Color.BLACK);
		g.setFont(numOfBulletsFont);
		g.drawString(Integer.toString(player.getNumOfBullets()), xBullets, yBullets);
	}
	
	public void popTextInit(String text, long beforeFadeDuration, float fadeSpeed) {
		popText = new PopText(text, beforeFadeDuration, fadeSpeed);
	}

	public PopText getPopText() {
		return popText;
	}
	
	
}
