package GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import com.sun.org.apache.bcel.internal.generic.IfInstruction;
import com.sun.xml.internal.bind.v2.model.core.ID;

import GameObjects.GameObject;
import GameObjects.Player;
import GameObjects.BonusObject.BonusBullet;
import GameObjects.BonusObject.Coin;
import GameObjects.BonusObject.Key;
import GameObjects.Bullet.Bullet;
import GameObjects.Bullet.FlyingGunBullet;
import GameObjects.Bullet.MainPlayerBullet;
import GameObjects.Bullet.TurretBullet;
import GameObjects.Enemy.Blob;
import GameObjects.Enemy.Enemy;
import GameObjects.Enemy.FlyingGun;
import GameObjects.Enemy.Turret;
import GameObjects.EnemyObjects.MovingPlatformWithThorns;
import GameObjects.EnemyObjects.Thorn;
import GameObjects.Hints.TutorialHints;
import GamePanel.GamePanel;
import GamePanel.HealthBar;
import TileMap.TileMap;
import Tools.Background;
import Tools.KeyInput;
import game.Game;
import sun.net.www.content.image.x_xbitmap;
import Tools.Camera;

public class Level1 extends GameState {

	private TileMap map;
	private Camera camera;
	private Player player;
	private LinkedList<Enemy> enemies;
	private LinkedList<GameObject> objects;
	private Background background;
	private GamePanel gamePanel;
	private GameStateManager gameStateManager;
	private ArrayList<Bullet> allBullets;
	private GameStateNames gameState;
	private TutorialHints tutorialHints;

	// Enemy spawn
	private long lastTimeBlobSpawn;
	private boolean turrenSpawn = false, flyingGunSpawn = false;

	public Level1(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		init();
	}

	public void init() {
		camera = new Camera();
		map = new TileMap(40, camera); // Create TileMap where 32 is size of the tiles
		map.loadTiles("/Tileset.png", 8, 8); // Load tiles
		map.loadLevel("/Level1Map.png"); // Load the map from template
		background = new Background(0, 150, "/Clouds.png", camera, 5, 0.1f);
		lastTimeBlobSpawn = System.nanoTime();
		allBullets = new ArrayList<>(); // initialize the list of all bullets
		player = new Player(200, 1100, map, camera, allBullets);
		addEnemies();
		camera.move((int) (player.getX() - Game.getWidthOfCanvas() / 2 + (player.getWidth() / 2)),
				(int) (player.getY() - (Game.getHeightOfCanvas() / 2) + (player.getHeight() / 2) - 150));
		camera.tick();
		addDifferentObjects();
		gamePanel = new GamePanel(player);
		gameState = GameStateNames.LEVEL1_STATE;

		gamePanel.popTextInit("TUTORIAL", 2000, 0.01f);
		tutorialHints = new TutorialHints(camera);
	}

	public void tick() {
		if (gameStateManager.getCurrentStateName() == GameStateNames.LEVEL1_STATE) {
			player.tick();
		}
		gamePanel.tick();
		// Set x offset
		if (player.getX() - camera.getxOffSet() + player.getWidth() > (Game.getWidthOfCanvas() * 6) / 10) {
			camera.setxOffSet((int) (player.getX() - ((Game.getWidthOfCanvas() * 6) / 10) + player.getWidth()));
		} else if (player.getX() - camera.getxOffSet() < (Game.getWidthOfCanvas() * 4) / 10) {
			camera.setxOffSet((int) (player.getX() - ((Game.getWidthOfCanvas() * 4) / 10)));
		}
		camera.setyOffSet((int) (player.getY() - (Game.getHeightOfCanvas() / 2)) - 50);
		camera.tick();
		// Reset if player is dead
		if (player.isDying() && gameStateManager.getCurrentStateName() != GameStateNames.GAMEOVER_STATE) {
			gameStateManager.setCurrentState(GameStateNames.GAMEOVER_STATE);
		}
		background.tick();

		// Tick all enemies
		tickEnemies();
		// Tick all objects
		tickObjects();

		// Tick all bullets
		tickAllBullets();

		if (KeyInput.isESC())
			System.exit(0);
	}

	public void render(Graphics g) {
		g.setColor(new Color(183, 255, 252));
		g.fillRect(0, 0, Game.getWidthOfCanvas(), Game.getWidthOfCanvas());
		background.render(g);
		map.drawMap(g);
		tutorialHints.render(g);
		player.render(g);
		g.setFont(new Font("Time Roman", 0, 15));
		for (int i = 0; i < enemies.size(); i++)
			enemies.get(i).render(g);
		for (int i = 0; i < objects.size(); i++)
			objects.get(i).render(g);
		gamePanel.render(g);

		// Render all bullets
		for (int i = 0; i < allBullets.size(); i++) {
			allBullets.get(i).render(g);
		}

	}

