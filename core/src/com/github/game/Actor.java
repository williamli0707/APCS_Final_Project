package com.github.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public interface Actor {
	ModelInstance modelInstance = null;
    public ModelInstance getInstance();
}
