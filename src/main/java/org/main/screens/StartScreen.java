package org.main.screens;

import imgui.ImGui;
import imgui.ImVec2;
import org.main.Game;

public class StartScreen implements IScreen {
    @Override
    public void update(){

    }

    @Override
    public void render(){
        ImGui.begin("StartScreen");

        ImGui.text("hello");
        float windowSizeX = ImGui.getWindowSize().x;

        if(ImGui.button("Start Game"))
            Game.uiManager.pushScreen(new HUDScreen());

        if(ImGui.button("open settings"))
            Game.uiManager.pushScreen(new SettingsScreen());

        ImGui.sameLine(windowSizeX - 50); ImGui.text("tim");



        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
