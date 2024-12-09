package Engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    private final Map<String, Shader> shaders = new HashMap<String, Shader>();

    public void init()
    {

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
