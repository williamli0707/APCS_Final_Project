package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

/**
 * The screen to be displayed on login.
 * @author William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public class LoginScreen implements Screen {

    final Main game;
    private LoginValidator loginValidator;
    private Stage stage;
    private Viewport viewport;
    private Image bg;

    /**
     * Constructor. Creates UI, textures, etc.
     * @param game the game this screen is part of
     */
    public LoginScreen(Main game) {
        this.game = game;
        stage = new Stage();
        viewport = new ScreenViewport();
        stage.setViewport(viewport);
        VisUI.load(); //
        loginValidator = new LoginValidator(1280, 720, game);
        bg = new Image(new Texture(Gdx.files.internal("crab_nebula.jpg")));
        bg.setBounds(0, 0, 1280, 720);
        stage.addActor(bg);
        stage.addActor(loginValidator);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders the screen.
     * @param delta the time since the last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Called when the window is resized.
     * @param width the new width
     * @param height the new height
     */
    @Override
    public void resize(int width, int height) {
        loginValidator.resize(width, height);
        viewport.update(width, height, true);
    }

    /**
     * default library required method
     */
    @Override
    public void show() {
        //when screen is shown
    }

    /**
     * default library required method
     */
    @Override
    public void hide() {

    }

    /**
     * default library required method
     */
    @Override
    public void pause() {
    }

    /**
     * default library required method
     */
    @Override
    public void resume() {
    }

    /**
     * Called when the screen is disposed.
     */
    @Override
    public void dispose() {
        VisUI.dispose();
        stage.dispose();
    }
}
