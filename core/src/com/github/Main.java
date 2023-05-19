package com.github;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class Main extends com.badlogic.gdx.Game {
	public Environment environment;
	public PerspectiveCamera camera;
	public CameraInputController cameraController;
	public ModelBatch modelBatch;
	public SpriteBatch spriteBatch;
	public BitmapFont font;

	@Override
	public void create() {
		modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch();
		environment = new Environment();
		font = new BitmapFont();
//		GLTFTestScreen screen = new GLTFTestScreen();

//		this.setScreen(new LoginScreen(this));
		SinglePlayerGame game = new SinglePlayerGame();
		this.setScreen(new GameScreen(this, game));
		this.setScreen(screen);
//		Gdx.input.setInputProcessor(screen);
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
}
