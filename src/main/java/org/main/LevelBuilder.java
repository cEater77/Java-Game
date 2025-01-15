package org.main;

import Engine.ResourceManager;
import Engine.renderer.Renderer;
import org.main.GameObjects.Block;
import org.main.GameObjects.GameObject;
import org.main.GameObjects.GameObjectType;
import org.main.GameObjects.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class LevelBuilder {

    private List<Level> levels = new ArrayList<>();
    private Renderer renderer;
    private ResourceManager resourceManager;

    LevelBuilder(Renderer renderer, ResourceManager resourceManager)
    {
        this.renderer = renderer;
        this.resourceManager = resourceManager;
    }

    public void loadLevel(String t_filePath)
    {
        String filePath = "GameData/levels/test.bin";
        String fileName = Paths.get(filePath).getFileName().toString();
        try (DataInputStream stream = new DataInputStream(Files.newInputStream(Paths.get(filePath)))) {
            Level level = new Level(renderer, resourceManager,fileName);
            int gameObjectCount = stream.readInt();
            for(int i = 0; i < gameObjectCount; i++)
            {
                GameObjectType type = GameObjectType.values()[stream.readInt()];
                GameObject gameObject;
                switch (type)
                {
                    case PLAYER: gameObject = new Player(resourceManager);break;
                    case DECORATION: gameObject = new Block(); break;
                    default: throw new IllegalStateException("Unknown type of GameObject was loaded in file");
                }
                gameObject.deserialize(stream);
                level.addGameObject(gameObject);
            }
            levels.add(level);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    public Level getLevel(String levelName)
    {
        for(Level level : levels)
        {
            if(level.getName().equals(levelName))
                return level;
        }

        throw new IllegalArgumentException("level: '" + levelName + "' doesn't exist");
    }

    public void saveLevel(Level level)
    {
        List<GameObject> gameObjects = level.getGameObjects();
        String filePath = "GameData/levels/test.bin";
        try (DataOutputStream stream = new DataOutputStream(Files.newOutputStream(Paths.get(filePath)))) {
            stream.writeInt(gameObjects.size());
            for(GameObject gameObject : gameObjects)
            {
                gameObject.serialize(stream);
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}
