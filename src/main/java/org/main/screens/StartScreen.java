package org.main.screens;

import imgui.ImGui;
import imgui.type.ImString;
import org.main.Game;

public class StartScreen implements IScreen {

    ImString playername = new ImString();

    @Override
    public void update(){

    }

    @Override
    public void render(){
        ImGui.begin("StartScreen");
        float windowSizeX = ImGui.getWindowSizeX();
        int playernamelength = playername.getLength();

        ImGui.text("Willkommenstext...");
        ImGui.sameLine(windowSizeX - (playernamelength * 7) - 150);
        ImGui.text("Herr "+playername+" ist angemeldet.");

        ImGui.text("");
        ImGui.inputTextWithHint("##SpielerName", "Spielername", playername);
        ImGui.sameLine(); //Level

        ImGui.text("Nö");

        if(ImGui.button("Start Game"))
            Game.uiManager.pushScreen(new HUDScreen());
        ImGui.text("");
        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
