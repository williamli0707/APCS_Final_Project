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

    private Vector3 myLoc;
    private SinglePlayerGame game;
    private Vector2 lastAngle = new Vector2(0, 1); //check all until right axis is found LOL

    public Troop(float health, float damage, float speed, double range, float cost, SinglePlayerGame game, Vector3 loc, Player p) {
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.cost = cost;
        this.game = game;
        myLoc = loc;
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
<<<<<<< HEAD
    /**
     * Called each render frame to determine how a troop acts
     */
    public void act(float delta) {
=======


    public boolean act(float delta) {
>>>>>>> c85f8cb4c66eecd2e1eb7da2b55203c448bc5c51
        double leastDist = 2e9;
        Vector3 dest = myLoc;
        for(Star a : game.getStars()){
            if (a.getPlayer()==player) {
                continue;
            }
            Vector3 tempLoc = a.getLocation();
            if(myLoc.dst(tempLoc)<leastDist){
                leastDist = myLoc.dst(tempLoc);
                dest = tempLoc;
            }
        }
//        for(Troop a: game.getTroops()) {
//            if (a.getLocation().dst(myLoc) <= range){
//                a.setHealth(a.getHealth() - damage);
//                setHealth(health - a.getDamage());
//            }
//        }
        if (health <= 0) {
            death();
            return false;
            //dead
        }
        move(dest, delta);
//        System.out.println("destination: " + dest);
        return true;
    }

    private void move(Vector3 newLoc, float delta) {
        Vector3 move = newLoc.sub(myLoc).nor();
        scene.modelInstance.transform.trn(move.x * speed * delta, 0, move.z * speed * delta);
        float angle = lastAngle.angleDeg(new Vector2(move.x, move.z));
        scene.modelInstance.transform.rotate(Vector3.Y, angle);
        lastAngle.rotateDeg(angle);
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
