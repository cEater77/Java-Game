package org.main;

import Engine.Renderer;
import Engine.ResourceManager;
import Engine.Shader;
import Engine.Window;
import org.joml.Vector2f;
import org.lwjgl.nuklear.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Application {

    public void init() {

        System.out.println("Initializing App");
        window = new Window(800, 800, "Java Game");
        GL.createCapabilities();

        resourceManager = new ResourceManager();
        resourceManager.init();

        resourceManager.loadShader("default","assets/shader/default.vert","assets/shader/default.frag" );

        renderer = new Renderer();
        renderer.init(resourceManager.getShader("default"));
    }

    public void run() {
        System.out.println("Running App...");

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            resourceManager.getShader("default").use();

            renderer.renderTile(new Vector2f(100.0f,100.0f), new Vector2f(50.0f,50.0f));
            renderer.renderTile(new Vector2f(100.0f,0.0f), new Vector2f(50.0f,25.0f));

            renderer.renderBatch();
            window.update();
        }
    }

    public void deinit() {
        System.out.println("Deinitializing App");
        window.close();
    }

    private Window window;
    private ResourceManager resourceManager;
    private Renderer renderer;
}
