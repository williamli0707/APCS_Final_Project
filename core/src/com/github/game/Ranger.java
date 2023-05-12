package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Game;

public class Ranger extends Troop {

	static {
		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}

	private static final float HEALTH = 50.0f, DAMAGE = 35.0f, SPEED = 5.0f, RANGE = 3.0f;

	public Ranger(Game game, float x, float y, float z) {
		super(HEALTH, DAMAGE, SPEED, RANGE, game, x, y, z);
		instance = new ModelInstance(model, x, y, z);
	}

	public ModelInstance getInstance() {
		return instance;
	}
}
