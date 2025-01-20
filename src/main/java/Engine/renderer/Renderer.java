package Engine.renderer;

import Engine.Window;
import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.util.*;

import java.nio.*;

import static org.lwjgl.opengl.GL11.glClearColor;

public class Renderer {

    public Renderer(Shader shader)
    {
        this.shader = shader;

        vao = GL33.glGenVertexArrays();
        GL33.glBindVertexArray(vao);

        // Set up the VBO
        vbo = GL33.glGenBuffers();
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);

        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, Vertex.VERTEX_SIZE * Float.BYTES, 0 * Float.BYTES); // Position
        GL33.glEnableVertexAttribArray(0);
        GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, Vertex.VERTEX_SIZE * Float.BYTES, 3 * Float.BYTES); // Offset
        GL33.glEnableVertexAttribArray(1);
        GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, Vertex.VERTEX_SIZE * Float.BYTES, 5 * Float.BYTES); // Texture
        GL33.glEnableVertexAttribArray(2);
        GL33.glVertexAttribPointer(3, 1, GL33.GL_FLOAT, false, Vertex.VERTEX_SIZE * Float.BYTES, 7 * Float.BYTES);
        GL33.glEnableVertexAttribArray(3);
        GL33.glVertexAttribPointer(4, 1, GL33.GL_FLOAT, false, Vertex.VERTEX_SIZE * Float.BYTES, 8 * Float.BYTES);
        GL33.glEnableVertexAttribArray(4);

        // Unbind the VAO and VBO
        GL33.glBindVertexArray(0);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);

        isometricMat.m00(-0.5f);   isometricMat.m10(0.5f);       isometricMat.m20(0);
        isometricMat.m01(-0.25f);  isometricMat.m11(-0.25f);     isometricMat.m21(0.5f);
        isometricMat.m02(0);       isometricMat.m12(0);          isometricMat.m22(0);

        isometricMat.scale(tileSize);

        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void renderTile(Vector3f pos, Texture texture, float alpha, float isHighlighted)
    {
        renderSprite(pos, new Vector2f(tileSize, tileSize), texture, alpha, isHighlighted);
    }

    public void renderSprite(Vector3f pos, Vector2f size, Texture texture, float alpha, float isHighlighted)
    {
        Vector2f bottomLeftCornerUV = new Vector2f(texture.uvPosition);
        Vector2f bottomLeftCornerOffset = new Vector2f(0,0);
        Vertex bottomLeftCorner = new Vertex(pos,bottomLeftCornerOffset, bottomLeftCornerUV, alpha, isHighlighted);

        Vector2f bottomRightCornerOffset = new Vector2f(size.x, 0);
        Vector2f bottomRightCornerUV = new Vector2f(texture.uvPosition.x + texture.uvSize.x, texture.uvPosition.y);
        Vertex bottomRightCorner = new Vertex(pos,bottomRightCornerOffset, bottomRightCornerUV, alpha, isHighlighted);

        Vector2f topRightCornerOffset = new Vector2f(size.x, size.y);
        Vector2f topRightCornerUV = new Vector2f(texture.uvPosition.x + texture.uvSize.x, texture.uvPosition.y + texture.uvSize.y);
        Vertex topRightCorner = new Vertex(pos,topRightCornerOffset, topRightCornerUV, alpha, isHighlighted);

        Vector2f topLeftCornerOffset = new Vector2f(0, size.y);
        Vector2f topLeftCornerUV = new Vector2f(texture.uvPosition.x, texture.uvPosition.y + texture.uvSize.y);
        Vertex topLeftCorner = new Vertex(pos,topLeftCornerOffset, topLeftCornerUV, alpha, isHighlighted);

        vertices.add(bottomLeftCorner);
        vertices.add(bottomRightCorner);
        vertices.add(topRightCorner);

        vertices.add(bottomLeftCorner);
        vertices.add(topLeftCorner);
        vertices.add(topRightCorner);
    }

    public void renderBackground()
    {

    }

    public void renderFog(float fogEnd)
    {
        shader.setUniform("fogEnd", fogEnd);
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
            shader.setUniform("isometricView",isometricMat);
            shader.setUniform("worldOrigin", new Vector2f(width.get(0) / 2.0f, height.get(0) / 2.0f));
        }
        if (!vertices.isEmpty()) {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.size() * Vertex.VERTEX_SIZE);
            try {
                for (Vertex vertex : vertices) {
                    buffer.put(vertex.position.x);
                    buffer.put(vertex.position.y);
                    buffer.put(vertex.position.z);
                    buffer.put(vertex.offset.x);
                    buffer.put(vertex.offset.y);
                    buffer.put(vertex.uv.x);
                    buffer.put(vertex.uv.y);
                    buffer.put(vertex.alpha);
                    buffer.put(vertex.isHighlighted);
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

    public Shader getShader() {
        return shader;
    }

    public void setTileSize(float tileSize)
    {
        if(tileSize <= 0)
            tileSize = 1.0f;
        isometricMat.scale(1.0f / this.tileSize);
        this.tileSize = tileSize;
        isometricMat.scale(this.tileSize);
    }

    private int vao;
    private int vbo;
    private Shader shader;
    private List<Vertex> vertices = new ArrayList<Vertex>();;
    private final Matrix3f isometricMat = new Matrix3f();
    private float tileSize = 80.0f;
}
