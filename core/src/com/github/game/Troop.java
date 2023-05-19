package com.github.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;

public abstract class Troop implements Actor {
    private float health, damage, speed, cost;
    private Player player;
    private double range;
    static Model model;
    ModelInstance instance;

    private Vector3 myLoc;
    private SinglePlayerGame game;

    public Troop(float health, float damage, float speed, double range, float cost, SinglePlayerGame game, Vector3 loc, Player p) {
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.cost = cost;
        this.game = game;
        myLoc = loc;
        player = p;
    }

    public float getCost() {
        return cost;
    }

    public Vector3 getLocation() {
        return myLoc;
    }
    public void act(float delta) {
        double leastDist = 2e9;
        Vector3 dest = myLoc;
        for(Actor a : game.getActors()){
            if (a.getPlayer()==player)
                continue;
            Vector3 tempLoc = a.getLocation();
            if(myLoc.dst(tempLoc)<leastDist){
                leastDist=myLoc.dst(tempLoc);
                dest = tempLoc;
            }
            if (tempLoc.dst(myLoc) <= range){
                if (a instanceof Troop){
                    Troop fighter = (Troop)a;
                    fighter.setHealth(fighter.getHealth() - damage);
                    setHealth(health - fighter.getDamage());
                    if (health <= 0)
                        death();
                    if (fighter.getHealth() <= 0)
                        fighter.death();
                }
            }
        }
        move(dest, delta);
    }

    private void move(Vector3 newLoc, float delta) {
        Vector3 move = newLoc.sub(myLoc).nor();
        instance.transform.trn(move.x * speed * delta, 0, move.z * speed * delta);
    }

    public float getHealth(){
        return health;
    }

    public void setHealth(float newHealth){health = newHealth;}

    public void death(){
        game.getActors().remove(this);
    }

    public float getDamage(){
        return damage;
    }

    public float getSpeed(){
        return speed;
    }

    public SinglePlayerGame getGame(){return game;}
    public Player getPlayer(){return player;}
    public double getRange(){
        return range;
    }
}
