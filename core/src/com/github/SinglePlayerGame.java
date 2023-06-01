package com.github;

import com.badlogic.gdx.math.Vector3;
import com.github.game.*;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

public class SinglePlayerGame {
	HashSet<Troop> troops;
	Star[] stars;
	Player player;

	public GameScreen screen;
	private static final int NUM_STARS = 10;
	public static float HOME_STAR_HEALTH = 1000f;
	private static final boolean DEMO = false;

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

		if(DEMO) for(int i = -100;i < -80;i++) for(int j = -100;j < 100;j++) addTroop(new Vanguard(this, new Vector3(i, 0, j), player));
	}

	public void genStars() {
		stars[0] = new HomeStar(this, 0, 0, HOME_STAR_HEALTH);
		stars[0].getConquered(player.getMothership());
		player.setHomeStar((HomeStar) stars[0]);
		for(int i = 1; i < NUM_STARS; i++) {
			float x = (float) (Math.random() * 200 - 100), y = (float) (Math.random() * 200 - 100);
			stars[i] = new Star(this, x, y);
		}
	}

	public void addActor(Troop actor) {
		troops.add(actor);
	}

	public void addTroop(Troop troop) {
		addActor(troop);
		screen.sceneManager.addScene(troop.getScene());
	}

	public HashSet<Troop> getTroops() {
		return troops;
	}

	public Player getPlayer() {
		return player;
	}

	public Star[] getStars() {
		return stars;
	}

	public void act(float delta) {
		if(DEMO) player.getMothership().act(delta);
		else {
			Queue<Troop> toRemove = new ArrayDeque<>();
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
