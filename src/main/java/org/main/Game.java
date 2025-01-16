package org.main;

import Engine.*;
import Engine.animation.Animation;
import Engine.animation.AnimationController;
import Engine.renderer.Renderer;
import Engine.renderer.Texture;
import org.joml.Vector3f;
import org.main.GameObjects.Block;
import org.main.GameObjects.Player;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Game {

    public void init() {
        System.out.println("Initializing Game");

        window = new Window(800, 800, "Java Game");
        resourceManager = new ResourceManager();

        resourceManager.loadShader("default", "assets/shader/default.vert", "assets/shader/default.frag");

        renderer = new Renderer(resourceManager.getShader("default"));
        uiManager = new UIManager();
        levelBuilder = new LevelBuilder(renderer, resourceManager, uiManager);
        blockRegistry = new BlockRegistry();

        registerBlocks();
    }

    public void run() {
        System.out.println("Running Game...");

        levelBuilder.loadLevel(Paths.get("GameData/Levels/test.bin"));
        currentActiveLevel = levelBuilder.getLevel("test.bin");
        if (currentActiveLevel == null) {
            levelBuilder.createLevel("test.bin");
            currentActiveLevel = levelBuilder.getLevel("test.bin");
        }

        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            window.beginFrame();

            currentActiveLevel.tick();

            renderer.renderBatch();
            uiManager.update();

            window.endFrame();
        }

        levelBuilder.saveLevel(currentActiveLevel);
    }

    public void deinit() {
        System.out.println("Deinitializing Game");
        window.close();
    }

    private void registerBlocks() {
        Block woodBlock = new Block(new Vector3f(0.0f), resourceManager, true, Block.BlockTypeID.WOOD);

        Animation defaultAnimationWood = new Animation();
        List<Texture> framesWood = Arrays.asList(resourceManager.getTexture("wood"));
        defaultAnimationWood.addFrameAnimation(1.0f, false, framesWood);
        woodBlock.setAnimationController(new AnimationController("default", MovementDirection.NONE, defaultAnimationWood));

        blockRegistry.registerBlock(woodBlock);

        Block darkWoodBlock = new Block(new Vector3f(0.0f), resourceManager, true, Block.BlockTypeID.DARK_WOOD);

        Animation defaultAnimationDarkWood = new Animation();
        List<Texture> framesDarkWood = Arrays.asList(resourceManager.getTexture("dark_wood"));
        defaultAnimationDarkWood.addFrameAnimation(1.0f, false, framesDarkWood);
        darkWoodBlock.setAnimationController(new AnimationController("default", MovementDirection.NONE, defaultAnimationDarkWood));

        blockRegistry.registerBlock(darkWoodBlock);
    }

    public static Window getWindow() {
        return window;
    }

    public static BlockRegistry getBlockRegistry() {
        return blockRegistry;
    }

    public static UIManager getUiManager() {
        return uiManager;
    }

    public static Level getCurrentActiveLevel() {
        return currentActiveLevel;
    }

    private static Window window;
    private static BlockRegistry blockRegistry;
    private static UIManager uiManager;
    private static Level currentActiveLevel;

    private ResourceManager resourceManager;
    private Renderer renderer;
    private LevelBuilder levelBuilder;

    private GameState gamestate;

    private enum GameState {
        START_MENU,
        SELECTION,
        PAUSE_MENU,
        IN_GAME,
        LEVEL_COMPLETE
    }
}
