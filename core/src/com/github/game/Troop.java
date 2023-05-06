package com.github.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.github.Main;

public abstract class Troop implements Actor {
    private float health = -1f, damage = -1f, speed = -1f, range = -1f;
    static Model model;
    ModelInstance instance;
    private float x, y, z;
    private Main game;

    public Troop(float health, float damage, float speed, float range, Main game, float x, float y, float z) {
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.game = game;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
