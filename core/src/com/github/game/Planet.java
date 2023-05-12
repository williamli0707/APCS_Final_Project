package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Game;

public class Planet implements Actor {

    static Model model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("planet.g3dj"));
    private Game game;
    private float x, y, z;
    ModelInstance instance;

    public Planet(Game game, float x, float y, float z) {
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

    @Override
    public Location getLocation() {
        return null;
    }

    public Player getPlayer(){return null;}
    public void act(float delta) {

    }
}
