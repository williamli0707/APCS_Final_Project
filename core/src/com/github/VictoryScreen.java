package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class VictoryScreen implements Screen {
    private Sprite bg;
    private SpriteBatch batch;
    private Stage stage;
    private VisTextButton mainMenu;
    public VictoryScreen(final Main game) {
        VisUI.load();
        bg = new Sprite(new Texture(Gdx.files.internal("Victory.png")));
        batch = new SpriteBatch();
        stage = new Stage(new ExtendViewport(1280, 720));
        Gdx.input.setInputProcessor(stage);
        mainMenu = new VisTextButton("RETURN TO MAIN MENU");
        mainMenu.setPosition(640, 50, Align.center);
        mainMenu.setWidth(250);
        mainMenu.setHeight(50);
        mainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.menuScreen();
            }
        });
        stage.addActor(mainMenu);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        bg.draw(batch);
        batch.end();
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        bg.setBounds(0, 0, 1280, 720);
        stage.getViewport().update(width, height);
        mainMenu.setPosition(640, 50, Align.center);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }
}
