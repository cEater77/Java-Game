package org.main;

import Engine.*;
import Engine.renderer.Renderer;
import Engine.renderer.Texture;
import org.joml.Vector3f;
import org.main.GameObjects.Block;
import org.main.GameObjects.Player;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Game {

    public void init() {

        System.out.println("Initializing Game");
        window = new Window(800, 800, "Java Game");

        resourceManager = new ResourceManager();
        resourceManager.init();

        resourceManager.loadShader("default","assets/shader/default.vert","assets/shader/default.frag" );

        renderer = new Renderer();
        renderer.init(resourceManager.getShader("default"));

        uiManager = new UIManager();
        uiManager.init(window.getNativeWindow());

        levelBuilder = new LevelBuilder(renderer,resourceManager);

        registerBlocks();
    }

    public void run() {
        System.out.println("Running Game...");


        Level level = new Level(renderer, resourceManager, "test");
        changeLevel(level);

        //Level level = levelBuilder.getLevel("test.bin");

        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            window.beginFrame();

            level.tick();

            renderer.renderBatch();
            uiManager.update();

            window.endFrame();
        }
        //levelBuilder.saveLevel(level);
    }

    public void deinit() {
        System.out.println("Deinitializing Game");
        window.close();
    }

    private void changeLevel(Level level)
    {
        List<Texture> frames = new ArrayList<>();
        frames.add(resourceManager.getTexture("snow_grass"));
        frames.add(resourceManager.getTexture("snow_grass"));
        frames.add(resourceManager.getTexture("snow_grass"));
        frames.add(resourceManager.getTexture("snow_grass"));
        frames.add(resourceManager.getTexture("snow_grass"));
        frames.add(resourceManager.getTexture("snow_grass"));
        frames.add(resourceManager.getTexture("snow_grass"));
        level.addGameObject(new Player(new Vector3f(0.0f), frames));
    }

    private void registerBlocks()
    {
        Block woodBlock = new Block(new Vector3f(0.0f), true, Block.BlockTypeID.WOOD);


    }

    public static Window window;
    private ResourceManager resourceManager;
    private Renderer renderer;
    public static UIManager uiManager;
    public LevelBuilder levelBuilder;
    private GameState gamestate;

    private enum GameState
    {
        START_MENU,
        SELECTION,
        PAUSE_MENU,
        IN_GAME,
        LEVEL_COMPLETE
    }
}
