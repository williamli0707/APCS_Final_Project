package com.github.StarcraftButGood;

import com.badlogic.gdx.math.Vector3;
import com.github.StarcraftButGood.game.*;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

/**
 * Represents a game. Keeps track of troops, stars, and the player.
 * @author William Li, Johnathan Kao, Leo Jiang
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public class SinglePlayerGame {
	private HashSet<Troop> troops;
	private Star[] stars;
	private Player player;
	private Queue<Troop> toRemove;
	/** screen displaying this game */
	public GameScreen screen;
	private static final int NUM_STARS = 10;
	public static float HOME_STAR_HEALTH = 1000f;
	public static final boolean DEMO = false;//

	/**
	 * Constructor for the SinglePlayerGame. Initializes the troops and stars.
	 * @param screen the screen displaying this game
	 */
	public SinglePlayerGame(GameScreen screen) {
		troops = new HashSet<>();
		stars = new Star[NUM_STARS];
		this.screen = screen;
		player = new Player(this);
		screen.sceneManager.addScene(player.getMothership().getScene());
//		enemy = new Player(this);
		genStars();
		getPlayer().getStars().add(stars[0]);
		troops.add(player.getMothership());
		PlayerData.add(0, 0, 1, 0);

		toRemove = new ArrayDeque<>();

		if(DEMO) for(int i = -100;i < -85;i++) for(int j = -100;j < 100;j++) addTroop(new Vanguard(this, new Vector3(i, 0, j), player));
	}

	/**
	 * Generates the stars for the game.
	 */
	public void genStars() {
		stars[0] = new HomeStar(this, 0, 0, HOME_STAR_HEALTH);
		stars[0].getConquered(player.getMothership());
		player.setHomeStar((HomeStar) stars[0]);
		for(int i = 1; i < NUM_STARS; i++) {
			float x = (float) (Math.random() * 200 - 100), y = (float) (Math.random() * 200 - 100);
			stars[i] = new Star(this, x, y);
		}
	}

	/**
	 * Adds a troop to the troop list.
	 * @param troop the troop to add
	 */
	public void addTroop(Troop troop) {
		troops.add(troop);
		screen.sceneManager.addScene(troop.getScene());
	}

	/**
	 * Returns the troop list.
	 * @return the troop list
	 */
	public HashSet<Troop> getTroops() {
		return troops;
	}

	/**
	 * Returns the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the list of stars.
	 * @return the list of stars
	 */
	public Star[] getStars() {
		return stars;
	}

	/**
	 * Acts every tick. Calls each troop's act method and each star's act method.
	 * @param delta the time, in seconds, since the last tick
	 */
	public void act(float delta) {
		if(DEMO) player.getMothership().act(delta);
		else {
			for (Troop troop : troops) {
				if (!troop.act(delta)) {
					toRemove.add(troop);
				}
			}
			while (!toRemove.isEmpty()) {
				troops.remove(toRemove.remove());
			}
			for (Star star : stars) {
				star.act(delta);
			}
		}
	}

}
