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

public class LoginScreen implements Screen {

    final Main game;
    private LoginValidator loginValidator;
    private Stage stage;
    private Viewport viewport;
    private Image bg;

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

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        loginValidator.resize(width, height);
        viewport.update(width, height, true);
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
        VisUI.dispose();
        stage.dispose();
    }
}
