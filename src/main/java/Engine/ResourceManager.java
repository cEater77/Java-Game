package Engine;

import Engine.renderer.Shader;
import Engine.renderer.Texture;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL33;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.json.*;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.sound.sampled.AudioInputStream;
import java.nio.file.*;


public class ResourceManager {

    private final Map<String, Shader> shaders = new HashMap<String, Shader>();
    private final Map<String, Texture> textures = new HashMap<String, Texture>();
    private final Map<String, AudioInputStream> sounds = new HashMap<>();

    private int textureAtlasID;

    public void init()
    {
        ByteBuffer imageBuffer;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Load the image file into a ByteBuffer
            IntBuffer widthBuffer = stack.mallocInt(1), heightBuffer = stack.mallocInt(1), channelBuffer = stack.mallocInt(1);
            STBImage.nstbi_set_flip_vertically_on_load(1);
            imageBuffer = STBImage.stbi_load("assets/texture/texture_atlas.png", widthBuffer, heightBuffer, channelBuffer, 4);
            if (imageBuffer == null) {
                throw new RuntimeException("Failed to load texture atlas");
            }
            width = widthBuffer.get();
            height = heightBuffer.get();
        }

        // Generate a new texture ID
        textureAtlasID = GL33.glGenTextures();
        GL33.glActiveTexture(GL33.GL_TEXTURE0 + 0);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureAtlasID);
        // Set texture parameters (wrapping and filtering)
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_S, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_T, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);

        // Load the image data into the texture
        GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, width, height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, imageBuffer);

        GL33.glActiveTexture(GL33.GL_TEXTURE0 + 15);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);

        JSONArray textureDefs = new JSONObject(getStringFromFile("assets/texture/textureDefs.json")).getJSONArray("textures");
        for (int i = 0; i < textureDefs.length(); i++) {
            JSONObject texture = textureDefs.getJSONObject(i);
            Vector2f uvPos = new Vector2f((float) texture.getJSONArray("position").getInt(0) / width, (float) texture.getJSONArray("position").getInt(1) / height );
            Vector2f uvSize = new Vector2f((float)Texture.DEFAULT_TEXTURE_WIDTH / width, (float)Texture.DEFAULT_TEXTURE_HEIGHT / height);
            this.textures.put(texture.getString("name"), new Texture(uvPos, uvSize));
        }
    }

    public void loadShader(String name, String vertexPath, String fragmentPath)
    {
        String vertexCode = getStringFromFile(vertexPath);

        String fragmentCode = getStringFromFile(fragmentPath);

        this.shaders.putIfAbsent(name, new Shader(vertexCode, fragmentCode));
    }

    public void loadSounds(String name, Path soundFile)
    {

    }

    public Shader getShader(String name)
    {
        return this.shaders.get(name);
    }
    public Texture getTexture(String name){return this.textures.get(name);}

    private String getStringFromFile(String path)
    {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
