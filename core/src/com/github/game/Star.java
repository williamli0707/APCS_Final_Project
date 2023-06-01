package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.github.PlayerData;
import com.github.SinglePlayerGame;

public class Star implements Actor {

	public static Model hostileModel = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("star-hostile.g3dj"));
	public static Model friendlyModel = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("star-friendly.g3dj"));
	SinglePlayerGame game;
	private Vector3 loc;
	private Player player=null;
	public static float RESOURCES_PER_SECOND = 5;
	private long tick = 1800;
	ModelInstance instance;

	public Star(SinglePlayerGame game, float x, float z) {
		this.game = game;
		loc = new Vector3(x, 0, z);
		instance = new ModelInstance(hostileModel, x,0f, z);
		game.screen.entities++;
	}

	@Override
	public ModelInstance getInstance() {
		return instance;
	}

	@Override
	public Vector3 getLocation() {
		return loc;
	}

	public Player getPlayer(){
		return player;
	}

	public void act(float delta) {
		tick++;
		if (player != null){
			player.addResources(RESOURCES_PER_SECOND * delta);
		} else {
//
			if(Math.random() < 0.00016 * (tick / 1800f)) {
				double randomNum = Math.random();
				System.out.println("spawning");
				if (randomNum < 0.15) {
					game.addTroop(new Aegis(game, loc, null));
				} else if (randomNum < 0.50) {
					game.addTroop(new Ranger(game, loc, null));
				} else {
					game.addTroop(new Vanguard(game, loc, null));
				}
			}
		}
	}

	/**
	 * Called from the mothership class to when the mothership is located on the star's position. The star now
	 * belongs to a player.
	 * @param mothership The mothership that conquers this star.
	 */
	public void getConquered(Mothership mothership){
		System.out.println("conquered");
		player = mothership.getPlayer();
		getPlayer().getStars().add(this);
		instance = new ModelInstance(friendlyModel, loc);
		PlayerData.add(0, 1, 0);
		if(getPlayer().getStars().size() == game.getStars().length + 1) game.screen.main.victory(); // oops i think this is a bug, player stars might have added home star twice?
	}

	/**
	 * Checks if the location is available for spawning
	 * @return true if the location is available and false if not
	 */
	public boolean canSpawn() {
		for (Troop troop: player.getTroops()){
			if (troop.getLocation().x == loc.x && troop.getLocation().z == loc.z){
				return false;
			}
		}
		return true;
	}
}
