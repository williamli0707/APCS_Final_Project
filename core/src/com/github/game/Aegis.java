package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

/**
 * represents the Aegis Troop in troops
 * @author Leo Jiang, William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public class Aegis extends Troop {
	/**
	 * decides the health, damage, speed, range, and cost of each aegis unit
	 */
	public static final float HEALTH = 100.0f, DAMAGE = 50.0f, SPEED = 2f, RANGE = 15.0f, COST = 1000;
	/**
	 * constructor for the class
	 * @param SinglePlayerGame game the game 
	 * @param Vector3 v the location
	 * @param Player p the player that owns this unit
	 */
	public Aegis(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(assetAegis.scene);
		scene.modelInstance.transform.trn(v);
		sprite = new Sprite(p == null ? game.screen.a_hostile : game.screen.a_friendly);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}

	/**
	 * loads the asset for the unit
	 */
	public static void init() {
		assetAegis = new GLBLoader().load(Gdx.files.internal("gltfTest/aegis/aegis.glb"));
	}
}
