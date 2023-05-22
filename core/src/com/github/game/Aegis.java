package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Aegis extends Troop {
	public static final float HEALTH = 100.0f, DAMAGE = 50.0f, SPEED = 1.0f, RANGE = 5.0f, COST = 1000;

	public Aegis(SinglePlayerGame game, float x, float y, float z, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, new Vector3(x,y,z),p);
//		instance = new ModelInstance(model, x, y, z)
		scene = new Scene(asset.scene);
		scene.modelInstance.transform.trn(x, y, z);
		game.screen.sceneManager.addScene(scene);
	}

	public Aegis(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(asset.scene);
		scene.modelInstance.transform.trn(v);
		game.screen.sceneManager.addScene(scene);
	}
	public Scene getScene() { return scene; }

	public ModelInstance getInstance() {
		return scene.modelInstance;
	}

	public static void init() {
		asset = new GLBLoader().load(Gdx.files.internal("gltfTest/aegis/aegis.glb"));
	}
}
