package com.github.StarcraftButGood.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Interface with all the necessary methods for an Actor class
 * 
 * @author Leo Jiang, Johnathan Kao, William Li
 * @version 6/1/23
 * @author Period 5
 * @author Sources: None
 */
public interface Actor {
    /**
     * location of the actor
     */
    Vector3 location = null;
    /**
     * the ModelInstance of the actor - this is the 3D instance of the actor in the world
     */
	ModelInstance modelInstance = null;

    /**
     * method to get the instance
     * @return the model
     */
    public ModelInstance getInstance();
    /**
     * method to get location
     * @return the location
     */
    public Vector3 getLocation();
    /**
     * method to get the player
     * @return the player
     */
    public Player getPlayer();
}
