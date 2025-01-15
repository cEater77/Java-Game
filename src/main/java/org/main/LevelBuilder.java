package org.main;

import Engine.ResourceManager;
import Engine.renderer.Renderer;
import org.joml.Vector3f;
import org.main.GameObjects.Block;
import org.main.GameObjects.GameObject;
import org.main.GameObjects.GameObjectType;
import org.main.GameObjects.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class LevelBuilder {

    private List<Level> levels = new ArrayList<>();

    private Renderer renderer;
    private ResourceManager resourceManager;
    private UIManager uiManager;

    LevelBuilder(Renderer renderer, ResourceManager resourceManager, UIManager uiManager) {
        this.renderer = renderer;
        this.resourceManager = resourceManager;
        this.uiManager = uiManager;
    }

    public void createLevel(String levelName)
    {
        Level level = new Level(renderer, resourceManager, uiManager, levelName);
        level.addGameObject(new Player(new Vector3f(0.0f), resourceManager));
        levels.add(level);
    }

    public void loadLevel(Path levelPath) {
        String fileName = levelPath.getFileName().toString();
        try (DataInputStream stream = new DataInputStream(Files.newInputStream(levelPath))) {
            Level level = new Level(renderer, resourceManager, uiManager, fileName);
            int gameObjectCount = stream.readInt();
            for (int i = 0; i < gameObjectCount; i++) {
                GameObjectType type = GameObjectType.values()[stream.readInt()];
                GameObject gameObject;
                switch (type) {
                    case PLAYER:
                        gameObject = new Player(resourceManager);
                        break;
                    case BLOCK:
                        gameObject = new Block();
                        break;
                    default:
                        throw new IllegalStateException("Unknown type of GameObject was loaded in file");
                }
                gameObject.deserialize(stream);
                level.addGameObject(gameObject);
            }
            levels.add(level);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    public Level getLevel(String levelName) {
        for (Level level : levels) {
            if (level.getName().equals(levelName))
                return level;
        }

        return null;
    }

    public void saveLevel(Level level) {
        List<GameObject> gameObjects = level.getGameObjects();
        String filePath = "GameData/levels/test.bin";
        try (DataOutputStream stream = new DataOutputStream(Files.newOutputStream(Paths.get(filePath)))) {
            stream.writeInt(gameObjects.size());
            for (GameObject gameObject : gameObjects) {
                gameObject.serialize(stream);
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}
