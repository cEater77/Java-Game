package Engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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

        // Make the OpenGL context current
        glfwMakeContextCurrent(nativeWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(nativeWindow);

        GL.createCapabilities();
    }

    public void close(){
        glfwSetWindowShouldClose(nativeWindow, true);
    }

    public int getFps()
    {
        return fps;
    }

    public float getLastFrameDuration()
    {
        return lastFrameDurationSec;
    }

    public void beginFrame()
    {
        frameBeginSec = System.nanoTime();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void endFrame()
    {
        if(!isFullScreen)
        {
            int[] temp_width = new int[1], temp_height = new int[1], temp_x = new int[1], temp_y = new int[1];
            glfwGetWindowSize(nativeWindow, temp_width, temp_height);
            glfwGetWindowPos(nativeWindow, temp_x, temp_y);
            width = temp_width[0];
            height = temp_height[0];
            xPosition = temp_x[0];
            yPosition = temp_y[0];
            viewportWidth = width;
            viewportHeight = height;
        }

        timePassedInSec += lastFrameDurationSec;
        totalGameTimeInSec += lastFrameDurationSec;
        currentFrameCount++;
        if(timePassedInSec > 1.0f)
        {
            fps = currentFrameCount;
            timePassedInSec = 0;
            currentFrameCount = 0;
        }

        GL33.glViewport(0,0, viewportWidth, viewportHeight);
        glfwSwapBuffers(nativeWindow);
        glfwPollEvents();

        if (frameBeginSec == 0) {
            lastFrameDurationSec = 0;
        } else {
            lastFrameDurationSec = (System.nanoTime() - frameBeginSec) / 1_000_000_000.0f;
        }
    }

    public void toggleFullscreen() {
        isFullScreen = !isFullScreen;

        if (isFullScreen) {
            long monitor = glfwGetPrimaryMonitor();
            GLFWVidMode videoMode = glfwGetVideoMode(monitor);
            glfwSetWindowMonitor(nativeWindow, monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate());
            viewportWidth = videoMode.width();
            viewportHeight = videoMode.height();
        } else {
            glfwSetWindowMonitor(nativeWindow, 0, xPosition, yPosition, width, height, 0);
        }
    }

    private int width, height;
    private int xPosition, yPosition;
    private String title;
    public static long nativeWindow;

    private int viewportWidth, viewportHeight;

    private int currentFrameCount;
    private float timePassedInSec;
    private long frameBeginSec;

    private int fps;
    private float lastFrameDurationSec;
    private float totalGameTimeInSec;

    private boolean isFullScreen = false;
}
