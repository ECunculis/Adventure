package GameObjects.EnemyObjects;

import java.awt.Graphics;

import GameObjects.GameObject;
import TileMap.TileMap;
import Tools.Camera;

public abstract class EnemyObjects extends GameObject{
	
	protected int hitPower;

	public EnemyObjects(float x, float y, Camera camera) {
		super(x, y, camera);
	}

	public int getHitPower() {
		return hitPower;
	}
	
}
