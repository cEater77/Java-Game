package org.main;

import Engine.Camera;
import Engine.Renderer;
import Engine.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.main.entities.Entity;
import org.main.entities.EntityType;
import org.main.entities.MovementDirection;
import org.main.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class World {
    Camera camera = new Camera(new Vector3f(0, 0, 0));
    Renderer renderer;
    ResourceManager resourceManager;
    List<Entity> entities = new ArrayList<>();

    public World(Renderer renderer, ResourceManager resourceManager) {
        this.renderer = renderer;
        this.resourceManager = resourceManager;

        entities.add(new Entity(new Vector3f(20.0f, 0.0f,0.0f), null));
        entities.add(new Player(new Vector3f(0.0f), null));
    }

    public void tick() {
        handleCollision();
        handleInput();
        draw();
    }

    private void draw()
    {
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                renderer.renderTile(new Vector3f(2.0f + (float) j, 2.0f + (float) i, 0.0f), resourceManager.getTexture("grass"));
            }
        }

        for(Entity e : entities) {
            renderer.renderSprite(new Vector3f(e.getPosition()).negate(), new Vector2f(75.0f), resourceManager.getTexture("snow_grass"));
        }

        renderer.getShader().setUniform("camPos", camera.getIsometricPosition());
    }

    private void handleCollision()
    {
        for(Entity e : entities) {
            for(Entity e2 : entities) {
                if(e != e2)
                {
                    if(e.getAABB().isIntersecting(e2.getAABB()))
                    {
                        e.onCollision(e2);
                        e2.onCollision(e);
                    }
                }
            }
        }
    }

    private void handleInput()
    {
        Player player = getPlayer();
        List<MovementDirection> movement = new ArrayList<>();

        if(GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.FORWARD);

        if(GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.BACKWARD);

        if(GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.RIGHT);

        if(GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.LEFT);

        player.move(movement);
        camera.setIsometricPosition(player.getPosition());
    }

    private Player getPlayer()
    {
        for (Entity entity : entities) {
            if (entity.getType() == EntityType.PLAYER)
                return (Player) entity;
        }

        return null;
    }
}
