package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.github.SinglePlayerGame;
import com.github.GameScreen;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Mothership extends Troop implements InputProcessor {
	static {
		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}

	private static final float health = 1f, damage = 0f, speed = 5f, range = 2f;
	private static SceneAsset asset = new GLBLoader().load(Gdx.files.internal("gltfTest/mothership/mothership.glb"));
	private Scene scene;
	private GameScreen screen;
	private Vector3 vel, loc;
	public Mothership(SinglePlayerGame game, float x, float y, float z, Player p, GameScreen screen) {
		super(health, damage, speed, range, 0, game, new Vector3(x,y,z),p);
		vel = new Vector3(0, 0, 0);
		loc = new Vector3(x, y, z);
//		instance = new ModelInstance(model, x, y, z);
		scene = new Scene(asset.scene);
		this.screen = screen;
	}

	@Override
	public void act(float delta){
		double leastDist = 2e9;
		for(Actor a : getGame().getActors()){
			if (a.getPlayer()==getPlayer() || !(a instanceof Star))
				continue;
			if (loc.dst(a.getLocation()) <= range){
				if (a instanceof Star){
					Star fighter = (Star)a;
					fighter.getConquered(this);
				}
			}
		}
		move(delta);
	}

	private void move(float delta) {
		Vector3 displ = new Vector3(vel.x * delta, vel.y * delta, vel.z * delta);
		scene.modelInstance.transform.trn(displ);
		screen.camera.position.add(displ);
		screen.camera.update();
		loc.add(displ);
	}
	public ModelInstance getInstance() {
		return scene.modelInstance;
	}

	public Scene getScene() {
		return scene;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == 51) vel.z += speed;
		if(keycode == 47) vel.z -= speed;
		if(keycode == 29) vel.x += speed;
		if(keycode == 32) vel.x -= speed;
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == 51) vel.z -= speed;
		if(keycode == 47) vel.z += speed;
		if(keycode == 29) vel.x -= speed;
		if(keycode == 32) vel.x += speed;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
//		System.out.println("keyTyped");
		if(character == 'p') System.out.println(loc);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
