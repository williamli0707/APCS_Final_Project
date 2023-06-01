package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.github.PlayerData;
import com.github.SinglePlayerGame;

/**
 * Represents a Star. Keeps track of a 3D instance, player controlling it, and its status.
 * @author Johnathan Kao, William Li
 * @version 6/1/23
 * @author Period 5
 * @author Sources - None
 */
public class Star implements Actor {

	/** the 3D model representing a hostile star */
	public static Model hostileModel = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("star-hostile.g3dj"));

	/** the 3D model representing a friendly star */
	public static Model friendlyModel = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("star-friendly.g3dj"));
	private final SinglePlayerGame game;
	private Vector3 loc;
	private Player player=null;

	/** Amount of resources added per second */
	public static float RESOURCES_PER_SECOND = 10;
	private long tick = 1800;
	private ModelInstance instance;

	/**
	 * Constructs a Star object in the specified game, at the specified location.
	 * @param game the game that the star is in
	 * @param x the x coordinate to spawn the star at
	 * @param z the z coordinate to spawn the star at
	 */
	public Star(SinglePlayerGame game, float x, float z) {
		this.game = game;
		loc = new Vector3(x, 0, z);
		instance = new ModelInstance(hostileModel, x,0f, z);
		game.screen.entities++;
	}

	/**
	 * Returns the 3D instance of the star.
	 * @return the 3D instance of the star
	 */
	@Override
	public ModelInstance getInstance() {
		return instance;
	}

	/**
	 * Returns the current location of the star.
	 * @return the current location of the star
	 */
	@Override
	public Vector3 getLocation() {
		return loc;
	}

	/**
	 * Returns the player controlling this star (can be null).
	 * @return the player controlling this star
	 */
	public Player getPlayer(){
		return player;
	}

	/**
	 * Actions every tick. Has a chance to spawn a random troop: 15% chance to spawn an Aegis,
	 * 35% chance to spawn a Vanguard, and 50% chance to spawn a Ranger. Also adds resources to
	 * the player.
	 * @param delta the time, in seconds, since the last tick
	 */
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
					game.addTroop(new Vanguard(game, loc, null));
				} else {
					game.addTroop(new Ranger(game, loc, null));
				}
			}
		}
	}

	/**
	 * Called from the mothership class to when the mothership is located on the star's position. The star now
	 * belongs to a player.
	 * @param mothership the mothership that conquers this star
	 */
	public void getConquered(Mothership mothership){
		System.out.println("conquered");
		player = mothership.getPlayer();
		getPlayer().getStars().add(this);
		instance = new ModelInstance(friendlyModel, loc);
		PlayerData.add(0, 1, 0, 0);
		if(getPlayer().getStars().size() == game.getStars().length + 1) game.screen.main.victory(); // oops i think this is a bug, player stars might have added home star twice?
	}
}
