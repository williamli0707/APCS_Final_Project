package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;

public class LoginScreen implements Screen {

    final Main game;
    private LoginValidator loginValidator;
    private Stage stage;

    public LoginScreen(Main game) {
        //constructor - get Game, initialize stuff
        //load textures, sounds
        this.game = game;
        stage = new Stage();
        VisUI.load();
        loginValidator = new LoginValidator();
        loginValidator.setFillParent(true);
        stage.addActor(loginValidator);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        VisUI.dispose();
        stage.dispose();
    }
}
