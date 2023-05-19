package com.github.game;

import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;

import java.util.ArrayList;

public class Player {
	private SinglePlayerGame game;
	private ArrayList<Troop> troops;
	private ArrayList<Star> stars;
	private int resources;

	private Mothership mothership;
	public Player(SinglePlayerGame game) {
		this.game = game;
		troops = new ArrayList<>();
		stars = new ArrayList<>();
		mothership = new Mothership(game, 0, 0, 0, this, game.screen);
	}

	public void placeTroop(int type, Vector3 loc) {
		//1 = ranger, 2 = vanguard, 3 = aegis
		for(Star a: game.getStars()) {
			if(a.getLocation().dst(loc) <= 5 && a.getPlayer() == null) {
				Troop t = null;
				if(type == 1) t = new Ranger(game, a.getLocation(), this);
				else if(type == 2) t = new Vanguard(game, a.getLocation(), this);
				else if(type == 3) t = new Aegis(game, a.getLocation(), this);

				assert t != null;

				game.addTroop(t);
				return;
			}
		}
		receiveMessage("Can't spawn there!");
	}
	public void receiveMessage(String string){
		//TODO
	}

	public ArrayList<Troop> getTroops() {
		return troops;
	}
	public ArrayList<Troop> addTroop(Troop troop) {
		troops.add(troop);
		return troops;
	}
	public ArrayList<Star> getStars() {
		return stars;
	}
	public Mothership getMothership() {return mothership;}
	public int getResources() {
		return resources;
	}
	public void addResources(int amount){
		resources+=amount;
	}
}
