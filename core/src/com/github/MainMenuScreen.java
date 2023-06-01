package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.kotcrab.vis.ui.VisUI;

public class MainMenuScreen implements Screen {

    final Main game;
    private Stage stage;
    private TextButton newSinglePlayerGameButton, logout;
    private Label stats;
    private Image logo, bg;

    public MainMenuScreen(final Main game) {
        //constructor - get Game, initialize stuff
        //load textures, sounds
        VisUI.load();
        this.game = game;
        stage = new Stage(new ScalingViewport(Scaling.stretch, 1280, 720));
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));

//        Label test = new Label("testing", VisUI.getSkin());
        newSinglePlayerGameButton = new TextButton("Start New Single Player Game", skin);
        logout = new TextButton("Logout", skin);
        stats = new Label("Stats: \n" +
                "Troops Killed: " + PlayerData.kills + "\n" +
                "Stars Conquered: " + PlayerData.stars + "\n" +
                "Games Played: " + PlayerData.games + "\n" +
                "Games Won: " + PlayerData.wins, VisUI.getSkin());
        bg = new Image(new Texture(Gdx.files.internal("nebula_background.jpg")));

        newSinglePlayerGameButton.setBounds(640 - 150, 360 - 25, 300, 50);
        logout.setPosition(640 - logout.getWidth() / 2, 360 - logout.getHeight() / 2 - 60);
        stats.setPosition(0, 0);
        bg.setBounds(0, 0, 1280, 720);

        newSinglePlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.gameScreen();
            }
        });

        logout.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.loginScreen();
            }
        });

        stage.addActor(bg);
        stage.addActor(newSinglePlayerGameButton);
        stage.addActor(stats);
        stage.addActor(logout);
//        stage.addActor(test);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
    }
}
