package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.github.Game;

public class Mothership extends Troop {
	static {
		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("placeholder.g3dj"));
	}

	private static final float health = 1f, damage = 0f, speed = 5f, range = 2f;
	public Mothership(Game game, float x, float y, float z, Player p) {
		super(health, damage, speed, range, game, new Location(x,y,z),p);
		instance = new ModelInstance(model, x, y, z);
	}

	@Override
	public void act(float delta){
		double leastDist = 2e9;
		Location dest = getLocation();
		for(Actor a : getGame().getActors()){
			if (a.getPlayer()==getPlayer() || !(a instanceof Star))
				continue;
			Location tempLoc = a.getLocation();
			if(getLocation().distance(tempLoc)<leastDist){
				leastDist=getLocation().distance(tempLoc);
				dest = tempLoc;
			}
			if (tempLoc.distance(getLocation()) <= range){
				if (a instanceof Star){
					Star fighter = (Star)a;
					fighter.getConquered(this);
				}
			}
		}
		move(dest, delta);
	}

	private void move(Location newLoc, float delta) {
		double dist = getLocation().distance(newLoc);
		instance.transform.trn((float)((newLoc.getX()-getLocation().getX())/dist) * speed * delta,
				0,
				(float)((newLoc.getZ()-getLocation().getZ())/dist) * speed * delta);
	}
	public ModelInstance getInstance() {
		return instance;
	}
}
