package org.main;

import Engine.*;
import Engine.animation.Animation;
import Engine.animation.AnimationController;
import Engine.renderer.Renderer;
import Engine.renderer.Texture;
import imgui.ImGui;
import imgui.flag.ImGuiKey;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
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

        levelBuilder.loadAllLevelsInDirectory(Paths.get("GameData/Levels"));
        currentActiveLevel = levelBuilder.getLevel("test");
    }

    public void run() {
        System.out.println("Running Game...");

        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            window.beginFrame();

            currentActiveLevel.tick();

            handleInput();
            renderer.renderBatch();
            uiManager.update();
            window.endFrame();
        }

        levelBuilder.saveAllLevels();
    }

    public void deinit() {
        System.out.println("Deinitializing Game");
        window.close();
    }

    private void registerBlocks() {
        Block woodBlock = new Block(new Vector3f(0.0f), resourceManager, true, Block.BlockTypeID.WOOD);
        woodBlock.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("wood"))));

        blockRegistry.registerBlock(woodBlock);

        Block darkWoodBlock = new Block(new Vector3f(0.0f), resourceManager, true, Block.BlockTypeID.DARK_WOOD);
        darkWoodBlock.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("dark_wood"))));

        blockRegistry.registerBlock(darkWoodBlock);

        Block finishBlock = new Block(new Vector3f(0.0f), resourceManager, true, Block.BlockTypeID.FINISH);
        finishBlock.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("endPoint"))));

        blockRegistry.registerBlock(finishBlock);

        Block barrierBlock = new Block(new Vector3f(0.0f), resourceManager, false, Block.BlockTypeID.BARRIER);
        barrierBlock.setAnimationController(new AnimationController());

        blockRegistry.registerBlock(barrierBlock);
    }

    private void handleInput()
    {
        if (ImGui.isKeyPressed(ImGuiKey.F11)) {
            window.toggleFullscreen();
        }

        if(currentActiveLevel != null)
        {
            if (ImGui.isKeyPressed(ImGuiKey.P)) {
                if(currentActiveLevel.isPaused())
                    currentActiveLevel.resume();
                else
                    currentActiveLevel.pause();
            }
        }
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
    public static void setCurrentActiveLevel(Level level)
    {
        currentActiveLevel = level;
    }

    public static LevelBuilder getLevelBuilder()
    {
        return levelBuilder;
    }

    private static Window window;
    private static BlockRegistry blockRegistry;
    private static UIManager uiManager;
    private static Level currentActiveLevel;
    private static LevelBuilder levelBuilder;

    private ResourceManager resourceManager;
    private Renderer renderer;

    private GameState gamestate;

    private enum GameState {
        START_MENU,
        SELECTION,
        PAUSE_MENU,
        IN_GAME,
        LEVEL_COMPLETE
    }
}
