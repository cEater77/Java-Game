package org.main;

import Engine.*;
import Engine.renderer.Renderer;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
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
    }

    public void run() {
        System.out.println("Running Game...");

        levelBuilder.loadLevel("a");
        Level level = levelBuilder.getLevel("test.bin");

        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            window.beginFrame();

            level.tick();

            renderer.renderBatch();
            uiManager.update();

            window.endFrame();
        }

        levelBuilder.saveLevel(level);
    }

    public void deinit() {
        System.out.println("Deinitializing Game");
        window.close();
    }

    public void createLevel()
    {

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
