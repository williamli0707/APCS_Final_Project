package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Vanguard extends Troop {
	public static final float HEALTH = 1000.0f, DAMAGE = 10.0f, SPEED = 3.5f, RANGE = 15.0f, COST = 400;

	public Vanguard(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(assetVanguard.scene);
		scene.modelInstance.transform.trn(v);
		game.screen.sceneManager.addScene(scene);
	}

	public static void init() {
		assetVanguard = new GLBLoader().load(Gdx.files.internal("gltfTest/vanguard2/vanguard.glb"));
	}
}
