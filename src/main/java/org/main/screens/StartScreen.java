package org.main.screens;

import imgui.ImGui;
import org.main.Game;

public class StartScreen implements IScreen {
    @Override
    public void update(){

    }

    @Override
    public void render(){
        ImGui.begin("StartScreen");
        ImGui.text("hello");
        if(ImGui.button("Start Game"))
            Game.uiManager.pushScreen(new HUDScreen());
        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