	public void addEnemies() {
		enemies = new LinkedList<>();
		enemies.add(new Blob(40 * 63, 1435, 40 * 63, 40 * 77, true, camera, player));
		enemies.add(new Blob(40 * 89, 1435, 40 * 89, 40 * 101, true, camera, player));
		// enemies.add(new Blob(1500, 1384, 1000, 1600, true, camera, player));
		// enemies.add(new Blob(1900, 1384, 1000, 1600, false, camera, player));
		// enemies.add(new Turret(1000, 1400, false, map, camera, player, 2000,
		// allBullets));
		// enemies.add(new Turret(1200, 1400, true, map, camera, player, 800,
		// allBullets));
		// enemies.add(new FlyingGun(500, 1200, 500, 1000, true, map, camera, player,
		// 1400, allBullets));
	}

	public void addDifferentObjects() {
		objects = new LinkedList<>();
		// for (int i = 0; i <= 15; i++)
		// objects.add(new Thorn(800 + i * 12, 1418, camera, player));

		for (int i = 0; i <= 19; i++)
			objects.add(new Thorn(680 + i * 12, 1535, camera, player));
		for (int i = 0; i <= 32; i++)
			objects.add(new Thorn(1482 + i * 12, 1535, camera, player));
		// objects.add(new MovingPlatformWithThorns(2000, 1200, camera, 200, 200, 6, 2,
		// 200, 200, player));
		// objects.add(new Key(2500, 1400, camera, player, gameStateManager));

		objects.add(new BonusBullet(4365, 1410, camera, player, 5));
		objects.add(new BonusBullet(5350, 1410, camera, player, 5));
		objects.add(new BonusBullet(5400, 1410, camera, player, 5));
		objects.add(new BonusBullet(5450, 1410, camera, player, 5));
		objects.add(new BonusBullet(5500, 1410, camera, player, 5));

		addCoins();
	}

	public void addCoins() {
		for (int i = 0; i <= 6; i++) {
			objects.add(new Coin(450 + i * 35, 1400, camera, player));
		}
		for (int i = 0; i <= 2; i++) {
			objects.add(new Coin(755 + i * 35, 1250, camera, player));
		}
	}

	public void tickEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy tempObject = enemies.get(i);
			tempObject.tick();
			if (tempObject.isShouldRemove())
				enemies.remove(i);
		}

		if (player.getX() > 4600 && !turrenSpawn) {
			turrenSpawn = true;
			enemies.add(new Turret(5100, 1400, false, map, camera, player, 1000, allBullets));
		}

		if (player.getX() > 5400 && !flyingGunSpawn) {
			flyingGunSpawn = true;
			enemies.add(new FlyingGun(6000, 1100, 5500, 6000, false, map, camera, player, 2000, allBullets));
		} else if (enemies.size() == 0 && flyingGunSpawn && gameStateManager.getCurrentStateName() != GameStateNames.LEVEL_FINISH_STATE) {
			gameStateManager.setCurrentState(GameStateNames.LEVEL_FINISH_STATE);
		}

	}

	public void tickObjects() {
		for (int i = 0; i < objects.size(); i++) {
			GameObject tempObject = objects.get(i);
			tempObject.tick();
			if (tempObject.isShouldRemove()) {
				objects.remove(i);
			}
		}
	}

	public void tickAllBullets() {
		for (int i = 0; i < allBullets.size(); i++) {
			allBullets.get(i).tick();
			if (allBullets.get(i) instanceof MainPlayerBullet) {
				MainPlayerBullet tempMainPlayerBullet = (MainPlayerBullet) allBullets.get(i);
				for (int j = 0; j < enemies.size(); j++) {
					Enemy tempEnemy = (Enemy) enemies.get(j);
					if (!tempMainPlayerBullet.isExploding()) {
						if (tempEnemy.getFullBounds().intersects(tempMainPlayerBullet.getFullBounds())) {
							if (!tempEnemy.isDying()) {
								tempMainPlayerBullet.setExploding(true);
								tempMainPlayerBullet.setVelX(0);
								tempMainPlayerBullet.setVelY(0);
							}
							tempEnemy.hit(tempMainPlayerBullet.getDamage());
							tempEnemy.setHitted(true);
							tempEnemy.setLastTimeHitted(System.currentTimeMillis());
						}
					}
				}
			} else {
				Bullet tempBullet = allBullets.get(i);
				if (!tempBullet.isExploding()) {
					if (player.getFullBounds().intersects(allBullets.get(i).getFullBounds()) && !player.isHitted()) {
						player.hit(tempBullet.getDamage());
						player.setHitted(true);
						player.setLastTimeHitted(System.currentTimeMillis());
						tempBullet.setExploding(true);
						tempBullet.setVelX(0);
						tempBullet.setVelY(0);
					}
				}
			}
			if (allBullets.get(i).isShouldRemove()) {
				allBullets.remove(i);
			}
		}
	}

}
