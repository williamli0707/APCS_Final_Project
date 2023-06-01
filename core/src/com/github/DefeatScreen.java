package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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

/**
 * The screen to be displayed when the player loses the game.
 * @author William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public class DefeatScreen implements Screen {
    private Sprite bg;
    private SpriteBatch batch;
    private Stage stage;
    private VisTextButton mainMenu;

    /**
     * Constructor for the defeat screen. Creates a viewport, adds UI, and loads textures.
     * @param game the game this screen is a part of
     */
    public DefeatScreen(final Main game) {
        VisUI.load();
        bg = new Sprite(new Texture(Gdx.files.internal("Defeat.png")));
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

    /**
     * Called when the screen is rendered. Draws the background and the UI.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        batch.begin();
        bg.draw(batch);
        batch.end();
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    /**
     * Called when the screen is showed.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the window is resized.
     * @param width the new width of the window
     * @param height the new height of the window
     */
    @Override
    public void resize(int width, int height) {
        bg.setBounds(0, 0, 1280, 720);
        stage.getViewport().update(width, height);
        mainMenu.setPosition(640, 50, Align.center);
    }

    /**
     * not sure what this is, default library required method
     */
    @Override
    public void pause() {

    }

    /**
     * not sure what this is, default library required method
     */
    @Override
    public void resume() {

    }

    /**
     * not sure what this is, default library required method
     */
    @Override
    public void hide() {

    }

    /**
     * Called when the screen is disposed.
     */
    @Override
    public void dispose() {
        VisUI.dispose();
    }
}
