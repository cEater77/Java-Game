package Engine;

import org.lwjgl.opengl.GL33;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.json.*;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;


public class ResourceManager {

    private final Map<String, Shader> shaders = new HashMap<String, Shader>();
    private final Map<String, Texture> textures = new HashMap<String, Texture>();

    private int textureAtlasID;

    public void init()
    {
        ByteBuffer imageBuffer;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Load the image file into a ByteBuffer
            IntBuffer widthBuffer = stack.mallocInt(1), heightBuffer = stack.mallocInt(1), channelBuffer = stack.mallocInt(1);
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

        JSONArray textureDefs = new JSONObject(getStringFromFile("assets/texture/textureDefs.json")).getJSONArray("texture_defs");
        for(int i = 0; i < textureDefs.length(); i++)
        {

        }

    }

    public void loadShader(String name, String vertexPath, String fragmentPath)
    {
        String vertexCode = getStringFromFile(vertexPath);

        String fragmentCode = getStringFromFile(fragmentPath);

        this.shaders.putIfAbsent(name, new Shader(vertexCode, fragmentCode));
    }

    public Shader getShader(String name)
    {
        return this.shaders.get(name);
    }

    private String getStringFromFile(String path)
    {
        String content = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            content = reader.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
