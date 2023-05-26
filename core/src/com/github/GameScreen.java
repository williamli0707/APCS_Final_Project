package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.game.Aegis;
import com.github.game.Ranger;
import com.github.game.Star;
import com.github.game.Vanguard;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.EnvironmentUtil;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class GameScreen implements Screen, InputProcessor {
    public SceneManager sceneManager;
    public Camera camera;

    private final ModelBatch batch;
    private final SpriteBatch spriteBatch;
    final Main main;
    private final SinglePlayerGame game;
    private FitViewport mapViewport;
    private static final ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);
    private Texture playerMinimapRegion, minimapRegion;

    public GameScreen(Main main) {
        //constructor - get Game, initialize stuff
        //load textures, sounds
        this.main = main;
        System.err.println("beginning initialization");
        Aegis.init();
        System.err.println("Aegis initialization complete");
        Vanguard.init();
        System.err.println("Vanguard initialization complete");
        Ranger.init();
        System.err.println("Ranger initialization complete");

        sceneManager = new SceneManager();

        this.game = new SinglePlayerGame(this);
        camera = new PerspectiveCamera(70f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.position.set(0f, 3f, -5);
        camera.lookAt(0f, 0, 0);
        camera.near = 1f;
        camera.far = 500f;
        sceneManager.setCamera(camera);

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

        batch = new ModelBatch();
        spriteBatch = new SpriteBatch();

        mapViewport = new FitViewport(800, 800);
        mapViewport.getCamera().position.set(0, 100, 0);
        mapViewport.setScreenBounds(Gdx.graphics.getWidth() - 260, 20, 200, 200);

        minimapRegion = new Texture(Gdx.files.internal("skybox-textures/space_negy.png"));
        playerMinimapRegion = new Texture(Gdx.files.internal("player_test.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        sceneManager.updateViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
//        mapViewport.update(700, 700, false);

        mapViewport.apply();
        spriteBatch.setProjectionMatrix(mapViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(minimapRegion, 0, 0);
        Vector3 loc = game.getPlayer().getMothership().getLocation();
        spriteBatch.draw(playerMinimapRegion, loc.x, loc.z);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
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

    public static void line(float x1, float y1, float z1,
                            float x2, float y2, float z2,
                            float r, float g, float b, float a) {
        lineRenderer.color(r, g, b, a);
        lineRenderer.vertex(x1, y1, z1);
        lineRenderer.color(r, g, b, a);
        lineRenderer.vertex(x2, y2, z2);
    }

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


    @Override
    public boolean keyDown(int keycode) {
        game.getPlayer().getMothership().keyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        game.getPlayer().getMothership().keyUp(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        game.getPlayer().getMothership().keyTyped(character);
        if(character == '1') {
            game.addTroop(new Ranger(game, game.getPlayer().getMothership().getLocation(), null));
        }
        if(character == '2') {
            game.addTroop(new Vanguard(game, game.getPlayer().getMothership().getLocation(), null));
        }
        if(character == '3') {
            game.addTroop(new Aegis(game, game.getPlayer().getMothership().getLocation(), null));
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            game.getPlayer().getMothership().touchDown(screenX, screenY, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        game.getPlayer().getMothership().touchUp();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        game.getPlayer().getMothership().touchDragged(screenX, screenY, pointer);
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
