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

    /**
     * allows for other methods to access the cost of the troop
     * @return the cost of the troop
     */
    public float getCost() {
        return cost;
    }

    /**
     * is called to give the location of the troop
     * @return a Vector3 with the location
     */
    public Vector3 getLocation() {
        return myLoc;
    }
    /**
     * Called each render frame to determine how a troop acts
     */
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

    /**
     * moves the troop
     */
    private void move(Vector3 newLoc, float delta) {
        Vector3 move = newLoc.sub(myLoc).nor();
        instance.transform.trn(move.x * speed * delta, 0, move.z * speed * delta);
    }

    /**
     * returns the health of the troop
     * @return health of the troop
     */
    public float getHealth(){
        return health;
    }

    /**
     *sets a new health for the troop
     * @param newHealth the new value that health is set to
     */
    public void setHealth(float newHealth){health = newHealth;}

    /**
     * removes the troop from the game
     */
    public void death() {
        game.getActors().remove(this);
        getPlayer().getTroops().remove(this);
    }

    /**
     * returns the damage that the troop deals
     * @return the value of the damage that the troop deals
     */
    public float getDamage(){
        return damage;
    }

    /**
     * returns the speed
     * @return float the speed
     */
    public float getSpeed(){
        return speed;
    }

    /**
     * returns the game object
     * @return SinglePlayerGame the game object
     */
    public SinglePlayerGame getGame(){return game;}
    /**
     * returns the player
     * @return Player the player
     */
    public Player getPlayer(){return player;}
    /**
     * returns the range value
     * @return double the range
     */
    public double getRange(){
        return range;
    }
}
