package Engine;

import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.main.Application;

import java.lang.Math;
import java.util.*;

import java.nio.*;

public class Renderer {

    public Renderer()
    {
        vertices = new ArrayList<Vertex>();
    }

    public void init(Shader shader)
    {
        this.shader = shader;
        
        vao = GL33.glGenVertexArrays();
        GL33.glBindVertexArray(vao);

        // Set up the VBO
        vbo = GL33.glGenBuffers();
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);

        GL33.glVertexAttribPointer(0, 2, GL33.GL_FLOAT, false, Vertex.VERTEX_SIZE * Float.BYTES, 0); // Position
        GL33.glEnableVertexAttribArray(0);
        GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, Vertex.VERTEX_SIZE * Float.BYTES, 2 * Float.BYTES); // Texture
        GL33.glEnableVertexAttribArray(1);

        // Unbind the VAO and VBO
        GL33.glBindVertexArray(0);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);

        isometricMat.m00(-0.5f);   isometricMat.m10(0.5f);       isometricMat.m20(0);
        isometricMat.m01(-0.25f);  isometricMat.m11(-0.25f);     isometricMat.m21(0.5f);
        isometricMat.m02(0);       isometricMat.m12(0);          isometricMat.m22(0);

        isometricMat.scale(75.0f);
    }

    public void renderTile(Vector3f pos, Vector2f size, Texture texture)
    {
        isometricMat.transform(pos);
        float windowWidth = (float)Application.window.getWidth();
        float windowHeight = (float)Application.window.getHeight();

        pos = new Vector3f(pos.x + windowWidth / 2.0f, pos.y + windowHeight, 0);
        vertices.add(new Vertex(new Vector2f(pos), new Vector2f(texture.uvPosition)));
        vertices.add(new Vertex(new Vector2f(pos.x + size.x, pos.y), new Vector2f(texture.uvPosition.x + texture.uvSize.x, texture.uvPosition.y)));
        vertices.add(new Vertex(new Vector2f(pos.x + size.x, pos.y + size.y), new Vector2f(texture.uvPosition.x + texture.uvSize.x, texture.uvPosition.y + texture.uvSize.y)));

        vertices.add(new Vertex(new Vector2f(pos), new Vector2f(texture.uvPosition)));
        vertices.add(new Vertex(new Vector2f(pos.x, pos.y + size.y), new Vector2f(texture.uvPosition.x, texture.uvPosition.y + texture.uvSize.y)));
        vertices.add(new Vertex(new Vector2f(pos.x + size.x, pos.y + size.y), new Vector2f(texture.uvPosition.x + texture.uvSize.x, texture.uvPosition.y + texture.uvSize.y)));
    }

    public void renderBackground()
    {

    }
    public void renderBatch()
    {
        shader.use();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1); // Buffer to store the width
            IntBuffer height = stack.mallocInt(1); // Buffer to store the height

            // Get the window size
            GLFW.glfwGetWindowSize(Window.nativeWindow, width, height);
            shader.setUniform("projection", new Matrix4f().ortho2D(0,width.get(0),0,height.get(0)));
            shader.setUniform("textureAtlas",0);
        }
        if (!vertices.isEmpty()) {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.size() * Vertex.VERTEX_SIZE);
            try {
                for (Vertex vertex : vertices) {
                    buffer.put(vertex.position.x);
                    buffer.put(vertex.position.y);
                    buffer.put(vertex.uv.x);
                    buffer.put(vertex.uv.y);
                }
                buffer.flip();

                // Update the VBO with new data
                GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
                GL33.glBufferData(GL33.GL_ARRAY_BUFFER, buffer, GL33.GL_DYNAMIC_DRAW);

                // Draw the batch
                GL33.glBindVertexArray(vao);
                GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, vertices.size());
                GL33.glBindVertexArray(0);
            } finally {
                MemoryUtil.memFree(buffer); // Free the buffer after use
            }
        }
        vertices.clear();
    }

    private int vao;
    private int vbo;
    private Shader shader;
    private List<Vertex> vertices;
    private final Matrix3f isometricMat = new Matrix3f();
}
