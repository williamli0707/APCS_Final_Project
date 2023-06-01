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
 * Represents the Vanguard Troop in troops.
 * @author Leo Jiang, William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public class Vanguard extends Troop {
	/** decides the health, damage, speed, range, and cost of each Vanguard unit */
	public static float HEALTH = 125.0f, DAMAGE = 20.0f, SPEED = 4f, RANGE = 10.0f, COST = 400;
	/**
	 * Constructor for the Vanguard Troop.
	 * @param game the game
	 * @param v the location
	 * @param p the player that owns this unit
	 */
	public Vanguard(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(assetVanguard.scene);
		scene.modelInstance.transform.trn(v);
		sprite = new Sprite(p == null ? game.screen.v_hostile : game.screen.v_friendly);
		sprite.setBounds(0, 0, 0, 0);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

		if(p == game.getPlayer()) for(Material m: scene.modelInstance.materials) m.set(ColorAttribute.createEmissive(0.05f,  0.1f, 0.05f, 0.5f));
		else for(Material m: scene.modelInstance.materials) m.set(ColorAttribute.createEmissive(0.1f, 0.05f, 0.05f, 0.5f));
	}
	/**loads the asset model */
	public static void init() {
		assetVanguard = new GLBLoader().load(Gdx.files.internal("gltfTest/vanguard2/vanguard.glb"));
	}
}
