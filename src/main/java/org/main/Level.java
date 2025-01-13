package org.main;

import Engine.Camera;
import Engine.animation.Animation;
import Engine.renderer.Renderer;
import Engine.ResourceManager;
import Engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.main.GameObjects.GameObject;
import org.main.GameObjects.GameObjectType;
import org.main.GameObjects.Player;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private String levelName;
    private Camera camera = new Camera(new Vector3f(0, 0, 0));
    private Renderer renderer;
    private ResourceManager resourceManager;
    private List<GameObject> gameObjects = new ArrayList<>();

    public Level(Renderer renderer, ResourceManager resourceManager, String levelName) {
        this.renderer = renderer;
        this.resourceManager = resourceManager;
        this.levelName = levelName;

        List<Texture> frames = new ArrayList<>();
        frames.add(resourceManager.getTexture("grass"));
        frames.add(resourceManager.getTexture("snow_grass"));
        frames.add(resourceManager.getTexture("wood"));
        frames.add(resourceManager.getTexture("ice"));
        frames.add(resourceManager.getTexture("dark_wood"));
        frames.add(resourceManager.getTexture("dark_log"));



        gameObjects.add(new Player(new Vector3f(0.0f), frames));
    }

    public void tick() {
        for (GameObject e : gameObjects) {
            e.update();
        }

        handleCollision();
        handleInput();
        draw();
    }

    private void draw() {
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                renderer.renderTile(new Vector3f(2.0f + (float) j, 2.0f + (float) i, 0.0f), resourceManager.getTexture("grass"));
            }
        }
        

        renderer.getShader().setUniform("camPos", camera.getIsometricPosition());
    }

    private void handleCollision() {
        for (GameObject e : gameObjects) {
            for (GameObject e2 : gameObjects) {
                if (e != e2 && e.getAABB().isIntersecting(e2.getAABB())) {
                    e.onCollision(e2);
                    e2.onCollision(e);
                }
            }
        }
    }

    private void handleInput() {
        Player player = getPlayer();
        List<MovementDirection> movement = new ArrayList<>();

        if (GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.FORWARD);

        if (GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.BACKWARD);

        if (GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.RIGHT);

        if (GLFW.glfwGetKey(Game.window.getNativeWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.LEFT);

        player.move(movement);
        camera.setIsometricPosition(player.getPosition());
    }

    private Player getPlayer() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getType() == GameObjectType.PLAYER)
                return (Player) gameObject;
        }

        throw new IllegalArgumentException("Player doesn't exist");
    }

    public List<GameObject> getGameObjects()
    {
        return gameObjects;
    }

    public String getName()
    {
        return levelName;
    }

    public void addGameObject(GameObject gameObject)
    {
        gameObjects.add(gameObject);
    }
}
