package Engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL33;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public long getNativeWindow() {
        return nativeWindow;
    }

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        this.nativeWindow = glfwCreateWindow(width, height, title, 0, 0);
        if ( nativeWindow == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(nativeWindow, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(nativeWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(nativeWindow);
    }

    public void close(){
        glfwSetWindowShouldClose(nativeWindow, true);
    }

    public void update()
    {
        int[] temp_width = new int[1], temp_height = new int[1];
        glfwGetWindowSize(nativeWindow, temp_width, temp_height);
        width = temp_width[0];
        height = temp_height[0];
        GL33.glViewport(0,0, width, height);
        glfwSwapBuffers(nativeWindow);
        glfwPollEvents();
    }

    private int width, height;
    private String title;
    public static long nativeWindow;
}
