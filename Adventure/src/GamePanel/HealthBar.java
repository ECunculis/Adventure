package GamePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import GameObjects.Player;
import Tools.BufferedImageLoader;

public class HealthBar {
	
	private BufferedImageLoader loader;
	private BufferedImage heart;
	private String path;
	private Player player;
	//Position on the screen
	private int x, y;
	//Size
	private int heartWidth, heartHeight;
	private int barWidth, barHeight;
	
	public HealthBar(Player player) {
		this.player = player;
		x = 10;
		y = 10;
		
		heartHeight = 30;
		heartWidth = 36;
		barHeight = 20;
		barWidth = 150;
		
		path = "/HealthBar.png";
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		heart = loader.getSubImage(4, 5, 15, 12);
	}
	
	public void render(Graphics g) {
		g.drawImage(heart, x, y, heartWidth, heartHeight, null);
		g.setColor(Color.BLACK);
		g.drawRect(x + heartWidth + 5, y + 3, barWidth, barHeight);
		g.setColor(Color.GREEN);
		g.fillRect(x + heartWidth + 6, y + 4,(int) ((barWidth * player.getHealth()) / player.getMaxHealth()) - 1, barHeight - 1);
	}
}
