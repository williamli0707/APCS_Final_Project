package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.github.SinglePlayerGame;

public class Ranger extends Troop {

	static {
		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("gltfTest/Ranger.g3dj"));
//		SceneAsset sceneAsset = new GLBLoader().load(Gdx.files.internal("gltfTest/BlenderModel.glb"));;
	}

	private static final float HEALTH = 50.0f, DAMAGE = 35.0f, SPEED = 5.0f, RANGE = 3.0f, COST = 150;

	public Ranger(SinglePlayerGame game, float x, float y, float z, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, new Vector3(x,y,z), p);
		instance = new ModelInstance(model, x, y, z);
	}

	public Ranger(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
		instance = new ModelInstance(model, v.x, v.y, v.z);
	}

	public ModelInstance getInstance() {
		return instance;
	}
}
