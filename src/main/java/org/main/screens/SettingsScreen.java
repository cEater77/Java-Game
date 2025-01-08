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
}
