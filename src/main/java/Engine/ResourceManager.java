package Engine;

import org.lwjgl.opengl.GL33;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;


public class ResourceManager {

    private final Map<String, Shader> shaders = new HashMap<String, Shader>();
    private int textureAtlasID;

    public void init()
    {
        ByteBuffer imageBuffer;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Load the image file into a ByteBuffer
            imageBuffer = STBImage.stbi_load("assets/texture/texture_atlas.png", stack.mallocInt(1), stack.mallocInt(1), stack.mallocInt(1), 4);
            if (imageBuffer == null) {
                throw new RuntimeException("Failed to load texture atlas");
            }

            // Get the width and height of the image
            width = stack.mallocInt(1).get(0);
            height = stack.mallocInt(1).get(0);
        }

        // Generate a new texture ID
        textureAtlasID = GL33.glGenTextures();
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureAtlasID);
        GL33.glActiveTexture(GL33.GL_TEXTURE0 + 0);
        // Set texture parameters (wrapping and filtering)
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_S, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_T, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);

        // Load the image data into the texture
        GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, width, height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, imageBuffer);

        // Generate MipMaps
        GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);
    }

    public void loadShader(String name, String vertexPath, String fragmentPath)
    {
        String vertexCode = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(vertexPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                vertexCode = vertexCode.concat(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fragmentCode = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(fragmentPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fragmentCode = fragmentCode.concat(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.shaders.putIfAbsent(name, new Shader(vertexCode, fragmentCode));
    }

    public Shader getShader(String name)
    {
        return this.shaders.get(name);
    }

}
