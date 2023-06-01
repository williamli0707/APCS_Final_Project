package com.github.game;

import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;

import java.util.ArrayList;

public class Player {
	private SinglePlayerGame game;
	private ArrayList<Troop> troops;
	private ArrayList<Star> stars;
	private float resources;

	private Mothership mothership;
	private HomeStar homeStar;
	public static int RESOURCE_START = 150000;

	public Player(SinglePlayerGame game) {
		this.game = game;
		troops = new ArrayList<>();
		stars = new ArrayList<>();
		mothership = new Mothership(game, 0, 0, 0, this, game.screen);
		resources = RESOURCE_START;//TODO
	}

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
				addTroop(t);
				game.addTroop(t);
				addResources(-t.getCost());
				return;
			}
		}
		receiveMessage("Can't spawn there!");
	}

	public void receiveMessage(String string){
		System.out.println(string);
		game.screen.setStatus(string);
	}

	public ArrayList<Troop> getTroops() {
		return troops;
	}

	public void addTroop(Troop troop) {
		troops.add(troop);
	}

	public ArrayList<Star> getStars() {
		return stars;
	}

	public Mothership getMothership() {return mothership;}

	public float getResources() {
		return resources;
	}

	public void addResources(float amount){
		resources += amount;
	}

	public void setHomeStar(HomeStar homeStar) {this.homeStar = homeStar;}
	public HomeStar getHomeStar() {return homeStar;}
}
