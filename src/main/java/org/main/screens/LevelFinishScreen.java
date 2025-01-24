package org.main.screens;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.main.Game;

public class LevelFinishScreen implements IScreen {

    int width,height;

    @Override
    public void update() {

    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(new ImVec2(0.0f, 0.0f));
        ImGui.setNextWindowSize(new ImVec2(width + 10, height + 10));
        ImGui.begin("finish screen", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBackground);

        ImVec2 windowSize = ImGui.getWindowSize();
        ImGui.setCursorPos(new ImVec2(windowSize.x / 2.0f, windowSize.y / 2.0f));

        ImGui.text("You Won!");

        ImGui.setCursorPosX(windowSize.x / 2.0f - ImGui.calcTextSizeX("Paused") / 2.0f);
        if(ImGui.button("Back to Start screen"))
        {
            Game.getCurrentActiveLevel().reset();
            Game.setCurrentActiveLevel(null);
            Game.getUiManager().popScreen();
            Game.getUiManager().pushScreen(new StartScreen());
        }

        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onEntrance() {

    }

    @Override
    public void onExit() {

    }

    @Override
    public boolean shouldRenderBehind() {
        return false;
    }

    @Override
    public boolean shouldAlwaysRender() {
        return false;
    }
}
