package GamePanel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game.Game;

public class PopText {

	private int x, y; // coordinates
	private String text;
	private Font font;
	private long beforeFadeDuration;
	private long lastTime;
	private boolean shouldRemove;
	private float fadeSpeed, opacity;

	public PopText(String text, long beforeFadeDuration, float fadeSpeed) { 
		this.text = text;
		this.beforeFadeDuration = beforeFadeDuration;
		this.fadeSpeed = fadeSpeed;
		init();
	}

	public void init() {
		shouldRemove = false;
		font = new Font("Javanese Text", 0, 60);
		lastTime = System.currentTimeMillis();
		opacity = 1.f;
		y = 200;
	}

	public void tick() {
		if (System.currentTimeMillis() - lastTime > beforeFadeDuration) {
			opacity -= fadeSpeed;
			if (opacity <= 0) {
				shouldRemove = true;
				opacity = 0;
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new  Color(29, 12, 91));
		g2d.setFont(font);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		x = (Game.getWidthOfCanvas() - g.getFontMetrics().stringWidth(text)) / 2;
		g2d.drawString(text, x, y);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	public boolean isShouldRemove() {
		return shouldRemove;
	}

}
