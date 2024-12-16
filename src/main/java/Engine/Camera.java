package Engine;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f();
    public Camera(Vector3f position)
    {
        this.position = position;
    }

    public void update()
    {

    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }
}
