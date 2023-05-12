package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Game;

public class Vanguard extends Troop {

	static {
		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}

	private static final float HEALTH = 1000.0f, DAMAGE = 10.0f, SPEED = 0.5f, RANGE = 15.0f;

	public Vanguard(Game game, float x, float y, float z) {
		super(HEALTH, DAMAGE, SPEED, RANGE, game, x, y, z);
		instance = new ModelInstance(model, x, y, z);
	}

	public ModelInstance getInstance() {
		return instance;
	}
}
