package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Aegis extends Troop {
	public static final float HEALTH = 100.0f, DAMAGE = 50.0f, SPEED = 2f, RANGE = 15.0f, COST = 1000;

	public Aegis(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(assetAegis.scene);
		scene.modelInstance.transform.trn(v);
		game.screen.sceneManager.addScene(scene);
		sprite = new Sprite(p == null ? game.screen.a_hostile : game.screen.a_friendly);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}

	public static void init() {
		assetAegis = new GLBLoader().load(Gdx.files.internal("gltfTest/aegis/aegis.glb"));
	}
}
