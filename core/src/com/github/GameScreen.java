package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.game.*;
import com.kotcrab.vis.ui.VisUI;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.EnvironmentUtil;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import java.util.HashSet;

public class GameScreen implements Screen, InputProcessor {
    /**
     * Manages all of the models in the game
     */
    public SceneManager sceneManager;
    /**
     * The main 3D camera
     */
    public Camera camera;
    private final ModelBatch batch;
    private final SpriteBatch spriteBatch;
    /**
     * Used to changing screens
     */
    public final Main main;
    private final SinglePlayerGame game;
    private final FitViewport miniMapViewport, mapViewport;
    private static final ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);
    /**
     * Textures on the map
     */
    public final Texture playerMinimapRegion, minimapRegion, minimapOutline, starFriendly, starHostile, r_friendly, r_hostile, v_friendly, v_hostile, a_friendly, a_hostile, selectionRegion, r_friendly_outline, v_friendly_outline, a_friendly_outline;
    private NinePatchDrawable arrow;
    private final NinePatch hpBorderPatch, hpBarPatch, arrowPatch;
    private Stage stage;
    private float mapFactor = 1;
    private boolean zoomMinimap = false, showMinimap = true, showMap = false, selection = false, showArrow = false;
    /**
     * The offset of the minimap used for calculating position
     */
    public static final int verticalOffset = 100, horizontalOffset = 0;
    private Label healthText, starHealthText, resourcesText, gameStatusText, fpsCount, entityCount;
    private Image hpMothershipBorder, hpMothershipBar, hpStarBorder, hpStarBar;
    private Vector2 selectionStart = new Vector2(0, 0), selectionEnd = new Vector2(0, 0), arrowBegin = new Vector2(0, 0), arrowEnd = new Vector2(0, 0);
    private HashSet<Troop> selectedTroops;
    private int mode = 0, tick = 0, fpsAvg = 0;
    public int entities = 0;

    /**
     * Constructor for GameScreen. Initializes a game, all textures, the environment, and the camera.
     * @param main
     */
    public GameScreen(Main main) {
        //constructor - get Game, initialize stuff
        //load textures, sounds
        this.main = main;
        selectedTroops = new HashSet<>();

        //initializing - would like a progress bar in the future
        System.err.println("beginning initialization");
        Aegis.init();
        System.err.println("Aegis initialization complete");
        Vanguard.init();
        System.err.println("Vanguard initialization complete");
        Ranger.init();
        System.err.println("Ranger initialization complete");
        VisUI.load();

        //3D setup
        sceneManager = new SceneManager();

        camera = new PerspectiveCamera(70f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.position.set(0f, 3f, -5);
        camera.lookAt(0f, 0, 0);
        camera.near = 1f;
        camera.far = 500f;
        sceneManager.setCamera(camera);

        batch = new ModelBatch();


        //Environment - I understand ~0.5% of this code
        DirectionalLightEx light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        Cubemap environmentCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(),
                "skybox-textures/space_", ".png", EnvironmentUtil.FACE_NAMES_NEG_POS);
        Cubemap specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        Texture brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));

        SceneSkybox skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

