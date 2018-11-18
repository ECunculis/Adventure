package GameObjects.BonusObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.security.KeyStore.PrivateKeyEntry;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.org.apache.xml.internal.security.Init;

import GameObjects.GameObject;
import GameObjects.Player;
import GameStateManager.GameState;
import GameStateManager.GameStateManager;
import GameStateManager.GameStateNames;
import TileMap.TileMap;
import Tools.BufferedImageLoader;
import Tools.Camera;

public class Key extends GameObject {
	
	private BufferedImageLoader loader;
	private BufferedImage image;
	private String path;
	private Player player;
	private GameStateManager gameStateManager;

	public Key(float x, float y, Camera camera, Player player, GameStateManager gameStateManager) {
		super(x, y, camera);
		this.player = player;
		this.gameStateManager = gameStateManager;
		init();
	}
	
	public void init() {
		width = 30;
		height = 14;
		cwidth = width;
		cheight = height;
		path = "/Key.png";
		loader = new BufferedImageLoader();
		loader.loadImage(path);
		image = loader.getSubImage(1, 1, 22, 9);
	}

	public void tick() {
		if (player.getFullBounds().intersects(getFullBounds())) {
			shouldRemove = true;
			gameStateManager.setCurrentState(GameStateNames.LEVEL_FINISH_STATE);
		}
	}

	public void render(Graphics g) {
		xMap = camera.getxOffSet();
		yMap = camera.getyOffSet();
		
		g.drawImage(image, (int) (x - xMap), (int) (y - yMap), (int) width,(int) height, null);
	}
	
}
