package org.main;

import Engine.animation.*;
import Engine.renderer.Renderer;
import Engine.ResourceManager;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.main.GameObjects.*;
import org.main.screens.*;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private String levelName;
    private List<GameObject> gameObjects = new ArrayList<>();
    private boolean isLevelPaused = false;
    private boolean isLevelFinished = false;
    private Vector3f playerStartPosition = new Vector3f(0.0f);
    private float tileSizeAtStart;

    private AnimationComponent finishAnimation = new AnimationComponent(0.8f, false);

    private boolean useFog = true;

    private Renderer renderer;
    private UIManager uiManager;

    public Level(Renderer renderer, UIManager uiManager, String levelName) {
        this.renderer = renderer;
        this.uiManager = uiManager;

        this.levelName = levelName;
    }

    public void tick() {

        if (!isLevelFinished) {
            if (!isLevelPaused) {

                // sortierung der GameObjects ist wichtig, da die Position in der Liste festlegt, ob ein GameObject über einem Anderen
                // gezeichnet wird. Hier werden GameObjects nach der Welt-Position sortiert.
                sortGameObjects();

                // nötig für die "End-Animation" wenn der spieler gewinnt
                tileSizeAtStart = Game.getCurrentActiveLevel().getRenderer().getTileSize();

                handleInput();
                gameObjects.forEach(GameObject::update);
                handleCollision();

                if (uiManager.getCurrentScreen() instanceof PauseScreen) {
                    uiManager.popScreen();
                }
            } else {
                if (!(uiManager.getCurrentScreen() instanceof PauseScreen)) {
                    uiManager.pushScreen(new PauseScreen());
                }
            }
        } else {
            if (!(uiManager.getCurrentScreen() instanceof LevelFinishScreen) && finishAnimation.isFinished()) {
                uiManager.pushScreen(new LevelFinishScreen());
            }
        }
        draw();
    }

    public void draw() {

        for (GameObject gameObject : gameObjects) {
            AnimationController animationController = gameObject.getAnimationController();
            Animation animation = animationController.getCurrentAnimation();
            animationController.update();
            if (isLevelPaused || isLevelFinished)
                animation.pauseAnimation();
            else
                animation.resumeAnimation();

            renderer.renderTile(gameObject.getPosition(), animation.getCurrentFrameAnimationData(), animation.getCurrentBlendAnimationData(), gameObject.isHighlighted() ? 1.0f : 0.0f);
        }

        if (isLevelFinished)
            playFinishTransition();

        Player player = getPlayer();
        Vector3f camPos = new Vector3f(player.getPosition());
        renderer.getShader().setUniform("camPos", camPos.negate());
        renderer.getShader().setUniform("useFog", useFog ? 1 : 0);

        renderer.renderFog(fogRadius);
    }

    private void handleCollision() {

        // momentan kann nur der spieler gegen GameObjects kollidieren, deswegen testen wir nur den spieler gegen alles.
        Player player = getPlayer();
        for(GameObject gameObject : gameObjects)
        {
            if(player != gameObject && player.getAABB().isIntersecting(gameObject.getAABB()) && !gameObject.ignoresCollision())
            {
                player.onCollision(gameObject);
                gameObject.onCollision(player);
            }
        }
    }

    public void sortGameObjects()
    {
        //Ordnung: zuerst sortiert nach z, anschließend nach x + y
        // x + y, weil die x und y in der Zeichordnung "gleichgewichtet" sein müssen.


        if(gameObjects.isEmpty())
            return;

        List<GameObject> newGameObjects = new ArrayList<>();
        while (!gameObjects.isEmpty())
        {
            GameObject smallestObject = gameObjects.get(0);
            for (GameObject object : gameObjects) {
                if (smallestObject.getPosition().z > object.getPosition().z)
                    smallestObject = object;
            }
            gameObjects.remove(smallestObject);
            newGameObjects.add(smallestObject);
        }

        while (!newGameObjects.isEmpty())
        {
            GameObject smallestObject = newGameObjects.get(0);
            for(GameObject object : newGameObjects)
            {
                Vector3f smallestPosition = smallestObject.getPosition();
                Vector3f objectPosition = object.getPosition();
                if(smallestPosition.z < objectPosition.z)
                    break;

                if(smallestPosition.x + smallestPosition.y > objectPosition.x + objectPosition.y)
                    smallestObject = object;
            }
            newGameObjects.remove(smallestObject);
            gameObjects.add(smallestObject);
        }
    }

    public void handleInput() {

        long nativeWindow = Game.getWindow().getNativeWindow();

        Player player = getPlayer();
        List<MovementDirection> movement = new ArrayList<>();

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.UP);

        if (GLFW.glfwGetKey(nativeWindow, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
            movement.add(MovementDirection.DOWN);

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

    public Vector3f getPlayerStartPosition()
    {
        return playerStartPosition;
    }

    public void finish() {
        isLevelFinished = true;
    }

    private void playFinishTransition() {
        float zoomDelta = 50.0f;
        finishAnimation.update(Game.getWindow().getLastFrameDuration());
        if (!finishAnimation.isFinished())
            renderer.setTileSize(tileSizeAtStart + finishAnimation.getProgress() * zoomDelta);
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


    public Renderer getRenderer() {
        return renderer;
    }

    public void setUseFog(boolean useFog) {
        this.useFog = useFog;
    }

    public void setFogRadius(float fogRadius) {
        this.fogRadius = fogRadius;
    }

    private float fogRadius = 10.0f;

    public boolean isUseFog() {
        return useFog;
    }

    public float getFogRadius() {
        return fogRadius;
    }

    public void pause() {
        isLevelPaused = true;
    }

    public void resume() {
        isLevelPaused = false;
    }

    public boolean isPaused() {
        return isLevelPaused;
    }

    public void reset()
    {
        isLevelFinished = false;
        getPlayer().setPosition(playerStartPosition);
        finishAnimation.reset();
        renderer.setTileSize(tileSizeAtStart);
    }
}