//        sceneManager.setShaderProvider(new CustomPBRShaderProvider(game));


        //Minimap and Map textures
        spriteBatch = new SpriteBatch();

        miniMapViewport = new FitViewport(800, 800);
        miniMapViewport.getCamera().position.set(0, 100, 0);
        miniMapViewport.setScreenBounds(Gdx.graphics.getWidth() - 200, 0, 200, 200);

        mapViewport = new FitViewport(800, 800);
        mapViewport.getCamera().position.set(0, 100, 0);
        mapViewport.setScreenBounds(Gdx.graphics.getWidth() / 2 - 350, Gdx.graphics.getHeight() / 2 - 350, 700, 700);

        minimapRegion = new Texture(Gdx.files.internal("skybox-textures/space_negy.png"));
        minimapOutline = new Texture(Gdx.files.internal("border.png"));
        playerMinimapRegion = new Texture(Gdx.files.internal("circle_yellow.png"));
        starHostile = new Texture(Gdx.files.internal("circle_red.png"));
        starFriendly = new Texture(Gdx.files.internal("circle_blue.png"));
        r_friendly = new Texture(Gdx.files.internal("R_friendly.png"));
        r_hostile = new Texture(Gdx.files.internal("R_hostile.png"));
        r_friendly_outline = new Texture(Gdx.files.internal("R_friendly_outline.png"));
        v_friendly = new Texture(Gdx.files.internal("V_friendly.png"));
        v_hostile = new Texture(Gdx.files.internal("V_hostile.png"));
        v_friendly_outline = new Texture(Gdx.files.internal("V_friendly_outline.png"));
        a_friendly = new Texture(Gdx.files.internal("A_friendly.png"));
        a_hostile = new Texture(Gdx.files.internal("A_hostile.png"));
        a_friendly_outline = new Texture(Gdx.files.internal("A_friendly_outline.png"));
        selectionRegion = new Texture(Gdx.files.internal("selection_region.png"));
        arrowPatch = new NinePatch(new Texture(Gdx.files.internal("arrow.png")), 10, 145, 81, 81);
        arrow = new NinePatchDrawable(arrowPatch);

        //UI
        stage = new Stage(new FitViewport(1280, 720));
        healthText = new Label("Health", VisUI.getSkin());
        starHealthText = new Label("Home Star Health", VisUI.getSkin());
        resourcesText = new Label("Resources", VisUI.getSkin());
        gameStatusText = new Label("", VisUI.getSkin());
        fpsCount = new Label("FPS: 60", VisUI.getSkin());
        entityCount = new Label("Entity Count: ", VisUI.getSkin());
        healthText.setPosition(426.667f, 20, Align.center);
        starHealthText.setPosition(853.333f, 20, Align.center);
        resourcesText.setPosition(50, 30, Align.center);
        gameStatusText.setPosition(30, 690, Align.center);
        fpsCount.setPosition(1180, 690, Align.center);
        entityCount.setPosition(1180, 660, Align.center);
        stage.addActor(healthText);
        stage.addActor(starHealthText);
        stage.addActor(resourcesText);
        stage.addActor(gameStatusText);
        stage.addActor(fpsCount);
        stage.addActor(entityCount);
        hpBorderPatch = new NinePatch(new Texture(Gdx.files.internal("hp_border.png")), 10, 10, 10, 10);
        hpBarPatch = new NinePatch(new Texture(Gdx.files.internal("hp_bar.png")), 0, 0, 0, 0);

        hpMothershipBorder = new Image(hpBorderPatch);
        hpMothershipBar = new Image(hpBarPatch);
        hpMothershipBorder.setWidth(305);
        hpMothershipBorder.setHeight(30);
        hpMothershipBar.setWidth(300);
        hpMothershipBar.setHeight(25);
        hpMothershipBorder.setPosition(426.667f, 50, Align.center);
        hpMothershipBar.setPosition(426.667f, 50, Align.center);
        stage.addActor(hpMothershipBorder);
        stage.addActor(hpMothershipBar);

        hpStarBorder = new Image(hpBorderPatch);
        hpStarBar = new Image(hpBarPatch);
        hpStarBorder.setWidth(305);
        hpStarBorder.setHeight(30);
        hpStarBar.setWidth(300);
        hpStarBar.setHeight(25);
        hpStarBorder.setPosition(853.333f, 50, Align.center);
        hpStarBar.setPosition(853.333f, 50, Align.center);
        stage.addActor(hpStarBorder);
        stage.addActor(hpStarBar);


        this.game = new SinglePlayerGame(this);
    }

    @Override
    public void render(float delta) {
        tick++;
        fpsAvg += Math.round(1 / delta);

        //3D
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2); // * 2 because ??

        sceneManager.update(delta);
        sceneManager.render();

        lineRenderer.begin(camera.combined, GL30.GL_LINES);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        grid(-100, 100, -100, 100);
        lineRenderer.end();

        batch.begin(camera);
        for(Star star: game.getStars()) batch.render(star.getInstance(), sceneManager.environment);
        batch.end();

        game.act(delta);
        camera.update();


        //UI
        stage.act(delta);

        hpMothershipBar.setWidth(300 * (Math.max(0, game.getPlayer().getMothership().getHealth()) / Mothership.health));
        hpStarBar.setWidth(300 * (Math.max(0, ((HomeStar) game.getStars()[0]).getHealth()) / SinglePlayerGame.HOME_STAR_HEALTH));
        resourcesText.setText("Resources: " + Math.round(game.getPlayer().getResources()));
        if(tick % 100 == 0) {
            fpsCount.setText("FPS: " + Math.round(fpsAvg / 100f));
            fpsAvg = 0;
        }
        entityCount.setText("Entity Count: " + entities);

        if(game.getPlayer().getMothership().getHealth() <= 0 || ((HomeStar) game.getStars()[0]).getHealth() <= 0) game.screen.main.defeat();

        stage.draw();


        //Maps
        if(showMinimap) {
            if (zoomMinimap) mapFactor += delta * 3f;
            else mapFactor -= delta * 3f;
            if (mapFactor > 3) mapFactor = 3;
            if (mapFactor < 1) mapFactor = 1;
            miniMapViewport.apply();
            spriteBatch.setProjectionMatrix(miniMapViewport.getCamera().combined);
            spriteBatch.begin();
            spriteBatch.draw(minimapOutline, -(250 * mapFactor) - 30 + horizontalOffset, -(250 * mapFactor) - 30 + verticalOffset, 500 * mapFactor + 60, 500 * mapFactor + 60);
            spriteBatch.draw(minimapRegion, -(250 * mapFactor) + horizontalOffset, -(250 * mapFactor) + verticalOffset, 500 * mapFactor, 500 * mapFactor);
            Vector3 loc = game.getPlayer().getMothership().getLocation();
            spriteBatch.draw(playerMinimapRegion, -loc.x * 2.5f * mapFactor + horizontalOffset, loc.z * 2.5f * mapFactor + verticalOffset, 20 * mapFactor, 20 * mapFactor);
            for (Star star : game.getStars()) {
                loc = star.getLocation();
                if (star.getPlayer() == null)
                    spriteBatch.draw(starHostile, -loc.x * 2.5f * mapFactor + horizontalOffset, loc.z * 2.5f * mapFactor + verticalOffset, 10 * mapFactor, 10 * mapFactor);
                else
                    spriteBatch.draw(starFriendly, -loc.x * 2.5f * mapFactor + horizontalOffset, loc.z * 2.5f * mapFactor + verticalOffset, 12 * mapFactor, 12 * mapFactor);
            }
            spriteBatch.end();
        }
        else if(showMap) {
            mapViewport.apply();
            spriteBatch.setProjectionMatrix(mapViewport.getCamera().combined);
            spriteBatch.begin();
            spriteBatch.draw(minimapOutline, -(250) - 30 + horizontalOffset, -(250) - 30 + verticalOffset, 500 + 60, 500 + 60);
            spriteBatch.draw(minimapRegion, -(250) + horizontalOffset, -(250) + verticalOffset, 500, 500);
            Vector3 loc = game.getPlayer().getMothership().getLocation();
            if(selection) spriteBatch.draw(selectionRegion, selectionStart.x, selectionStart.y, selectionEnd.x - selectionStart.x, selectionEnd.y - selectionStart.y);
            spriteBatch.draw(playerMinimapRegion, -loc.x * 2.5f + horizontalOffset, loc.z * 2.5f + verticalOffset, 20, 20);
            for (Star star : game.getStars()) {
                float fac = star instanceof HomeStar ? 1.5f : 1;
                loc = star.getLocation();
                if (star.getPlayer() == null)
                    spriteBatch.draw(starHostile, -loc.x * 2.5f + horizontalOffset, loc.z * 2.5f + verticalOffset, 10 * fac, 10 * fac);
                else
                    spriteBatch.draw(starFriendly, -loc.x * 2.5f + horizontalOffset, loc.z * 2.5f + verticalOffset, 12 * fac, 12 * fac);
            }
            for(Troop troop: game.getTroops()) {
                if(troop instanceof Mothership) continue;
                if(!selectedTroops.contains(troop)) troop.getSprite().draw(spriteBatch);
                else {
                    loc = troop.getLocation();
                    if(troop instanceof Ranger) spriteBatch.draw(r_friendly_outline, -loc.x * 2.5f + GameScreen.horizontalOffset, loc.z * 2.5f + GameScreen.verticalOffset, 6, 6, 12, 12, 1, 1, -troop.angle, 0, 0, r_friendly_outline.getWidth(), r_friendly_outline.getHeight(), false, false);
                    else if(troop instanceof Vanguard) spriteBatch.draw(v_friendly_outline, -loc.x * 2.5f + GameScreen.horizontalOffset, loc.z * 2.5f + GameScreen.verticalOffset, 6, 6, 12, 12, 1, 1, -troop.angle, 0, 0, v_friendly_outline.getWidth(), v_friendly_outline.getHeight(), false, false);
                    else if(troop instanceof Aegis) spriteBatch.draw(a_friendly_outline, -loc.x * 2.5f + GameScreen.horizontalOffset, loc.z * 2.5f + GameScreen.verticalOffset, 6, 6, 12, 12, 1, 1, -troop.angle, 0, 0, a_friendly_outline.getWidth(), a_friendly_outline.getHeight(), false, false);
                }
            }
            if(showArrow) arrow.draw(spriteBatch, arrowBegin.x, arrowBegin.y, 0, 0, (float) Math.sqrt(Math.pow(arrowEnd.x - arrowBegin.x, 2) + Math.pow(arrowEnd.y - arrowBegin.y, 2)) * 12.6f, 252, 0.079365f, 0.079365f, arrowEnd.cpy().sub(arrowBegin).angleDeg());
            spriteBatch.end();
        }

    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);

        stage.getViewport().update(width, height);
        healthText.setPosition(426.667f, 20, Align.center);
        starHealthText.setPosition(853.333f, 20, Align.center);
        resourcesText.setPosition(50, 30, Align.center);
        gameStatusText.setPosition(30, 690, Align.center);
        fpsCount.setPosition(1180, 690, Align.center);
        entityCount.setPosition(1180, 660, Align.center);

        miniMapViewport.setScreenBounds(width - 200, 0, 200, 200);
        mapViewport.setScreenBounds(width / 2 - 350, height / 2 - 350, 700, 700);
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
        Troop.dispose();
        VisUI.dispose();
    }

    /**
     * Draws a line in 3D space from (x1, y1, z1) to (x2, y2, z2) with the given color.
     * @param x1 x coordinate of start
     * @param y1 y coordinate of start
     * @param z1 x coordinate of start
     * @param x2 x coordinate of end
     * @param y2 y coordinate of end
     * @param z2 z coordinate of end
     * @param r R value in RGB
     * @param g G value in RGB
     * @param b B value in RGB
     * @param a alpha value
     */
    public static void line(float x1, float y1, float z1,
                            float x2, float y2, float z2,
                            float r, float g, float b, float a) {
        lineRenderer.color(r, g, b, a);
        lineRenderer.vertex(x1, y1, z1);
        lineRenderer.color(r, g, b, a);
        lineRenderer.vertex(x2, y2, z2);
    }

    /**
     * Draws a grid in 3D space with in increments of 1 from (x1, y1) to (x2, y2)
     * @param x1 x coordinate of start
     * @param x2 x coordinate of end
     * @param y1 y coordinate of start
     * @param y2 y coordinate of end
     */
    public static void grid(int x1, int x2, int y1, int y2) {
        for (int x = x1; x <= x2; x++) {
            // draw vertical
            line(x, 0, y1,
                    x, 0, y2,
                    0, 1, 0, 0);
        }

        for (int y = y1; y <= y2; y++) {
            // draw horizontal
            line(x1, 0, -y,
                    x2, 0, -y,
                    0, 1, 0, 0);
        }
    }

    /**
     * Sets the status of the gameStatusText.
     * @param stat the status to set it to
     */
    public void setStatus (String stat) {
        gameStatusText.setText(stat);
    }

    @Override
    public boolean keyDown(int keycode) {
        game.getPlayer().getMothership().keyDown(keycode);
        if(keycode == 70) {
            zoomMinimap = true;
        }
        if(keycode == 61) {
            showMinimap = false;
            showMap = true;
            mode = 0;
            gameStatusText.setText("Press 1 to spawn a new Ranger, 2 for a new Vanguard, or 3 for a new Aegis. Drag a rectangular region to move your troops. ");
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        game.getPlayer().getMothership().keyUp(keycode);
        if(keycode == 70) {
            zoomMinimap = false;
        }
        if(keycode == 61) {
            showMinimap = true;
            showMap = false;
            selection = false;
            showArrow = false;
            gameStatusText.setText("");
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        game.getPlayer().getMothership().keyTyped(character);
        if(showMap) {
            if (character == '1') {
                gameStatusText.setText("Now placing a new Ranger (Cost: " + Ranger.COST + " resources). Click on the map, near a star, where you want to place the troop. ");
                System.out.println("switched to mode 1");
                if (mode == 1) mode = 0;
                else mode = 1;
            }
            if (character == '2') {
                gameStatusText.setText("Now placing a new Vanguard (Cost: " + Vanguard.COST + " resources). Click on the map, near a star, where you want to place the troop. ");
                System.out.println("switched to mode 2");
                if (mode == 2) mode = 0;
                else mode = 2;
            }
            if (character == '3') {
                gameStatusText.setText("Now placing a new Aegis (Cost: " + Aegis.COST + " resources). Click on the map, near a star, where you want to place the troop. ");
                System.out.println("switched to mode 3");
                if (mode == 3) mode = 0;
                else mode = 3;
            }
        }
        if(character == '4') {
            game.addTroop(new Ranger(game, game.getPlayer().getMothership().getLocation().cpy(), null));
        }
        if(character == '5') {
            game.addTroop(new Vanguard(game, game.getPlayer().getMothership().getLocation().cpy(), null));
        }
        if(character == '6') {
            game.addTroop(new Aegis(game, game.getPlayer().getMothership().getLocation().cpy(), null));
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            game.getPlayer().getMothership().touchDown(screenX, screenY, pointer, button);
        }
        Vector2 unp = mapViewport.unproject(new Vector2(screenX, screenY));
        if(selection) {
            if(unp.x < Math.min(selectionStart.x, selectionEnd.x) || unp.x > Math.max(selectionStart.x, selectionEnd.x) || unp.y < Math.min(selectionStart.y, selectionEnd.y) || unp.y > Math.max(selectionStart.y, selectionEnd.y)) selection = false;
            else {
                showArrow = true;
                arrowBegin = unp;
                arrowEnd = unp;
            }
        }
        else if(showMap) {
            if(unp.x < -300 || unp.x > 300 || unp.y < -300 || unp.y > 300) return true;
            selection = true;
            selectionStart = unp;
            selectionEnd = unp;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        game.getPlayer().getMothership().touchUp();
        if(selectionEnd.cpy().sub(selectionStart).len() < 5) selection = false;
        if(showArrow) {
            showArrow = false;
            selection = false;
            gameStatusText.setText("Moving " + selectedTroops.size() + " troops to your selection. ");
            for(Troop t : selectedTroops) {
                t.setDestination(new Vector2(-(arrowEnd.x - GameScreen.horizontalOffset) / 2.5f, (arrowEnd.y - GameScreen.verticalOffset) / 2.5f));
            }
            selectedTroops.clear();
        }
        else if(selection) {
            mode = 0;
            gameStatusText.setText("Drag your selection where you would like the troops to go. ");
            for(Troop t: game.getTroops()) {
                Vector2 loc = new Vector2(-t.getLocation().x * 2.5f + GameScreen.horizontalOffset, t.getLocation().z * 2.5f + GameScreen.verticalOffset);
                if(t.getPlayer() == game.getPlayer() && loc.x <= Math.max(selectionStart.x, selectionEnd.x) && loc.x >= Math.min(selectionStart.x, selectionEnd.x) && loc.y <= Math.max(selectionStart.y, selectionEnd.y) && loc.y >= Math.min(selectionStart.y, selectionEnd.y)) {
                    selectedTroops.add(t);
                }
            }
            if(selectedTroops.size() == 0) selection = false;
        }
        else if(showMap && mode != 0) {
            Vector2 loc = mapViewport.unproject(new Vector2(screenX, screenY));
            loc.x -= horizontalOffset;
            loc.x = -loc.x;
            loc.y -= verticalOffset;
            loc.scl(0.4f);
            System.out.println(loc);
            game.getPlayer().placeTroop(mode, new Vector3(loc.x, 0, loc.y));
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!selection) game.getPlayer().getMothership().touchDragged(screenX, screenY, pointer);
        else if(!showArrow) selectionEnd = mapViewport.unproject(new Vector2(screenX, screenY));
        else arrowEnd = mapViewport.unproject(new Vector2(screenX, screenY));
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


}
