package com.github;

import com.github.game.Actor;
import com.github.game.Player;
import com.github.game.Star;
import com.github.game.Troop;

import java.util.ArrayList;

public class SinglePlayerGame {
	ArrayList<Actor> actors;
	Star[] stars;
	Player player;

	public GameScreen screen;
	private static int NUM_STARS = 10;

	public SinglePlayerGame(GameScreen screen) {
		actors = new ArrayList<>();
		stars = new Star[NUM_STARS];
		this.screen = screen;
		player = new Player(this);
		screen.sceneManager.addScene(player.getMothership().getScene());
//		enemy = new Player(this);
		genStars();
	}

	public void genStars() {
		for(int i = 0; i < NUM_STARS; i++) {
			float x = (float) (Math.random() * 200 - 100), y = (float) (Math.random() * 200 - 100);
			stars[i] = new Star(this, x, y);
		}
	}

	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public void addTroop(Troop troop) {
		addActor(troop);
		screen.sceneManager.addScene(troop.getScene());
	}

	public ArrayList<Actor> getActors() {
		return actors;
	}

	public Player getPlayer() {
		return player;
	}

	public Star[] getStars() {
		return stars;
	}

	public void act(float delta) {
		player.getMothership().act(delta);
		for(Actor actor : actors) {
			actor.act(delta);
		}
	}
}
