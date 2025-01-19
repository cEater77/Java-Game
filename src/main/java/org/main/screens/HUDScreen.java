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
            Game.getUiManager().popScreen();
        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

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
