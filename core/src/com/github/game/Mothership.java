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

public class Mothership extends Troop {
	static {
//		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}

	public static final float health = 1f, damage = 0f, speed = 5f, range = 2f;
	private static SceneAsset asset = new GLBLoader().load(Gdx.files.internal("gltfTest/mothership/mothership.glb"));
	private Scene scene;
	private GameScreen screen;
	private Vector3 vel, loc;
	private Vector2 lastTouch;

	private float angle = 0;
	private int tick = 0;
	public Mothership(SinglePlayerGame game, float x, float y, float z, Player p, GameScreen screen) {
		super(health, damage, speed, range, 0, game, new Vector3(x,y,z),p);
		vel = new Vector3(0, 0, 0);
		loc = new Vector3(x, y, z);
//		instance = new ModelInstance(model, x, y, z);
		scene = new Scene(asset.scene);
		lastTouch = new Vector2(0, 0);
		this.screen = screen;
	}

	@Override
	public void act(float delta){
		for(Star a : getGame().getStars()){
			if (a.getPlayer() == getPlayer())
				continue;
			if (loc.dst(a.getLocation()) <= range){
				a.getConquered(this);
			}
			if (health <= 0)
				death();
		}
		tick++;
		move(delta);
	}

	private void move(float delta) {
		Vector3 orig = new Vector3(vel.x, vel.y, vel.z), dir = vel.nor().rotate(Vector3.Y, angle);
		Vector3 displ = new Vector3(dir.x * speed * delta, dir.y * speed * delta, dir.z * speed * delta);
		scene.modelInstance.transform.trn(displ);
		screen.camera.position.add(displ);
		screen.camera.update();
		loc.add(displ);
		vel = orig;
	}
	public ModelInstance getInstance() {
		return scene.modelInstance;
	}

	public Scene getScene() {
		return scene;
	}

	public void keyDown(int keycode) {
		if(keycode == 51) vel.z ++;
		if(keycode == 47) vel.z --;
		if(keycode == 29) vel.x ++;
		if(keycode == 32) vel.x --;
	}

	public void keyUp(int keycode) {
		if(keycode == 51) vel.z --;
		if(keycode == 47) vel.z ++;
		if(keycode == 29) vel.x --;
		if(keycode == 32) vel.x ++;
	}

	public void keyTyped(char character) {
//		System.out.println("keyTyped");
		if(character == 'p') System.out.println(loc);
	}

	public void touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT) {
			lastTouch.set(screenX, screenY);
		}
	}

	public void touchDragged(int screenX, int screenY, int pointer) {
		float anglex = (lastTouch.x - screenX) / 30f;
		angle += anglex;
		screen.camera.rotateAround(loc, Vector3.Y, anglex);
		scene.modelInstance.transform.rotate(Vector3.Y, anglex);
//		screen.camera.rotateAround(loc, Vector3.Z, angley);
//		screen.camera.rotateAround(loc, Vector3.X, angley);
		lastTouch.set(screenX, screenY);
	}
}
