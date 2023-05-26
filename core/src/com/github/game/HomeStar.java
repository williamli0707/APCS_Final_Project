package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.github.SinglePlayerGame;

public class HomeStar extends Star{
    static Model hostileModel = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("star-hostile.g3dj"));
    static Model friendlyModel = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("star-friendly.g3dj"));
    private SinglePlayerGame game;
    private float x, z, health;
    private Player player=null;
    ModelInstance instance;
    public HomeStar(SinglePlayerGame game, float x, float z, float hp) {
        super(game, x, z);
        health=hp;
    }
    public float getHealth(){
        return health;
    }
    public void setNewHealth(float newHealth) { health=newHealth;}
}
