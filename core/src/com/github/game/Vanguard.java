package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Vanguard extends Troop {
	public static float HEALTH = 125.0f, DAMAGE = 15.0f, SPEED = 4f, RANGE = 10.0f, COST = 375;

	public Vanguard(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(assetVanguard.scene);
		scene.modelInstance.transform.trn(v);
		sprite = new Sprite(p == null ? game.screen.v_hostile : game.screen.v_friendly);
		sprite.setBounds(0, 0, 0, 0);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}

	public static void init() {
		assetVanguard = new GLBLoader().load(Gdx.files.internal("gltfTest/vanguard2/vanguard.glb"));
	}
}
