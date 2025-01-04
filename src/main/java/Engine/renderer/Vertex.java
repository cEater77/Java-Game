package Engine.renderer;

import org.joml.*;

public class Vertex {
    Vertex(Vector3f position, Vector2f offset, Vector2f uv)
    {
        this.position = position;
        this.uv = uv;
        this.offset = offset;
    }
    public Vector3f position;
    public Vector2f offset;
    public Vector2f uv;

    public final static int VERTEX_SIZE = 7;
}
