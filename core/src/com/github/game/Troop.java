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
/**
 * represents the Parent Troop class
 * @author Leo Jiang, William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public abstract class Troop implements Actor {
    /**required health, damage, speed, cost for each troop */
    private float health, damage, speed, cost;
    /**required player */
    private Player player;
    /**required range */
    private double range;
    /**loads aegis, vanguard, ranger assets */
    public static SceneAsset assetAegis, assetVanguard, assetRanger;
    /**required scene for each troop */
    public Scene scene;
    /**required location */
    public Vector3 myLoc;
    /**the game troop is in */
    private SinglePlayerGame game;
    /**whether to move or not */
    private boolean move = true;
    /**angle to turn */
    public float angle = 0;
    /**asset/sprite to load */
    public Sprite sprite;
    /**whether to move the troop based on player command */
    private boolean manualOverride = false;
    /**location based on player command  */
    private Vector3 manualDest;
    /**
     * constructor, makes a troop object
     * @param health health of troop
     * @param damage damage of troop
     * @param speed speed of troop
     * @param range range of troop
     * @param cost cost of troop
     * @param game game troop is in
     * @param loc location of troop
     * @param p player troop belongs to
     */
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
        game.screen.entities++;
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
     * @param float delta each tick
     * @return boolean whether the troop is dead or not
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
    /**
     * determines movement of the troop
     * @param dest destination of the move
     * @param delta each tick
     */
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
    /**
     * checks if troop is dead
     * @return boolean yes/no on troop death
     */
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
        game.screen.entities--;
        System.out.println("died");
        try {
            game.screen.sceneManager.removeScene(scene);
        } catch (Exception e) {
//            e.printStackTrace();
            //do nothing so that it doesnt show in the demo
        }
        if(player == null) PlayerData.add(1, 0, 0, 0);
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
    /**
     * returns speed
     * @return float the speed
     */
    public float getSpeed(){
        return speed;
    }
    /**
     * returns game 
     * @return SinglePlayerGame the game
     */
    public SinglePlayerGame getGame(){return game;}
    /**
     * returns the player
     * @return Player the player
     */
    public Player getPlayer(){return player;}
    /**
     * returns the range
     * @return double the range
     */
    public double getRange(){
        return range;
    }
    /**
     * returns the asset/model
     * @return ModelInstance the model
     */
    public ModelInstance getInstance() { return scene.modelInstance; }
    /**
     * returns the scene
     * @return Scene the scene
     */
    public Scene getScene(){return scene;}
    /**
     * discards assets
     */
    public static void dispose() {
        assetRanger.dispose();
        assetVanguard.dispose();
        assetAegis.dispose();
    }
    /**
     * returns the sprite of a troop
     * @return Sprite the sprite
     */
    public Sprite getSprite() {
        return sprite;
    }
    /**
     * sets destination of troop for manual movement
     * @param destination the destination
     */
    public void setDestination(Vector2 destination) {
        manualOverride = true;
        manualDest = new Vector3(destination.x, 0, destination.y);
    }
}
