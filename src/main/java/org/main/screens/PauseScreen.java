package org.main.screens;

import imgui.ImGui;

public class PauseScreen implements IScreen{
    @Override
    public void update() {

    }

    @Override
    public void render() {
        ImGui.begin("pause screen");
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
