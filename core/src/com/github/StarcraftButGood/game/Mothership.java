package com.github.StarcraftButGood.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.StarcraftButGood.GameScreen;
import com.github.StarcraftButGood.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

/**
 * Represents the player-controlled mothership in game
 * @author Leo Jiang, William Li
 * @version 6/1/23
 * @author Period 5
 * @author Sources: None
 */
public class Mothership extends Troop {
	/**
	 * health, damage, speed, and range of the mothership
	 */
	public static float health = 100f , damage = 0f, speed = 5f, range = 2f;
	/**
	 * asset for the troop
	 */
	private static final SceneAsset asset = new GLBLoader().load(Gdx.files.internal("gltfTest/mothership/mothership.glb"));
	/**
	 * scene for the mothership
	 */
	private Scene scene;
	/**screen for this game */
	private GameScreen screen;
	/**location and direction */
	private Vector3 vel, curLoc;
	/**helps pathfind */
	private Vector2 lastTouch;
	/**pathfinding angle */
	private float angle = 0;
	/**
	 * Constructor for mothership with the given game, location, player and screen.
	 * @param game the specific game
	 * @param x starting x coordinate
	 * @param y starting y coordinate
	 * @param z starting z coordinate
	 * @param p player mothership belongs to
	 * @param screen screen for the game
	 */
	public Mothership(SinglePlayerGame game, float x, float y, float z, Player p, GameScreen screen) {
		super(health, damage, speed, range, 0, game, new Vector3(x,y,z),p);
		vel = new Vector3(0, 0, 0);
		curLoc = new Vector3(x, y, z);
//		instance = new ModelInstance(model, x, y, z);
		scene = new Scene(asset.scene);
		lastTouch = new Vector2(0, 0);
		this.screen = screen;
	}

	/**
	 * How each mothership acts per tick.
	 * @param delta time, in seconds, since last tick
	 * @return whether the mothership has died or not
	 */
	@Override
	public boolean act(float delta){
		for(Star a : getGame().getStars()){
			if (a.getPlayer() == getPlayer())
				continue;
			if (curLoc.dst(a.getLocation()) <= range){
				a.getConquered(this);
			}
		}
		move(delta);
		return checkDeath();
	}

	/**
	 * How each mothership moves.
	 * @param delta time, in seconds, since last tick
	 */
	private void move(float delta) {
		Vector3 orig = new Vector3(vel.x, vel.y, vel.z), dir = vel.nor().rotate(Vector3.Y, angle);
		Vector3 displ = new Vector3(dir.x * speed * delta, dir.y * speed * delta, dir.z * speed * delta);
		scene.modelInstance.transform.trn(displ);
		screen.camera.position.add(displ);
		screen.camera.update();
		curLoc.add(displ);
		vel = orig;
	}
	/**
	 * Returns the 3D instance of this object.
	 * @return the model
	 */
	public ModelInstance getInstance() {
		return scene.modelInstance;
	}
	/**
	 * Returns the scene object.
	 * @return the scene
	 */
	public Scene getScene() {
		return scene;
	}
	/**
	 * Determines movement.
	 * @param keycode the keypress
	 */
	public void keyDown(int keycode) {
		if(keycode == 51) vel.z ++;
		if(keycode == 47) vel.z --;
		if(keycode == 29) vel.x ++;
		if(keycode == 32) vel.x --;
	}
	/**
	 * Determines movement.
	 * @param keycode keypress
	 */
	public void keyUp(int keycode) {
		if(keycode == 51) vel.z --;
		if(keycode == 47) vel.z ++;
		if(keycode == 29) vel.x --;
		if(keycode == 32) vel.x ++;
	}

	/**
	 * Helps determine movement via finding when the last keypress was.
	 * @param screenX x coordinate on screen
	 * @param screenY y coordinate on screen
	 * @param pointer the pointer
	 * @param button the button pressed
	 */
	public void touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT) {
			lastTouch.set(screenX, screenY);
		}
	}

	/**
	 * Used in determining how far to rotate screen when changing camera angles.
	 * @param screenX x coordinate on screen
	 * @param screenY y coordinate on screen
	 * @param pointer location dragged to
	 */
	public void touchDragged(int screenX, int screenY, int pointer) {
		float anglex = (lastTouch.x - screenX) / 30f;
		angle += anglex;
		screen.camera.rotateAround(curLoc, Vector3.Y, anglex);
		scene.modelInstance.transform.rotate(Vector3.Y, anglex);
		lastTouch.set(screenX, screenY);
	}
	/**
	 * Returns the current location of the mothership.
	 * @return Vector3 curLoc the location
	 */
	public Vector3 getLocation() {
		return curLoc;
	}
}
