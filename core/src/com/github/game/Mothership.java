package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Game;

public class Mothership extends Troop {
	static {
		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}

	private static final float health = 1f, damage = 0f, speed = 5f, range = 5f;
	public Mothership(Game game, float x, float y, float z, Player p) {
		super(health, damage, speed, range, game, new Location(x,y,z),p);
		instance = new ModelInstance(model, x, y, z);
	}

	public ModelInstance getInstance() {
		return instance;
	}
}
