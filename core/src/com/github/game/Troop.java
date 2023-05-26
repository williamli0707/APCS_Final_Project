package com.github.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public abstract class Troop implements Actor {
    private float health, damage, speed, cost;
    private Player player;
    private double range;
    static SceneAsset assetAegis, assetVanguard, assetRanger;
    Scene scene;

    public Vector3 myLoc, dest = new Vector3(1e9f, 1e9f, 1e9f);
    private Vector2 axis = Vector2.X;
    private SinglePlayerGame game;
    private boolean move = true;
    private int tick;

    public Troop(float health, float damage, float speed, double range, float cost, SinglePlayerGame game, Vector3 loc, Player p) {
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.cost = cost;
        this.game = game;
        myLoc = new Vector3(loc.x, loc.y, loc.z);
        player = p;
        System.out.println("new troop: health = " + health + ", damage = " + damage + ", speed = " + speed);
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
    /*
     * Called each render frame to determine how a troop acts
     */
    public boolean act(float delta) {
        boolean rotate = false;
        for(Star a : game.getStars()){
            if (a.getPlayer()==player) {
                continue;
            }
            if(myLoc.dst(a.getLocation()) < myLoc.dst(dest)){
                System.out.println();
                dest = a.getLocation();
                rotate = true;
            }
            if (a.getLocation().dst(myLoc) <= range){
                if(player == null) move = false;
//                a.setHealth(a.getHealth() - damage * delta);
            }
            else move = true;
        }
        if (health <= 0) {
            death();
            return false;
            //dead
        }
        move(delta, rotate);
//        System.out.println("destination: " + dest);
        return true;
    }

    private void move(float delta, boolean rotate) {
        Vector3 move = dest.cpy().sub(myLoc).nor();
        if(rotate && !dest.equals(myLoc)) {
            scene.modelInstance.transform.rotateTowardTarget(move, Vector3.Y);
        }

        if(!this.move) return;

//        Vector3 move = Vector3.Zero;
        scene.modelInstance.transform.trn(move.x * speed * delta, 0, move.z * speed * delta);
        myLoc.add(move.x * speed * delta, 0, move.z * speed * delta);

//        System.out.println("moved by " + move.x * speed * d

//        scene.modelInstance.transform.d
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
    public void setHealth(float newHealth) {
        health = newHealth;
    }

    /**
     * removes the troop from the game
     */
    public void death() {
        System.out.println("death");
//        game.getTroops().remove(this);
//        if(getPlayer() != null) getPlayer().getTroops().remove(this);
        game.screen.sceneManager.removeScene(getScene());
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
}
