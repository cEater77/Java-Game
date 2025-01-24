package org.main;

import Engine.ResourceManager;
import Engine.renderer.Renderer;
import org.main.GameObjects.*;

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

    // wurde verwendet als wir die levels erstellt haben, im Base-Game hat es keinen nutzen
    public void createLevel(String levelName)
    {
        Level level = new Level(renderer, uiManager, levelName);
        level.addGameObject(new Player(level.getPlayerStartPosition(), resourceManager));
        levels.add(level);
    }

    public void loadLevel(Path levelPath) {
        String fileName = levelPath.getFileName().toString();
        fileName = fileName.substring(0, fileName.length() - 4);
        try (DataInputStream stream = new DataInputStream(Files.newInputStream(levelPath))) {
            Level level = new Level(renderer, uiManager, fileName);
            int gameObjectCount = stream.readInt();
            for (int i = 0; i < gameObjectCount; i++) {

                GameObjectType type = GameObjectType.values()[stream.readInt()];
                GameObject gameObject;
                switch (type) {
                    case PLAYER:
                        gameObject = new Player(level.getPlayerStartPosition(),resourceManager);
                        break;
                    case BLOCK:
                        gameObject = new Block();
                        break;
                    default:
                        throw new IllegalStateException("Unknown type of GameObject was loaded in file");
                }

                gameObject.deserialize(stream);
                gameObject.update();
                level.addGameObject(gameObject);
            }
            levels.add(level);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    public void loadAllLevelsInDirectory(Path directoryPath)
    {
        File directory = new File(String.valueOf(directoryPath));
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    String fileString = file.toString();
                    if (file.isFile() && fileString.substring(fileString.indexOf(".")).equals(".bin")) {
                        loadLevel(file.toPath());
                    }
                }
            }
        } else {
            System.out.println("The provided path is not a valid directory.");
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
        String filePath = "GameData/levels/" + level.getName() + ".bin";
        try (DataOutputStream stream = new DataOutputStream(Files.newOutputStream(Paths.get(filePath)))) {
            stream.writeInt(gameObjects.size());
            for (GameObject gameObject : gameObjects) {
                gameObject.serialize(stream);
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public List<Level> getAllLevels()
    {
        return levels;
    }

    public void saveAllLevels()
    {
        levels.forEach(this::saveLevel);
    }
}
