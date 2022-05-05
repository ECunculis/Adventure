package GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

//import com.sun.corba.se.spi.oa.ObjectAdapter;
//import com.sun.org.apache.bcel.internal.generic.IfInstruction;
//import com.sun.org.apache.xpath.internal.operations.Mod;
//import com.sun.xml.internal.bind.v2.model.core.ID;

import GameObjects.GameObject;
import GameObjects.Player;
import GameObjects.BonusObject.BonusBullet;
import GameObjects.BonusObject.BreakableTile;
import GameObjects.BonusObject.Coin;
import GameObjects.BonusObject.Health;
import GameObjects.BonusObject.Key;
import GameObjects.BonusObject.Peach;
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
import Tools.Camera;

public class Level2 extends GameState {

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

	// Pop text spawn
	private boolean poptextSpawn = false;

	public Level2(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		init();
	}

	public void init() {
		camera = new Camera();
		camera.setMinX(520);
		map = new TileMap(40, camera); // Create TileMap where 32 is size of the tiles
		map.loadTiles("/Tileset.png", 8, 8); // Load tiles
		map.loadLevel("/Level2Map.png"); // Load the map from template
		background = new Background(0, 150, "/Clouds.png", camera, 5, 0.1f);
		allBullets = new ArrayList<>(); // initialize the list of all bullets
		
		player = new Player(16 * 40, 32 * 40, map, camera, allBullets);
		//player = new Player(121 * 40, 32 * 40, map, camera, allBullets);
		
		
		addEnemies();
		camera.move((int) (player.getX() - Game.getWidthOfCanvas() / 2 + (player.getWidth() / 2)),
				(int) (player.getY() - (Game.getHeightOfCanvas() / 2) + (player.getHeight() / 2) - 150));
		camera.tick();
		addDifferentObjects();
		gamePanel = new GamePanel(player);
		gameState = GameStateNames.LEVEL2_STATE;

		gamePanel.popTextInit("FIRST LEVEL", 2000, 0.01f);
	}

	public void tick() {
		
		if (gameStateManager.getCurrentStateName() == GameStateNames.LEVEL2_STATE) {
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
		
		if (!poptextSpawn) {
			if (gamePanel.getPopText() == null) {
				poptextSpawn = true;
				gamePanel.popTextInit("Find the key!", 2000, 0.01f);
			}
		}

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
		
		g.setFont(new Font("Time Roman", 0, 15));
		g.setColor(Color.red);
		//g.drawString((int) player.getX() + "  " + (int) player.getY(), 300, 50);
		
		for (int i = 0; i < enemies.size(); i++)
			enemies.get(i).render(g);
		for (int i = 0; i < objects.size(); i++)
			objects.get(i).render(g);
		gamePanel.render(g);
		player.render(g);

		// Render all bullets
		for (int i = 0; i < allBullets.size(); i++) {
			allBullets.get(i).render(g);
		}

	}

	public void addEnemies() {
		enemies = new LinkedList<>();

		enemies.add(new Turret(1788, 1240, false, map, camera, player, 1500, allBullets));
		enemies.add(new Blob(1840, 1230, 1840, 2200, true, camera, player));
		enemies.add(new Blob(2950, 1395, 2950, 3400, true, camera, player));
		enemies.add(new Turret(93 * 40, 20 * 40, false, map, camera, player, 2000, allBullets));
		enemies.add(new Turret(119 * 40, 23 * 40 + 8, true, map, camera, player, 1200, allBullets));
		enemies.add(new Turret(139 * 40, 27 * 40 + 8, false, map, camera, player, 1200, allBullets));
		enemies.add(new Blob(126 * 40, 32 * 40, 126 * 40, 139 * 40, true, camera, player));
		enemies.add(new FlyingGun(145 * 40, 28 * 40, 145 * 40, 157 * 40, false, map, camera, player, 1000, allBullets));


	}

	public void addDifferentObjects() {
		objects = new LinkedList<>();
		
		objects.add(new Key(161 * 40, 36 * 40, camera, player, gameStateManager));

		
		objects.add(new MovingPlatformWithThorns(1300, 1200, camera, 200, 200, 5, 2, 200, 100, player));
		objects.add(new MovingPlatformWithThorns(3400, 13 * 40 - 20, camera, 300, 300, 5, 2, 200, 100, player));
		
		
		for (int i = 0; i <= 53; i++) {
		objects.add(new Thorn(2280 + i * 12, 1415, camera, player));
		}
		for (int i = 0; i <= 10; i++) {
			objects.add(new Thorn(97 * 40 + i * 12, 20 * 40 + 15, camera, player));
		}
		
		objects.add(new MovingPlatformWithThorns(104 * 40, 13 * 40 - 20, camera, 295, 300, 8, 3, 200, 100, player));
		objects.add(new MovingPlatformWithThorns(115 * 40, 13 * 40 - 20, camera, 340, 300, 8, 3, 200, 100, player));

		objects.add(new Health(88* 40 + 14, 30 * 40 + 14, camera, player, 100));
		
		objects.add(new BreakableTile(13 * 40, 27 * 40, camera, player, map));
		objects.add(new Peach(3 * 40, 28 * 40, camera, player, 1000));
		
		addBullets();
		addCoins();
	}

	public void addCoins() {
		objects.add(new Coin(15 * 40 + 12, 28 * 40, camera, player));
		objects.add(new Coin(16 * 40 + 12, 28 * 40, camera, player));
		objects.add(new Coin(17 * 40 + 12, 28 * 40, camera, player));
		
		
		
		objects.add(new Coin(60 * 40 + 12, 24 * 40, camera, player));
		objects.add(new Coin(66 * 40 + 12, 27 * 40, camera, player));
		objects.add(new Coin(60 * 40 + 12, 31 * 40, camera, player));
		objects.add(new Coin(64 * 40 + 12, 31 * 40, camera, player));
		objects.add(new Coin(68 * 40 + 12, 31 * 40, camera, player));
		objects.add(new Coin(72 * 40 + 12, 31 * 40, camera, player));
	}
	
	public void addBullets() {
		objects.add(new BonusBullet(54* 40 + 14, 21 * 40 + 14, camera, player, 10));
		objects.add(new BonusBullet(130 * 40 + 14, 36 * 40 + 14, camera, player, 10));
		objects.add(new BonusBullet(131 * 40 + 14, 36 * 40 + 14, camera, player, 10));
		objects.add(new BonusBullet(132 * 40 + 14, 36 * 40 + 14, camera, player, 10));
	}

	public void tickEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy tempObject = enemies.get(i);
			tempObject.tick();
			if (tempObject.isShouldRemove())
				enemies.remove(i);
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