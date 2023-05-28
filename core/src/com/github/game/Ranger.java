package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Ranger extends Troop {

	public static final float HEALTH = 50.0f, DAMAGE = 35.0f, SPEED = 5.0f, RANGE = 5.0f, COST = 150;

	public Ranger(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(assetRanger.scene);
		scene.modelInstance.transform.trn(v);
		game.screen.sceneManager.addScene(scene);
	}

	public static void init() {
		assetRanger = new GLBLoader().load(Gdx.files.internal("gltfTest/ranger/ranger.glb"));
	}
}
