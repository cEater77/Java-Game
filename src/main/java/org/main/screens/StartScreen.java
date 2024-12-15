package org.main.screens;

import imgui.ImGui;
import org.main.Application;

public class StartScreen implements Screen {
    @Override
    public void update(){

    }

    @Override
    public void render(){
        ImGui.begin("StartScreen");
        ImGui.text("hello");
        if(ImGui.button("Start Game"))
            Application.uiManager.pushScreen(new HUDScreen());
        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
