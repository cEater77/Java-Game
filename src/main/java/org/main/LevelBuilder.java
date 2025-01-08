package org.main;

import org.main.GameObjects.GameObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {

    private List<Level> levels = new ArrayList<>();

    LevelBuilder()
    {

    }

    public void loadLevel(String fileName)
    {
        String filePath = "GameData/levels/test.bin";
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Paths.get(filePath)))) {


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
