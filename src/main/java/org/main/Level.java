package org.main;

import Engine.Camera;
import Engine.Window;
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
import java.util.Comparator;
import java.util.List;

public class Level {
    private String levelName;
    private Camera camera = new Camera(new Vector3f(0, 0, 0));
    private List<GameObject> gameObjects = new ArrayList<>();

    private Renderer renderer;
    private ResourceManager resourceManager;
    private UIManager uiManager;

    public Level(Renderer renderer, ResourceManager resourceManager, UIManager uiManager, String levelName) {
        this.renderer = renderer;
        this.resourceManager = resourceManager;
        this.uiManager = uiManager;

        this.levelName = levelName;
    }

    public void tick() {

        gameObjects.sort(Comparator
                .comparingDouble((GameObject g) -> g.getPosition().x)
                .thenComparingDouble(g -> g.getPosition().y)
                .thenComparingDouble(g -> g.getPosition().z)
        );
        gameObjects.forEach(GameObject::update);

        handleCollision();
        handleInput();
        draw();
    }

    private void draw() {

        for (GameObject gameObject : gameObjects) {
            if (gameObject.getGameObjectType() == GameObjectType.PLAYER)
                continue;
            Animation animation = gameObject.getAnimationController().getCurrentAnimation();
            renderer.renderTile(gameObject.getPosition(), animation.getCurrentFrameAnimationData(), animation.getCurrentBlendAnimationData(), gameObject.isHighlighted() ? 1.0f : 0.0f);
        }

        Player player = getPlayer();
        Animation animation = player.getAnimationController().getCurrentAnimation();
        renderer.renderTile(player.getPosition(), animation.getCurrentFrameAnimationData(), animation.getCurrentBlendAnimationData(), player.isHighlighted() ? 1.0f : 0.0f);

        renderer.getShader().setUniform("camPos", camera.getIsometricPosition().negate());
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
        long nativeWindow = Game.getWindow().getNativeWindow();

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.FORWARD);

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.BACKWARD);

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.RIGHT);

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.LEFT);

        player.move(movement);
        camera.setIsometricPosition(new Vector3f(getPlayer().getPosition()));
    }

    private Player getPlayer() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getGameObjectType() == GameObjectType.PLAYER)
                return (Player) gameObject;
        }

        throw new IllegalArgumentException("Player doesn't exist");
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public String getName() {
        return levelName;
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
