package Engine.renderer;

import org.joml.*;

public class Vertex {
    Vertex(Vector3f position, Vector2f offset, Vector2f uv, float alpha, float isHighlighted)
    {
        this.position = position;
        this.uv = uv;
        this.offset = offset;
        this.alpha = alpha;
        this.isHighlighted = isHighlighted;
    }
    public Vector3f position;
    public Vector2f offset;
    public Vector2f uv;
    public float alpha;
    // boolean isHighlighted
    public float isHighlighted = 0.0f;

    public final static int VERTEX_SIZE = 9;
}
