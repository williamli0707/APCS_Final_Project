package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Game;

public class Star implements Actor {

	static Model model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("star.g3dj"));
	private Game game;
	private float x, y, z;
	private Player player=null;
	ModelInstance instance;

	public Star(Game game, float x, float y, float z) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.z = z;
		instance = new ModelInstance(model, x, y, z);
	}

	@Override
	public ModelInstance getInstance() {
		return instance;
	}

	@Override
	public Location getLocation() {
		return new Location(x,y,z);
	}
	public Player getPlayer(){
		return player;
	}
	public void act(float delta) {
	if (player!=null){
		player.addResources(5);
	}
	}
}
