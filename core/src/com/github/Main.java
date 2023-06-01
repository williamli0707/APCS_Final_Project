package com.github;

import com.badlogic.gdx.Gdx;

/**
 * The main class of the game.
 * @author William Li
 * @version 6/7/23
 * @author Period 5
 * @author Sources - None
 */
public class Main extends com.badlogic.gdx.Game {

	/**
	 * "Constructor" for the Main class. Attempts to make a custom configuration (for debug purposes)
	 * and then starts the game.
	 */
	@Override
	public void create() {
//		try {
//			Scanner in = new Scanner(new File("config.txt"));
//
//			Ranger.HEALTH = in.nextFloat();
//			Ranger.DAMAGE = in.nextFloat();
//			Ranger.SPEED = in.nextFloat();
//			Ranger.RANGE = in.nextFloat();
//			Ranger.COST = in.nextFloat();
//
//			Vanguard.HEALTH = in.nextFloat();
//			Vanguard.DAMAGE = in.nextFloat();
//			Vanguard.SPEED = in.nextFloat();
//			Vanguard.RANGE = in.nextFloat();
//			Vanguard.COST = in.nextFloat();
//
//			Aegis.HEALTH = in.nextFloat();
//			Aegis.DAMAGE = in.nextFloat();
//			Aegis.SPEED = in.nextFloat();
//			Aegis.RANGE = in.nextFloat();
//			Aegis.COST = in.nextFloat();
//
//			Mothership.health = in.nextFloat();
//			Mothership.speed = in.nextFloat();
//
//			SinglePlayerGame.HOME_STAR_HEALTH = in.nextFloat();
//
//			Player.RESOURCE_START = in.nextInt();
//
//			System.err.println("using custom configuration");
//
//		} catch (Exception ignore) {
////			ignore.printStackTrace();
//		}
		loginScreen();
	}

	/**
	 * Called every tick. Renders the screen.
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Called when the game is closed. Disposes of the screen.
	 */
	@Override
	public void dispose() {

	}

	/**
	 * Called when the window is resized.
	 * @param width the new width in pixels
	 * @param height the new height in pixels
	 */
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	/**
	 * default library required method
	 */
	@Override
	public void pause() { }

	/**
	 * default library required method
	 */
	@Override
	public void resume() { }

	/**
	 * Creates a new login screen and sets it as the current screen.
	 */
	public void loginScreen() {
		if(getScreen() != null) getScreen().dispose();
		setScreen(new LoginScreen(this));
	}

	/**
	 * Creates a new main menu screen and sets it as the current screen.
	 */
	public void menuScreen() {
		if(getScreen() != null) getScreen().dispose();
		setScreen(new MainMenuScreen(this));
	}

	/**
	 * Creates a new game screen and sets it as the current screen.
	 */
	public void gameScreen() {
		if(getScreen() != null) getScreen().dispose();
		GameScreen screen = new GameScreen(this);
		this.setScreen(screen);
		Gdx.input.setInputProcessor(screen);
	}

	/**
	 * Creates a new victory screen and sets it as the current screen.
	 */
	public void victory() {
		if(getScreen() != null) getScreen().dispose();
		PlayerData.add(0, 0, 0, 1);
		setScreen(new VictoryScreen(this));
	}

	/**
	 * Creates a new defeat screen and sets it as the current screen.
	 */
	public void defeat() {
		if(getScreen() != null) getScreen().dispose();
		setScreen(new DefeatScreen(this));
	}
}