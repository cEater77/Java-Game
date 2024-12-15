package org.main.screens;

import imgui.ImGui;
import org.main.Application;

public class HUDScreen implements Screen {

    @Override
    public void update() {

    }

    @Override
    public void render() {
        ImGui.begin("HUD Screen");
        if(ImGui.button("Leave Game"))
            Application.uiManager.popScreen();
        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
