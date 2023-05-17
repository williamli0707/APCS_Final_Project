package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.github.game.Mothership;
import com.github.game.Player;
import com.github.game.Star;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class GameScreen implements Screen {
    private SceneManager sceneManager;
    public PerspectiveCamera camera;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private DirectionalLightEx light;

    private ModelBatch batch;
    private SpriteBatch spriteBatch;
    private Star star;
    Texture background;
    private Mothership mothership;
    final Main main;
    final Game game;

    public GameScreen(Main main, Game game) {
        //constructor - get Game, initialize stuff
        //load textures, sounds
        this.main = main;
        this.game = game;

        sceneManager = new SceneManager();
        // create scene
        camera = new PerspectiveCamera(50f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 5f, -5);
        camera.lookAt(0f, 0, 0);
        camera.near = 1f;
        camera.far = 100f;
        sceneManager.setCamera(camera);

        mothership = new Mothership(game, 0, 0, 0, new Player(game), this);
        Gdx.input.setInputProcessor(mothership);
        sceneManager.addScene(mothership.getScene());

        // setup light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(256);
//        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
//        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));
//        sceneManager.environment.set(new ColorAttribute(ColorAttribute.Fog, Color.WHITE));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

        batch = new ModelBatch();
        spriteBatch = new SpriteBatch();
        star = new Star(null, 0, 6);
        background = new Texture(Gdx.files.internal("space.jpeg"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        sceneManager.update(delta);
        sceneManager.render();

        mothership.act(delta);

        batch.begin(camera);
        batch.render(star.getInstance(), sceneManager.environment);
        batch.end();
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
}
