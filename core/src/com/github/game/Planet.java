package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Main;

public class Planet implements Actor {

    static Model model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("planet.g3dj"));
    private Main game;
    private float x, y, z;
    ModelInstance instance;

    public Planet(Main game, float x, float y, float z) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.z = z;
        instance = new ModelInstance(model, x, y, z);
    }

    @Override
    public ModelInstance getInstance() {
        return instance;
    }
}
