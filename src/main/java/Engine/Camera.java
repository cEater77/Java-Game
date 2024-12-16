package Engine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.main.Game;

public class Camera {
    private Vector3f isometricPosition = new Vector3f();
    private float speed = 1.0f/15.0f;

    public Camera(Vector3f isometricPosition)
    {
        this.isometricPosition = isometricPosition;
    }

    public void init()
    {

    }

    public void update()
    {
        Vector2f camPositionDelta = new Vector2f(0.0f,0.0f);
        if(GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            camPositionDelta.y += speed;

        if(GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            camPositionDelta.y -= speed;

        if(GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
            camPositionDelta.x += speed;

        if(GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
            camPositionDelta.x -= speed;

        if(camPositionDelta.x != 0.0f || camPositionDelta.y != 0.0f)
            camPositionDelta.normalize(speed);
        isometricPosition.x += camPositionDelta.x;
        isometricPosition.y += camPositionDelta.y;
    }

    public void setIsometricPosition(Vector3f isometricPosition) {
        this.isometricPosition = isometricPosition;
    }

    public Vector3f getIsometricPosition() {
        return new Vector3f(isometricPosition);
    }
}
