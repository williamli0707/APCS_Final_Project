package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.GameScreen;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

/**
 * represents the player controlled mothership in game
 * @author Leo Jiang, William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources: None
 */
public class Mothership extends Troop {
	static {
//		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}
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
	 * constructor for mothership, creates a unique mothership for each player
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
	 * how each mothership acts per tick
	 * @param float delta each tick
	 * @return boolean whether the mothership has died or not
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
	 * how each mothership moves
	 * @param delta each tick
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
	 * returns the asset
	 * @return ModelInstance the model
	 */
	public ModelInstance getInstance() {
		return scene.modelInstance;
	}
	/**
	 * returns the scene
	 * @return Scene the scene
	 */
	public Scene getScene() {
		return scene;
	}
	/**
	 * determines movement
	 * @param keycode the keypress
	 */
	public void keyDown(int keycode) {
		if(keycode == 51) vel.z ++;
		if(keycode == 47) vel.z --;
		if(keycode == 29) vel.x ++;
		if(keycode == 32) vel.x --;
	}
	/**
	 * determines movement
	 * @param keycode keypress
	 */
	public void keyUp(int keycode) {
		if(keycode == 51) vel.z --;
		if(keycode == 47) vel.z ++;
		if(keycode == 29) vel.x --;
		if(keycode == 32) vel.x ++;
	}
	/**
	 * determines keypresses
	 * @param character the keypress
	 */
	public void keyTyped(char character) {
//		System.out.println("keyTyped");
		if(character == 'p') System.out.println(curLoc);
		if(character == 'l') {
			for(Troop i: getPlayer().getTroops()) {
				System.out.println(i.getClass() + " " + i.myLoc);
			}
		}
	}
	/**
	 * helps determine movement via finding when the last keypress was
	 * @param screenX x coordinate on screen
	 * @param screenY y coordinate on screen
	 * @param pointer location pointer
	 * @param button the keypress
	 */
	public void touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT) {
			lastTouch.set(screenX, screenY);
		}
	}
	/**temporary test class */
	public void touchUp() {
	}
	/**
	 * used in determining how far to rotate screen when changing camera angles
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
	 * returns location of the mothership
	 * @return Vector3 curLoc the location
	 */
	public Vector3 getLocation() {
		return curLoc;
	}
}
