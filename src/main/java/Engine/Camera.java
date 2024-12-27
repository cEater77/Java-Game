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

    }

    public void setIsometricPosition(Vector3f isometricPosition) {
        this.isometricPosition = isometricPosition;
    }

    public Vector3f getIsometricPosition() {
        return new Vector3f(isometricPosition);
    }
}
