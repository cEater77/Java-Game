package org.main.screens;

import imgui.ImGui;

public class SettingsScreen implements IScreen{
    @Override
    public void update() {

    }

    @Override
    public void render() {
        ImGui.begin("Settings Screen");
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
