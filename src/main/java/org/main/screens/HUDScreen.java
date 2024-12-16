package org.main.screens;

import imgui.ImGui;
import org.main.Game;

public class HUDScreen implements IScreen {

    @Override
    public void update() {

    }

    @Override
    public void render() {
        ImGui.begin("HUD Screen");
        if(ImGui.button("Leave Game"))
            Game.uiManager.popScreen();
        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
