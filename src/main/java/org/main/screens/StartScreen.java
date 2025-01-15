package org.main.screens;

import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.main.Game;

import java.beans.Visibility;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartScreen implements IScreen {

    ImString playername = new ImString();
    Integer playernamelength = 0;
    List<String> levelNames = Arrays.asList("Level auswählen","Level 1","Level 2","Level 3","Level 4","Level 5","Level 6","Level 7","Level 8","Level 9","Level 10","Level generieren");



    @Override
    public void update(){
    }

    @Override
    public void render(){
        ImGui.begin("StartScreen");

        ImGui.text("Willkommenstext...");
        if(playernamelength>0){
            ImGui.sameLine(0,ImGui.getWindowSizeX() - 281 - (playernamelength * 7)); ImGui.text("Herr "+ playername + " ist angemeldet.");
        }else{
            ImGui.sameLine(); ImGui.text("");
        }

        ImGui.text(""+playernamelength);

        ImGui.inputTextWithHint("##","Spielername eingeben", playername);
        playernamelength = playername.getLength();
        String[] temp = new String[levelNames.size()]; for(int i = 0; i < levelNames.size(); i++) temp[i] = levelNames.get(i);
        ImGui.sameLine();ImGui.combo("Levels", new ImInt(0), temp);

        ImGui.text("");

        ImGui.sameLine(0,ImGui.getWindowSizeX()-120); ImGui.text("Schwierigkeit:");

        ImGui.sliderFloat("##",new float[5],1,100); //ImGui.sliderScalarN("##",new float[5],1,0,3);

        ImGui.sliderFloat("##",new float[5],1,100); //Visibility

        if(ImGui.button("Start Game"))
            Game.uiManager.pushScreen(new HUDScreen());
        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
