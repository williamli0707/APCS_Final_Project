package com.github.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.github.Game;

public abstract class Troop implements Actor {
    private float health, damage, speed;
    private Player player;
    private double range;
    static Model model;
    ModelInstance instance;

    private Location myLoc;
    private Game game;

    public Troop(float health, float damage, float speed, double range, Game game, Location loc, Player p) {
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.game = game;
        myLoc = loc;
        player = p;
    }

    public Location getLocation() {
        return myLoc;
    }
    public void act(float delta) {
        for(Actor a : game.getActors()){
            if (a.getPlayer().equals(player))
                continue;
            Location tempLoc = a.getLocation();
            if (tempLoc.Distance(myLoc) <= range){
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
    }

    public void move(Location newLoc) {
        myLoc = newLoc;
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


    public Player getPlayer(){return player;}
    public double getRange(){
        return range;
    }
}
