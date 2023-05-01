package com.github;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.JsonReader;

public class Main extends Game {
	public Environment environment;
	public PerspectiveCamera camera;
	public CameraInputController cameraController;
	public ModelBatch modelBatch;
	public SpriteBatch spriteBatch;
	public BitmapFont font;

	@Override
	public void create() {
		// Create an environment so we have some lighting
//		environment = new Environment();
//		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
//		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
//
//		modelBatch = new ModelBatch();
//
//		// Create a perspective camera with some sensible defaults
//		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		camera.position.set(10f, 10f, 10f);
//		camera.lookAt(0, 0, 0);
//		camera.near = 1f;
//		camera.far = 300f;
//		camera.update();
//
//		// Import and instantiate our model (called "myModel.g3dj")
//		ModelBuilder modelBuilder = new ModelBuilder();
//		model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("/Users/williamli/Blender/test.g3dj"));
//		instance = new ModelInstance(model);
//
//		cameraController = new CameraInputController(camera);
//		Gdx.input.setInputProcessor(cameraController);
		modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch();
		environment = new Environment();
		font = new BitmapFont();
		this.setScreen(new LoginScreen(this));
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
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }
}
