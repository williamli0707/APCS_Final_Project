package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Game;

public class Aegis extends Troop {
	static {
		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}

	private static final float HEALTH = 100.0f, DAMAGE = 50.0f, SPEED = 1.0f, RANGE = 5.0f;

	public Aegis(Game game, float x, float y, float z, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, game, new Vector3(x,y,z),p);
		instance = new ModelInstance(model, x, y, z);
	}

	public ModelInstance getInstance() {
		return instance;
	}
}
