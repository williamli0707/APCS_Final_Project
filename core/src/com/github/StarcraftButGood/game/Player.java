package com.github.StarcraftButGood.game;

import com.badlogic.gdx.math.Vector3;
import com.github.StarcraftButGood.SinglePlayerGame;

import java.util.ArrayList;

/**
 * Represents a player in the game. Keeps track of owned troops, stars, and mothership.
 *
 * @author Johnathan Kao, William Li
 * @version 6/1/23
 * @author Period 5
 * @author Sources: None
 */

public class Player {
	private SinglePlayerGame game;
	private ArrayList<Star> stars;
	private float resources;

	private Mothership mothership;
	private HomeStar homeStar;
	/** how many resources every player starts with */
	public static int RESOURCE_START = 150;

	/**
	 * Constructor for Player. Creates a new player with the given game.
	 * @param game
	 */
	public Player(SinglePlayerGame game) {
		this.game = game;
		stars = new ArrayList<>();
		mothership = new Mothership(game, 0, 0, 0, this, game.screen);
		resources = RESOURCE_START;//TODO
	}

	/**
	 * Places a troop, if possible, at the given location. Does not place if the player does not have enough
	 * resources or if the location is not within 1000 units of a star owned by the player.
	 * @param type the type of troop to spawn
	 * @param loc the location to spawn at
	 */
	public void placeTroop(int type, Vector3 loc) {
		assert type == 1 || type == 2 || type == 3;
		float cost = type == 1 ? Ranger.COST : type == 2 ? Vanguard.COST : Aegis.COST;
		if(getResources() < cost) {
			receiveMessage("You do not have enough resources! ");
			return;
		}
		//1 = ranger, 2 = vanguard, 3 = aegis
		for(Star a: game.getStars()) {
			if(a.getLocation().dst(loc) <= 1000 && a.getPlayer() == this) {
				Troop t;
				if(type == 1) t = new Ranger(game, loc, this);
				else if(type == 2) t = new Vanguard(game, loc, this);
				else t = new Aegis(game, loc, this);
				game.addTroop(t);
				addResources(-t.getCost());
				return;
			}
		}
		receiveMessage("Can't spawn there!");
	}

	/**
	 * Sends a message to the player's screen.
	 * @param string the message to send
	 */
	public void receiveMessage(String string){
		System.out.println(string);
		game.screen.setStatus(string);
	}

	public ArrayList<Star> getStars() {
		return stars;
	}

	/**
	 * Returns the mothership that this player owns.
	 * @return the mothership that this player owns
	 */
	public Mothership getMothership() {return mothership;}

	/**
	 * Returns the amount of resources this player has.
	 * @return the amount of resources this player has
	 */
	public float getResources() {
		return resources;
	}

	/**
	 * Adds the given amount of resources to the player's total.
	 * @param amount the amount of resources to add
	 */
	public void addResources(float amount){
		resources += amount;
		resources = Math.max(0, resources);
	}

	/**
	 * Sets the home star of a player.
	 * @param homeStar the home star for the player
	 */
	public void setHomeStar(HomeStar homeStar) {this.homeStar = homeStar;}

	/**
	 * Gets the home star of a player.
	 * @return the home star of the player
	 */
	public HomeStar getHomeStar() {return homeStar;}
}
