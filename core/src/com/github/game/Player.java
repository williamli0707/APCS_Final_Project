package com.github.game;

import com.github.Game;

import java.util.ArrayList;

public class Player {
	private Game game;
	private ArrayList<Troop> troops;
	private ArrayList<Star> stars;
	private int resources=0;
	public Player(Game game) {
		this.game = game;
		troops = new ArrayList<>();
		stars = new ArrayList<>();
	}

	public void placeTroop(Troop troop, Star star) {

	}

	public ArrayList<Troop> getTroops() {
		return troops;
	}
	public ArrayList<Star> getStars() {
		return stars;
	}
	public int getResources() {
		return resources;
	}
	public void addResources(int amount){
		resources+=amount;
	}
}
