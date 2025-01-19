package Engine.renderer;

import org.joml.Vector2f;

public class Texture {

    public Texture(Vector2f pos, Vector2f size)
    {
        this.uvPosition = pos;
        this.uvSize = size;
    }
    public Vector2f uvPosition;
    public Vector2f uvSize;

    public static final int DEFAULT_TEXTURE_WIDTH = 32;
    public static final int DEFAULT_TEXTURE_HEIGHT = 32;
    public static final Texture EMPTY_TEXTURE = new Texture(new Vector2f(0.0f, 0.0f), new Vector2f(0.0f,0.0f));

}
