package com.github.StarcraftButGood.game;

import com.github.StarcraftButGood.SinglePlayerGame;

/**
 * Represents the Home Star of the player.
 * @author Johnathan Kao, William Li
 * @version 6/1/23
 * @author Period 5
 * @author Sources - None
 */
public class HomeStar extends Star{
    private float health;

    /**
     * Constructor for the home star. Creates a HomeStar object with the given game, location, and health.
     * @param game the game instance
     * @param x the x coordinate of the location
     * @param z the z coordinate of the location
     * @param hp the health to start with
     */
    public HomeStar(SinglePlayerGame game, float x, float z, float hp) {
        super(game, x, z);
        health = hp;
    }

    /**
     * Acts every tick.
     * @param delta the time, in seconds, since the last tick
     */
    public void act(float delta) {
        super.act(delta);
//        if(!game.getPlayer().getMothership().getLocation().equals(Vector3.Zero)) instance.transform.rotateTowardTarget(game.getPlayer().getMothership().getLocation(), Vector3.Y);
    }

    /**
     * Returns the health.
     * @return health of this star
     */
    public float getHealth(){
        return health;
    }

    /**
     * Sets the health to the given value.
     * @param newHealth the value to set health to
     */
    public void setNewHealth(float newHealth) { health=newHealth;}
}
