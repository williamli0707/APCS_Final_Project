package com.github;

import com.github.game.Actor;
import com.github.game.Player;
import com.github.game.Star;

import java.util.ArrayList;

public class Game {
	ArrayList<Actor> actors;
	Star[] stars;
	Player player1, player2;

	public Game(Star[] stars) {
		actors = new ArrayList<>();
		this.stars = stars.clone();
		player1 = new Player(this);
		player2 = new Player(this);
	}

	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public void addActor(Actor actor, int player) {

	}

	public ArrayList<Actor> getActors() {
		return actors;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public Star[] getStars() {
		return stars;
	}
}
