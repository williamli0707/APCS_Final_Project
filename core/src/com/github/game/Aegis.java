package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

/**
 * Represents the Aegis Troop in troops
 * @author Leo Jiang, William Li
 * @version 6/1/23
 * @author Period 5
 * @author Sources - None
 */
public class Aegis extends Troop {
	/** decides the health, damage, speed, range, and cost of each Aegis unit */
	public static float HEALTH = 225.0f, DAMAGE = 40.0f, SPEED = 3.5f, RANGE = 15.0f, COST = 1000;

	/**
	 * constructor for the Aegis troop
	 * @param game the game
	 * @param v the location
	 * @param p the player that owns this unit
	 */
	public Aegis(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(assetAegis.scene);
		scene.modelInstance.transform.trn(v);
		sprite = new Sprite(p == null ? game.screen.a_hostile : game.screen.a_friendly);
		sprite.setBounds(0, 0, 0, 0);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

		if(p == game.getPlayer()) for(Material m: scene.modelInstance.materials) m.set(ColorAttribute.createEmissive(0.3f, 0.5f, 0.3f, 0.5f));
		else for(Material m: scene.modelInstance.materials) m.set(ColorAttribute.createEmissive(0.5f, 0.3f, 0.3f, 0.5f));
	}

	/**
	 * loads the asset for the unit
	 */
	public static void init() {
		assetAegis = new GLBLoader().load(Gdx.files.internal("gltfTest/aegis/aegis.glb"));
	}
}
