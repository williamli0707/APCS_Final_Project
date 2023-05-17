package com.github;

import com.github.game.Actor;
import com.github.game.Player;
import com.github.game.Star;
import com.github.game.Troop;

import java.util.ArrayList;

public class Game {
	ArrayList<Actor> actors;
	Star[] stars;
	Player player, enemy;

	public Game(Star[] stars) {
		actors = new ArrayList<>();
		this.stars = stars.clone();
		player = new Player(this);
		enemy = new Player(this);
	}

	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public void addTroop(Troop troop, int p) {
		addActor(troop);
		if(p == 1) player.addTroop(troop);
		else enemy.addTroop(troop);
	}

	public ArrayList<Actor> getActors() {
		return actors;
	}

	public Player getPlayer1() {
		return player;
	}

	public Player getEnemy() {
		return enemy;
	}

	public Star[] getStars() {
		return stars;
	}
}
