package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.game.Actor;
import com.github.game.Ranger;
import com.github.game.Planet;
import com.github.game.Star;

public class GameScreen implements Screen {
    private Environment environment;
    private PerspectiveCamera camera;
    private CameraInputController cameraController;
    private ModelBatch modelBatch;
    private Array<Actor> actors;
    private Viewport viewport;
    final Main main;
    final Game game;

    public GameScreen(Main main, Game game) {
        //constructor - get Game, initialize stuff
        //load textures, sounds
        this.main = main;
        this.game = game;
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        // Create a perspective camera with some sensible defaults
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 15f, 10f);
        camera.lookAt(0f, 2, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        actors = new Array<>();

        actors.add(new Star(game, -4, -2, 0));
        actors.add(new Planet(game, 4, -2, 0));
        actors.add(new Ranger(game, 12, 0, 0, game.getPlayer1()));

//        cameraController = new CameraInputController(camera);
//        Gdx.input.setInputProcessor(cameraController);
        viewport = new ScreenViewport(camera);
        viewport.setCamera(camera);
    }

    @Override
    public void render(float delta) {
//        cameraController.update();

        // Clear the stuff that is left over from the previous render cycle
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Let our ModelBatch take care of efficient rendering of our ModelInstance
        modelBatch.begin(camera);
        for (Actor actor: actors) modelBatch.render(actor.getInstance(), environment);
        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.update();
        viewport.update(width, height, false);
    }

    @Override
    public void show() {
        //when screen is shown
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        //dispose of all resources
        modelBatch.dispose();
    }
}
