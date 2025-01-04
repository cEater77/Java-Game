package Engine.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

import org.lwjgl.system.MemoryUtil;

import java.util.*;
import java.nio.FloatBuffer;

public class Shader {
    private final int programID;
    private Map<String, Integer> unifromLocationCache = new HashMap<>();

    // Constructor accepts the source code of the vertex and fragment shaders
    public Shader(String vertexSource, String fragmentSource) {
        // Compile shaders and link the program

        int vertexShaderID = compileShader(vertexSource, GL33.GL_VERTEX_SHADER);
        int fragmentShaderID = compileShader(fragmentSource, GL33.GL_FRAGMENT_SHADER);

        // Create the program and link shaders to it
        programID = GL33.glCreateProgram();
        GL33.glAttachShader(programID, vertexShaderID);
        GL33.glAttachShader(programID, fragmentShaderID);
        GL33.glLinkProgram(programID);

        // Check for program linking errors
        if (GL33.glGetProgrami(programID, GL33.GL_LINK_STATUS) == GL33.GL_FALSE) {
            String infoLog = GL33.glGetProgramInfoLog(programID);
            throw new RuntimeException("Error linking shader program: " + infoLog);
        }

        // Clean up the shader objects as they are now linked to the program
        GL33.glDeleteShader(vertexShaderID);
        GL33.glDeleteShader(fragmentShaderID);
    }

    // Helper function to compile individual shaders (vertex or fragment)
    private int compileShader(String source, int type) {
        int shaderID = GL33.glCreateShader(type);
        GL33.glShaderSource(shaderID, source);
        GL33.glCompileShader(shaderID);

        if (GL33.glGetShaderi(shaderID, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE) {
            String infoLog = GL33.glGetShaderInfoLog(shaderID);
            throw new RuntimeException("Error compiling shader: " + infoLog);
        }
        return shaderID;
    }

    // Activate the shader program for rendering
    public void use() {
        GL33.glUseProgram(programID);
    }

    // Set an integer uniform variable
    public void setUniform(String name, int value) {
        if(!unifromLocationCache.containsKey(name)) {
            int location = GL33.glGetUniformLocation(programID, name);
            if (location == -1) {
                System.err.println("Warning: Uniform '" + name + "' not found in shader.");
                return;
            }
            unifromLocationCache.put(name, location);
        }

        GL33.glUniform1i(unifromLocationCache.get(name), value);
    }

    // Set a float uniform variable
    public void setUniform(String name, float value) {
        if(!unifromLocationCache.containsKey(name)) {
            int location = GL33.glGetUniformLocation(programID, name);
            if (location == -1) {
                System.err.println("Warning: Uniform '" + name + "' not found in shader.");
                return;
            }
            unifromLocationCache.put(name, location);
        }

        GL33.glUniform1f(unifromLocationCache.get(name), value);
    }

    // Set a 4x4 matrix uniform
    public void setUniform(String name, Matrix4f matrix) {
        if(!unifromLocationCache.containsKey(name)) {
            int location = GL33.glGetUniformLocation(programID, name);
            if (location == -1) {
                System.err.println("Warning: Uniform '" + name + "' not found in shader.");
                return;
            }
            unifromLocationCache.put(name, location);
        }

        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
        try{
            matrix.get(buffer);
            GL33.glUniformMatrix4fv(unifromLocationCache.get(name), false, buffer);
        } finally {
            MemoryUtil.memFree(buffer);
        }
    }

    public void setUniform(String name, Vector3f value) {
        if(!unifromLocationCache.containsKey(name)) {
            int location = GL33.glGetUniformLocation(programID, name);
            if (location == -1) {
                System.err.println("Warning: Uniform '" + name + "' not found in shader.");
                return;
            }
            unifromLocationCache.put(name, location);
        }

        GL33.glUniform3f(unifromLocationCache.get(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, Matrix3f matrix) {
        if(!unifromLocationCache.containsKey(name)) {
            int location = GL33.glGetUniformLocation(programID, name);
            if (location == -1) {
                System.err.println("Warning: Uniform '" + name + "' not found in shader.");
                return;
            }
            unifromLocationCache.put(name, location);
        }

        FloatBuffer buffer = MemoryUtil.memAllocFloat(9);
        try{
            matrix.get(buffer);
            GL33.glUniformMatrix3fv(unifromLocationCache.get(name), false, buffer);
        } finally {
            MemoryUtil.memFree(buffer);
        }
    }

    public void setUniform(String name, Vector2f value) {
        if(!unifromLocationCache.containsKey(name)) {
            int location = GL33.glGetUniformLocation(programID, name);
            if (location == -1) {
                System.err.println("Warning: Uniform '" + name + "' not found in shader.");
                return;
            }
            unifromLocationCache.put(name, location);
        }

        GL33.glUniform2f(unifromLocationCache.get(name), value.x, value.y);
    }

    // Clean up the shader program
    public void delete() {
        if (programID != 0) {
            GL33.glDeleteProgram(programID);
        }
    }

    // Get the program ID (useful for debugging or other purposes)
    private int getProgramID() {
        return programID;
    }

}
