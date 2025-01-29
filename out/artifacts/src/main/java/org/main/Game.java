package org.main;

import Engine.*;
import Engine.animation.AnimationController;
import Engine.renderer.Renderer;
import imgui.ImGui;
import imgui.flag.ImGuiKey;
import org.joml.*;
import org.main.GameObjects.*;
import org.main.screens.StartScreen;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private static Window window;
    private static BlockRegistry blockRegistry;
    private static UIManager uiManager;
    private static Level currentActiveLevel;
    private static LevelBuilder levelBuilder;

    private ResourceManager resourceManager;
    private Renderer renderer;

    public void init() throws IOException {
        System.out.println("Initializing Game");

        window = new Window(800, 800, "Java Game");
        resourceManager = new ResourceManager();

        resourceManager.loadShader("default", "src/main/resources/assets/shader/default.vert", "src/main/resources/assets/shader/default.frag");

        renderer = new Renderer(resourceManager.getShader("default"));
        uiManager = new UIManager();
        levelBuilder = new LevelBuilder(renderer, resourceManager, uiManager);
        blockRegistry = new BlockRegistry();

        // Alle Blöcke die im Spiel verwendet werden, müssen registriert werden bevor das eigentliche spiel angefängt, damit
        // die levelen diese auch laden können
        registerBlocks();

        levelBuilder.loadAllLevelsInDirectory(Paths.get("src/main/resources/assets/GameData/Levels"));


        uiManager.pushScreen(new StartScreen());
    }

    public void run() {
        System.out.println("Running Game...");

        while (!glfwWindowShouldClose(window.getNativeWindow())) {
            window.beginFrame();

            if (currentActiveLevel != null)
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
        Block woodBlock = new Block(new Vector3f(0.0f), resourceManager, false, Block.BlockTypeID.WOOD);
        woodBlock.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("smooth_wood"))));

        blockRegistry.registerBlock(woodBlock);

        Block darkWoodBlock = new Block(new Vector3f(0.0f), resourceManager, true, Block.BlockTypeID.DARK_WOOD);
        darkWoodBlock.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("dark_wood"))));

        blockRegistry.registerBlock(darkWoodBlock);

        Block grass = new Block(new Vector3f(0.0f), resourceManager, true, Block.BlockTypeID.GRASS);
        grass.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("grass"))));

        blockRegistry.registerBlock(grass);

        Block smoothStone = new Block(new Vector3f(0.0f), resourceManager, false, Block.BlockTypeID.SMOOTH_STONE);
        smoothStone.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("smooth_stone"))));

        blockRegistry.registerBlock(smoothStone);

        Block chiseledStone = new Block(new Vector3f(0.0f), resourceManager, true, Block.BlockTypeID.CHISELED_STONE);
        chiseledStone.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("chiseled_stone"))));

        blockRegistry.registerBlock(chiseledStone);

        Block whiteLog = new Block(new Vector3f(0.0f), resourceManager, false, Block.BlockTypeID.WHITE_LOG);
        whiteLog.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("white_log"))));

        blockRegistry.registerBlock(whiteLog);

        Block halfStone = new Block(new Vector3f(0.0f), resourceManager, false, Block.BlockTypeID.HALF_STONE);
        halfStone.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("half_stone"))));

        blockRegistry.registerBlock(halfStone);

        Block finishBlock = new Block(new Vector3f(0.0f), resourceManager, false, Block.BlockTypeID.FINISH);
        finishBlock.setAnimationController(new AnimationController(Arrays.asList(resourceManager.getTexture("finish"))));
        finishBlock.setCollisionCallback((block, other) ->
        {
            if (other.getGameObjectType() == GameObjectType.PLAYER) {
                float aabbAccordance = other.getAABB().getMinTranslationVector(block.getAABB()).length();
                if (aabbAccordance > 0.55f)
                    Game.getCurrentActiveLevel().finish();
            }
        });

        blockRegistry.registerBlock(finishBlock);
    }

    private void handleInput() {
        if (ImGui.isKeyPressed(ImGuiKey.F11)) {
            window.toggleFullscreen();
        }

        if (currentActiveLevel != null) {
            if (ImGui.isKeyPressed(ImGuiKey.P)) {
                if (currentActiveLevel.isPaused())
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

    public static void setCurrentActiveLevel(Level level) {
        currentActiveLevel = level;
    }

    public static LevelBuilder getLevelBuilder() {
        return levelBuilder;
    }
}
