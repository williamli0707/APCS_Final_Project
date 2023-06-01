package com.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.github.SinglePlayerGame;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Ranger extends Troop {

	public static float HEALTH = 50.0f, DAMAGE = 30.0f, SPEED = 5.0f, RANGE = 7.5f, COST = 150;

	public Ranger(SinglePlayerGame game, Vector3 v, Player p) {
		super(HEALTH, DAMAGE, SPEED, RANGE, COST, game, v, p);
//		instance = new ModelInstance(model, v.x, v.y, v.z);
		scene = new Scene(assetRanger.scene);
		scene.modelInstance.transform.trn(v);
		sprite = new Sprite(p == null ? game.screen.r_hostile : game.screen.r_friendly);
		sprite.setBounds(0, 0, 0, 0);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

		if(p == game.getPlayer()) for(Material m: scene.modelInstance.materials) m.set(ColorAttribute.createEmissive(0.4f, 1, 0.4f, 0.5f));
		else for(Material m: scene.modelInstance.materials) m.set(ColorAttribute.createEmissive(1, 0.4f, 0.4f, 0.5f));
	}

	public static void init() {
		assetRanger = new GLBLoader().load(Gdx.files.internal("gltfTest/ranger/ranger.glb"));
	}
}
