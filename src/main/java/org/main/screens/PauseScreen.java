package org.main.screens;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.main.Game;

public class PauseScreen implements IScreen{

    int width,height;

    @Override
    public void update() {

    }

    @Override
    public void render() {
        ImGui.begin("pause screen", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBackground);
        ImGui.setNextWindowSize(new ImVec2(0.0f, 0.0f));
        ImGui.setNextWindowSize(new ImVec2(width, height));

        ImVec2 windowSize = ImGui.getWindowSize();
        ImGui.setCursorPos(new ImVec2(windowSize.x / 2.0f, windowSize.y / 2.0f));

        ImGui.text("Paused");

        ImGui.setCursorPosX(windowSize.x / 2.0f - ImGui.calcTextSizeX("Paused") / 2.0f);
        if(ImGui.button("Resume Game"))
        {
            Game.getCurrentActiveLevel().resume();
        }

        ImGui.setCursorPosX(windowSize.x / 2.0f - ImGui.calcTextSizeX("Paused") / 2.0f);
        if(ImGui.button("Leave Level"))
        {
            Game.getCurrentActiveLevel().resume();
            Game.setCurrentActiveLevel(null);
            Game.getUiManager().popScreen();
            Game.getUiManager().pushScreen(new StartScreen());
        }

        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {
        this.height = height;
        this.width = width;
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
