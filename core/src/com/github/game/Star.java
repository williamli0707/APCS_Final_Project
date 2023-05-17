package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Game;

public class Star implements Actor {

	static Model model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("star.g3dj"));
	private Game game;
	private float x, z;
	private Player player=null;
	ModelInstance instance;

	public Star(Game game, float x, float z) {
		this.game = game;
		this.x = x;
		this.z = z;
		instance = new ModelInstance(model, x,0f, z);
	}

	@Override
	public ModelInstance getInstance() {
		return instance;
	}

	@Override
	public Vector3 getLocation() {
		return new Vector3(x,0,z);
	}
	public Player getPlayer(){
		return player;
	}
	public void act(float delta) {
		if (player!=null){
			player.addResources(5);
		}
	}
	public void getConquered(Mothership mothership){
		player=mothership.getPlayer();
	}
	public boolean canSpawn() {
		for (Troop troop: player.getTroops()){
			if (troop.getLocation().x == x && troop.getLocation().z == z){
				return false;
			}
		}
		return true;
	}
	public void spawn(Troop troop){

		if (troop.getHealth()==50.0){
			troop=new Ranger(game, x, 0, z, player);
		}
		else if (troop.getHealth()==1000.0f){
			troop=new Vanguard(game, x, 0, z, player);
		}
		else{
			troop=new Aegis(game,x, 0,z, player);
		}
		player.addTroop(troop);

	}
}
