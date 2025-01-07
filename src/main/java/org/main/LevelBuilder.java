package org.main;

import org.main.GameObjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {

    private List<Level> levels = new ArrayList<>();

    LevelBuilder()
    {

    }

    public void loadLevel(String fileName)
    {

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

    }

}
