package com.github.game;

import com.github.SinglePlayerGame;

public class HomeStar extends Star{
    private float health;
    public HomeStar(SinglePlayerGame game, float x, float z, float hp) {
        super(game, x, z);
        health=hp;
    }

    public void act(float delta) {
        super.act(delta);
//        if(!game.getPlayer().getMothership().getLocation().equals(Vector3.Zero)) instance.transform.rotateTowardTarget(game.getPlayer().getMothership().getLocation(), Vector3.Y);
    }

    public float getHealth(){
        return health;
    }
    public void setNewHealth(float newHealth) { health=newHealth;}
}
