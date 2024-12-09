package Engine;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import java.util.*;

import java.nio.FloatBuffer;

public class Renderer {

    public void init(Shader shader)
    {
        this.shader = shader;
        
        vao = GL33.glGenVertexArrays();
        GL33.glBindVertexArray(vao);

        // Set up the VBO
        vbo = GL33.glGenBuffers();
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);

        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 5 * Float.BYTES, 0); // Position
        GL33.glEnableVertexAttribArray(0);
        GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES); // Texture
        GL33.glEnableVertexAttribArray(1);

        // Unbind the VAO and VBO
        GL33.glBindVertexArray(0);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
    }

    public void renderTile()
    {

    }

    public void renderBackground()
    {

    }
    public void renderBatch()
    {
        shader.use();

        if (vertices.size() > 0) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer buffer = stack.mallocFloat(vertices.size());
                for (Float vertex : vertices) {
                    buffer.put(vertex);
                }
                buffer.flip();

                // Update the VBO with new data
                GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
                GL33.glBufferData(GL33.GL_ARRAY_BUFFER, buffer, GL33.GL_DYNAMIC_DRAW);

                // Draw the batch
                GL33.glBindVertexArray(vao);
                GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, vertices.size() / 5);
                GL33.glBindVertexArray(0);
            }
        }
        vertices.clear();
    }

    private static final int MAX_BATCH_SIZE = 1002;

    private int vao;
    private int vbo;
    private Shader shader;
    private List<Float> vertices;
}
