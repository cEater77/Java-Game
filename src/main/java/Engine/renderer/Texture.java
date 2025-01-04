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

}
