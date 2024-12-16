package org.main;

import Engine.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Game {

    public void init() {

        System.out.println("Initializing App");
        window = new Window(800, 800, "Java Game");
        GL.createCapabilities();

        resourceManager = new ResourceManager();
        resourceManager.init();

        resourceManager.loadShader("default","assets/shader/default.vert","assets/shader/default.frag" );

        renderer = new Renderer();
        renderer.init(resourceManager.getShader("default"));

        uiManager = new UIManager();
        uiManager.init(window.getNativeWindow());

        camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f));
        camera.init();
    }

    public void run() {
        System.out.println("Running App...");

        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //renderer.renderBackground();
            for (int i = -12; i < 13; i++) {
                for (int j = -12; j < 13; j++) {
                    renderer.renderTile(new Vector3f(2.0f + (float) j, 2.0f + (float) i, 0.0f), resourceManager.getTexture("grass"));
                }
            }

            renderer.renderSprite(camera.getIsometricPosition().negate(), new Vector2f(20.0f), resourceManager.getTexture("snow_grass"));

            camera.update();
            renderer.renderBatch();

            uiManager.update();
            window.update();
        }
    }

    public void deinit() {
        System.out.println("Deinitializing App");
        window.close();
    }

    public static Window window;
    private ResourceManager resourceManager;
    private Renderer renderer;
    public static UIManager uiManager;
    public static Camera camera;
}
