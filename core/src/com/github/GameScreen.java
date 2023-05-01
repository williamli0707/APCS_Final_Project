package com.github;

import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {

    final Main game;

    public GameScreen(Main game) {
        //constructor - get Game, initialize stuff
        //load textures, sounds
        this.game = game;
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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
    }
}
