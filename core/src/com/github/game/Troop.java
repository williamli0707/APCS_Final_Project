package com.github.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.github.Game;

public abstract class Troop implements Actor {
    private float health = -1f, damage = -1f, speed = -1f, range = -1f;
    static Model model;
    ModelInstance instance;
    private double x, y, z;
    private Game game;

    public Troop(float health, float damage, float speed, float range, Game game, float x, float y, float z) {
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.game = game;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location getLocation() {
        return new Location(x, y, z);
    }
    public void act(float delta) {
        //TODO
    }

    public void move(double x, double y, double z) {
        //TODO
    }
}
