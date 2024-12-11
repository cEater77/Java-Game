package Engine;

import org.joml.*;

public class Vertex {
    Vertex(Vector2f pos, Vector2f uv)
    {
        position = pos;
        this.uv = uv;
    }
    public Vector2f position;
    public Vector2f uv;

    public final static int VERTEX_SIZE = 4;
}
