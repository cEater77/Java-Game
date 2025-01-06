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
        GL.createCapabilities();

        resourceManager = new ResourceManager();
        resourceManager.init();

        resourceManager.loadShader("default","assets/shader/default.vert","assets/shader/default.frag" );

        renderer = new Renderer();
        renderer.init(resourceManager.getShader("default"));

        uiManager = new UIManager();
        uiManager.init(window.getNativeWindow());
    }

    public void run() {
        System.out.println("Running Game...");

        World world = new World(renderer, resourceManager);

        while (!glfwWindowShouldClose(window.getNativeWindow())) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            world.tick();

            renderer.renderBatch();
            uiManager.update();
            window.update();
        }
    }

    public void deinit() {
        System.out.println("Deinitializing Game");
        window.close();
    }

    public static Window window;
    private ResourceManager resourceManager;
    private Renderer renderer;
    public static UIManager uiManager;
    private GameState gamestate;

    private enum GameState
    {
        START_MENU,
        SELECTION,
        IN_GAME
    }
}
