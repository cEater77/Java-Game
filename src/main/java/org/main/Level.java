package org.main;

import Engine.Window;
import Engine.animation.Animation;
import Engine.animation.AnimationController;
import Engine.renderer.Renderer;
import Engine.ResourceManager;
import Engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.main.GameObjects.GameObject;
import org.main.GameObjects.GameObjectType;
import org.main.GameObjects.Player;
import org.main.screens.PauseScreen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class Level {
    private String levelName;
    private List<GameObject> gameObjects = new ArrayList<>();
    private boolean isLevelPaused = false;
    private boolean isLevelFinished = false;

    public boolean useFog = true;
    public float fogRadius = 10.0f;

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

        if (!isLevelPaused) {
            gameObjects.sort(Comparator
                    .comparingDouble((GameObject g) -> g.getPosition().z)
                    .thenComparingDouble(g -> g.getPosition().x + g.getPosition().y)
            );

            handleInput();
            handleCollision();
            draw();


            if (uiManager.getCurrentScreen() instanceof PauseScreen) {
                uiManager.popScreen();
            }
        } else {
            if (!(uiManager.getCurrentScreen() instanceof PauseScreen)) {
                uiManager.pushScreen(new PauseScreen());
            }
        }
    }

    public void draw() {

        for (GameObject gameObject : gameObjects) {
            AnimationController animationController = gameObject.getAnimationController();
            animationController.update();
            Animation animation = animationController.getCurrentAnimation();
            if (isLevelPaused)
                animation.pauseAnimation();
            else
                animation.resumeAnimation();

            renderer.renderTile(gameObject.getPosition(), animation.getCurrentFrameAnimationData(), animation.getCurrentBlendAnimationData(), gameObject.isHighlighted() ? 1.0f : 0.0f);
        }

        Player player = getPlayer();
        Vector3f camPos = new Vector3f(player.getPosition());
        renderer.getShader().setUniform("camPos", camPos.negate());
        renderer.getShader().setUniform("useFog", useFog ? 1 : 0);

        renderer.renderFog(fogRadius);
    }

    private void handleCollision() {
        for (GameObject e : gameObjects) {
            for (GameObject e2 : gameObjects) {
                if (e != e2 && e.getAABB().isIntersecting(e2.getAABB())) {
                        e.onCollision(e2);
                        e.update();
                }
            }
        }
    }

    public void handleInput() {

        long nativeWindow = Game.getWindow().getNativeWindow();

        Player player = getPlayer();
        List<MovementDirection> movement = new ArrayList<>();

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.FORWARD);

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.BACKWARD);

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.RIGHT);

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.LEFT);

        player.move(movement);
    }

    private Player getPlayer() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getGameObjectType() == GameObjectType.PLAYER)
                return (Player) gameObject;
        }

        throw new IllegalArgumentException("Player doesn't exist");
    }

    public void Finish()
    {

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

    public void pause()
    {
        isLevelPaused = true;
    }

    public void resume()
    {
        isLevelPaused = false;
    }

    public boolean isPaused()
    {
        return isLevelPaused;
    }
}
