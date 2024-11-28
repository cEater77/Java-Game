package org.main;

import Engine.Window;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Application {

    public void init() {
        System.out.println("Initializing App...");
        window = new Window(800, 800, "Java Game");
        GL.createCapabilities();
        //test
    }

    public void run() {
        System.out.println("Running App...");
        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);



            glfwSwapBuffers(window.getNativeWindow());
            glfwPollEvents();
        }
    }

    public void deinit() {
        System.out.println("Deinitializing App...");
        window.close();
    }

    private Window window;
}
