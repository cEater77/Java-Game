package Engine;

import Engine.renderer.Shader;
import Engine.renderer.Texture;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL33;

import java.io.IOException;
import java.io.InputStream;
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

    public ResourceManager() {
        ByteBuffer imageBuffer;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Lade die Bilddatei in ein buffer
            IntBuffer widthBuffer = stack.mallocInt(1), heightBuffer = stack.mallocInt(1), channelBuffer = stack.mallocInt(1);
            STBImage.nstbi_set_flip_vertically_on_load(1);
            imageBuffer = STBImage.stbi_load(getResourcePath("assets/texture/texture_atlas.png"), widthBuffer, heightBuffer, channelBuffer, 4);
            if (imageBuffer == null) {
                throw new RuntimeException("Failed to load texture atlas");
            }
            width = widthBuffer.get();
            height = heightBuffer.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // erstelle eine OpenGL texture
        textureAtlasID = GL33.glGenTextures();
        GL33.glActiveTexture(GL33.GL_TEXTURE0 + 0);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureAtlasID);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_S, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_T, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);

        // Lade den buffer in die OpenGL texture rein.
        GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, width, height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, imageBuffer);

        GL33.glActiveTexture(GL33.GL_TEXTURE0 + 15);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);

        // Lade die subtexturen, wie Blocktexturen oder Spielerframes, von der JSON datei, rein.
        JSONArray textureDefs = new JSONObject(getStringFromFile("assets/texture/texture_defs.json")).getJSONArray("textures");
        for (int i = 0; i < textureDefs.length(); i++) {
            JSONObject texture = textureDefs.getJSONObject(i);
            JSONObject texturePlace = texture.getJSONObject("frame");
            Vector2f uvPos = new Vector2f((float) texturePlace.getInt("x") / width, (float) texturePlace.getInt("y") / height);
            Vector2f uvSize = new Vector2f((float) texturePlace.getInt("w") / width, (float) texturePlace.getInt("h") / height);
            this.textures.put(texture.getString("filename"), new Texture(uvPos, uvSize));
        }
    }

    public void loadShader(String name, String vertexPath, String fragmentPath) {
        String vertexCode = getStringFromFile(vertexPath);

        String fragmentCode = getStringFromFile(fragmentPath);

        this.shaders.putIfAbsent(name, new Shader(vertexCode, fragmentCode));
    }

    public void loadSounds(String name, Path soundFile) {

    }

    public Shader getShader(String name) {
        return this.shaders.get(name);
    }

    public Texture getTexture(String name) {
        return this.textures.get(name);
    }

    private static String getStringFromFile(String path) {
        try {
            path = getResourcePath(path);
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResourcePath(String resourcePath) throws IOException {
        Path filePath = Paths.get(resourcePath);

        // Check if the file exists normally on disk
        if (Files.exists(filePath)) {
            return filePath.toAbsolutePath().toString();
        }

        // Otherwise, try to load it from inside the JAR
        InputStream inputStream = ResourceManager.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        // Extract resource to a temporary file
        Path tempFile = Files.createTempFile("temp_texture", ".png");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        inputStream.close();

        return tempFile.toAbsolutePath().toString();
    }

}
