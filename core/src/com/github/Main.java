package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.github.game.*;

import java.io.File;
import java.util.Scanner;


public class Main extends com.badlogic.gdx.Game {
	public Environment environment;
	public ModelBatch modelBatch;
	public SpriteBatch spriteBatch;
	public BitmapFont font;

	@Override
	public void create() {
		try {
			Scanner in = new Scanner(new File("config.txt"));

			Ranger.HEALTH = in.nextFloat();
			Ranger.DAMAGE = in.nextFloat();
			Ranger.SPEED = in.nextFloat();
			Ranger.RANGE = in.nextFloat();
			Ranger.COST = in.nextFloat();

			Vanguard.HEALTH = in.nextFloat();
			Vanguard.DAMAGE = in.nextFloat();
			Vanguard.SPEED = in.nextFloat();
			Vanguard.RANGE = in.nextFloat();
			Vanguard.COST = in.nextFloat();

			Aegis.HEALTH = in.nextFloat();
			Aegis.DAMAGE = in.nextFloat();
			Aegis.SPEED = in.nextFloat();
			Aegis.RANGE = in.nextFloat();
			Aegis.COST = in.nextFloat();

			Mothership.health = in.nextFloat();
			Mothership.speed = in.nextFloat();

			SinglePlayerGame.HOME_STAR_HEALTH = in.nextFloat();

			Player.RESOURCE_START = in.nextInt();

			System.err.println("using custom configuration");

		} catch (Exception ignore) {
//			ignore.printStackTrace();
		}

		modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch();
		environment = new Environment();
		font = new BitmapFont();
//		GLTFTestScreen screen = new GLTFTestScreen();
//		gameScreen();
		loginScreen();
//		menuScreen();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		modelBatch.dispose();
		font.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	public void loginScreen() {
		if(getScreen() != null) getScreen().dispose();
		setScreen(new LoginScreen(this));
	}

	public void menuScreen() {
		if(getScreen() != null) getScreen().dispose();
		setScreen(new MainMenuScreen(this));
	}

	public void gameScreen() {
		if(getScreen() != null) getScreen().dispose();
		GameScreen screen = new GameScreen(this);
		this.setScreen(screen);
		Gdx.input.setInputProcessor(screen);
	}

	public void victory() {
		if(getScreen() != null) getScreen().dispose();
		setScreen(new VictoryScreen(this));
	}

	public void defeat() {
		if(getScreen() != null) getScreen().dispose();
		setScreen(new DefeatScreen(this));
	}
}