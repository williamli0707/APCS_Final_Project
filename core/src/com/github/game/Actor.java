package com.github.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * interface with all the necessary methods for an Actor class
 * 
 * @author Leo Jiang, Johnathan Kao, William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources: None
 */
public interface Actor {
    /**
     * location of the actor
     */
    Vector3 location = null;
    /**
     * asset of the actor
     */
	ModelInstance modelInstance = null;
    /**
     * method to get asset
     * @return ModelInstance the asset
     */
    public ModelInstance getInstance();
    /**
     * method to get location
     * @return Vector3 the location
     */
    public Vector3 getLocation();
    /**
     * method to get the player
     * @return Player the player 
     */
    public Player getPlayer();
//    public void act(float delta);
}
