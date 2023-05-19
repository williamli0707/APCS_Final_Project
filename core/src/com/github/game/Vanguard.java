package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.github.SinglePlayerGame;

public class Vanguard extends Troop {

	static {
		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}

	private static final float HEALTH = 1000.0f, DAMAGE = 10.0f, SPEED = 0.5f, RANGE = 15.0f, COST = 400;
	public Vanguard(SinglePlayerGame game, float x, float y, float z, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, new Vector3(x,y,z), p);
		instance = new ModelInstance(model, x, y, z);
	}

	public Vanguard(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
		instance = new ModelInstance(model, v.x, v.y, v.z);
	}

	public ModelInstance getInstance() {
		return instance;
	}
}
