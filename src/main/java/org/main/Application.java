package org.main;

import Engine.ResourceManager;
import Engine.Shader;
import Engine.Window;
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
    }

    public void run() {
        System.out.println("Running App...");

        float[] vertices = new float[]{
                -0.5f,-0.5f,0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.0f, 1.0f, 1.0f,
                -0.5f,-0.5f,0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.0f, 1.0f, 1.0f
        };

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        int VAO = GL33.glGenVertexArrays();
        GL33.glBindVertexArray(VAO);

        int VBO = GL33.glGenBuffers();
       // int EBO = GL33.glGenBuffers();

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, VBO);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, verticesBuffer, GL33.GL_STATIC_DRAW); // Adjust size as needed
        memFree(verticesBuffer);

        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 5 * Float.BYTES, 0); // Position
        GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES); // Texture coords

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            resourceManager.getShader("default").use();
            GL33.glBindVertexArray(VAO);

            GL33.glDrawArrays(GL_TRIANGLES, 0, 6);

            window.update();
        }
    }

    public void deinit() {
        System.out.println("Deinitializing App");
        window.close();
    }

    private Window window;
    private ResourceManager resourceManager;
}
