package com.github.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public interface Actor {
    Vector3 location = null;
	ModelInstance modelInstance = null;
    public ModelInstance getInstance();
    public Vector3 getLocation();
    public Player getPlayer();
    public void act(float delta);
}
