package com.github.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.GameScreen;
import com.github.PlayerData;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public abstract class Troop implements Actor {
    private float health, damage, speed, cost;
    private Player player;
    private double range;
    public static SceneAsset assetAegis, assetVanguard, assetRanger;
    public Scene scene;

    public Vector3 myLoc;
    private SinglePlayerGame game;
    private boolean move = true;
    public float angle = 0;
    public Sprite sprite;
    private boolean manualOverride = false;
    private Vector3 manualDest;

    public Troop(float health, float damage, float speed, double range, float cost, SinglePlayerGame game, Vector3 loc, Player p) {
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.cost = cost;
        this.game = game;
        myLoc = new Vector3(loc.x, loc.y, loc.z);
        player = p;
        System.out.println("new troop: health = " + health + ", damage = " + damage + ", speed = " + speed + ", location = " + loc);
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
    public boolean act(float delta) {
        if(manualOverride) {
            for (Troop a : game.getTroops()) {
                if (a.getPlayer() == player) continue;
                if (a.getLocation().dst(myLoc) <= range) a.setHealth(a.getHealth() - damage * delta);
            }
            if (player == null && game.getPlayer().getHomeStar().getLocation().dst(myLoc) <= range) {
                game.getPlayer().getHomeStar().setNewHealth(game.getPlayer().getHomeStar().getHealth() - damage * delta);
            }
            move(manualDest, delta);
        }
        else {
            Vector3 dest = new Vector3(1e9f, 1e9f, 1e9f);
            if (player == null) dest = game.getPlayer().getHomeStar().getLocation();
            for (Star a : game.getStars()) {
                if (a.getPlayer() == player) continue;
                if (myLoc.dst(dest) > myLoc.dst(a.getLocation())) dest = a.getLocation();
            }
            for (Troop a : game.getTroops()) {
                if (a.getPlayer() == player) continue;
                if (myLoc.dst(dest) > myLoc.dst(a.getLocation())) dest = a.getLocation();
                if (a.getLocation().dst(myLoc) <= range) {
                    a.setHealth(a.getHealth() - damage * delta);
                    if(player == game.getPlayer()) GameScreen.line(myLoc.x, myLoc.y, myLoc.z, a.getLocation().x, a.getLocation().y, a.getLocation().z, 0f, 1f, 0f, 1f);
                    else GameScreen.line(myLoc.x, myLoc.y, myLoc.z, a.getLocation().x, a.getLocation().y, a.getLocation().z, 1f, 0f, 0f, 1f);
//                System.out.println(damage * delta);
                }
            }
            if (player == null && game.getPlayer().getHomeStar().getLocation().dst(myLoc) <= range) {
                game.getPlayer().getHomeStar().setNewHealth(game.getPlayer().getHomeStar().getHealth() - damage * delta);
            }
            move(dest, delta);
        }
        return checkDeath();
    }

    private void move(Vector3 dest, float delta) {
        move = myLoc.dst(dest) > range;
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setRotation(-angle);
        sprite.setBounds(-myLoc.x * 2.5f + GameScreen.horizontalOffset, myLoc.z * 2.5f + GameScreen.verticalOffset, 12, 12);
        Vector3 move = dest.cpy().sub(myLoc).nor();
        angle = new Vector2(move.x, move.z).angleDeg();
        if(!dest.equals(myLoc)) scene.modelInstance.transform.idt().rotateTowardDirection(move, Vector3.Y).trn(myLoc);
        if(!this.move) return;
        scene.modelInstance.transform.trn(move.x * speed * delta, 0, move.z * speed * delta);
        myLoc.add(move.x * speed * delta, 0, move.z * speed * delta);
    }

    /**
     * returns the health of the troop
     * @return health of the troop
     */
    public float getHealth(){
        return health;
    }

    public boolean checkDeath() {
        if (health <= 0){
            death();
            return false;
            //dies
        }
        return true;
    }

    /**
     *sets a new health for the troop
     * @param newHealth the new value that health is set to
     */
    public void setHealth(float newHealth) {
        health = newHealth;
//        System.out.println(health);
    }

    /**
     * removes the troop from the game
     */
    public void death() {
        System.out.println("died");
        scene.modelInstance.userData = 0;
        game.screen.sceneManager.removeScene(scene);
        if(player == null) PlayerData.add(1, 0, 0);
        System.out.println(game.screen.sceneManager.getRenderableProviders().contains(scene, true));
//        System.out.println("died " + this);
    }

    /**
     * returns the damage that the troop deals
     * @return the value of the damage that the troop deals
     */
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
    public ModelInstance getInstance() { return scene.modelInstance; }
    public Scene getScene(){return scene;}
    public static void dispose() {
        assetRanger.dispose();
        assetVanguard.dispose();
        assetAegis.dispose();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setDestination(Vector2 destination) {
        manualOverride = true;
        manualDest = new Vector3(destination.x, 0, destination.y);
    }
}
