package com.github.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

public interface Actor {
    Location location = null;
	ModelInstance modelInstance = null;
    public ModelInstance getInstance();
    public Location getLocation();
    public void act(float delta);
}
