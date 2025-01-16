package org.main.screens;

import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.main.Game;

import java.util.Arrays;
import java.util.List;

public class StartScreen implements IScreen {

    ImString playername = new ImString();
    Integer playernamelength = 0;
    List<String> levelNames = Arrays.asList("Level auswählen", "Tutorial", "Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9", "Level 10", "Level generieren");
    boolean timeractive; //wenn true ist muss Timer aktiviert werden
    int z = 0; //für timer
    boolean ranglisteactive;
    int r = 0;
    int timerverändert = 0;
    List<String> ranglistePosition = Arrays.asList("1. ", "2. ", "3. ", "4. ", "5. ", "6. ", "7. ", "8. ", "9. ", "10. ");

    int[] visibilty = new int[1];

    public enum Schwierigkeit {
        EINFACH,
        MITTEL,
        SCHWER,
        INDIVIDUELL
    }

    private int[] difficultyIndex = new int[1];

    @Override
    public void update() {
    }


    @Override
    public void render() {
        ImGui.begin("StartScreen");

        ImGui.text("Willkommenstext...");
        if (playernamelength > 0) {
            ImGui.sameLine(0, ImGui.getWindowSizeX() - 281 - (playernamelength * 7));
            ImGui.text("Herr " + playername + " ist angemeldet.");
        } else {
            ImGui.sameLine();
            ImGui.text("");
        }

        ImGui.text("" + playernamelength);

        ImGui.inputTextWithHint("##", "Spielername eingeben", playername);
        playernamelength = playername.getLength();
        String[] temp = new String[levelNames.size()];
        for (int i = 0; i < levelNames.size(); i++) temp[i] = levelNames.get(i);
        ImGui.sameLine();
        ImGui.combo("Levels", new ImInt(0), temp);

        ImGui.text("");

        ImGui.sameLine(0, ImGui.getWindowSizeX() - 120);
        ImGui.text("Schwierigkeit:");

        ImGui.sliderFloat("##aeserserer", new float[5], 1, 100); //ImGui.sliderScalarN("##",new float[5],1,0,3);


        ImGui.sliderInt("##oeghuifdh",visibilty , 1, 100); //Visibility

        ImGui.sliderInt("##Schwierigkeitsslider", difficultyIndex, 0, 3, Schwierigkeit.values()[difficultyIndex[0]].toString().toLowerCase());
        if(Schwierigkeit.values()[difficultyIndex[0]] == Schwierigkeit.EINFACH){
            visibilty[0] = 80;
        } else if (Schwierigkeit.values()[difficultyIndex[0]] == Schwierigkeit.MITTEL) {
            visibilty[0] = 50;
        } else if (Schwierigkeit.values()[difficultyIndex[0]] == Schwierigkeit.SCHWER) {
            visibilty[0] = 20;
        }

        //ImGui.sliderAngle("##",)

        ImGui.text("");
        ImGui.sameLine(0, ImGui.getWindowSizeX() - 100);
        if (ImGui.checkbox("Timer", timeractive)) {
            if (z == 0) {
                timeractive = true;
                z = 1;
            } else {
                timeractive = false;
                z = 0;
            }
        }

        ImGui.text("");
        ImGui.sameLine(0, ImGui.getWindowSizeX() - 100);
        if (ImGui.checkbox("Rangliste", ranglisteactive)) {
            if (r == 0) {
                ranglisteactive = true;
                r = 1;
                if (z == 0) {
                    timerverändert = 1;
                    z = 1;
                    timeractive = true;
                }
            } else {
                ranglisteactive = false;
                r = 0;
                if (z == 1 && timerverändert == 1) {
                    z = 0;
                    timerverändert = 0;
                    timeractive = false;
                }
            }
        }

        ImGui.text("Rangliste:");

        String[] löle = new String[ranglistePosition.size()];
        for (int j = 0; j < ranglistePosition.size(); j++) löle[j] = ranglistePosition.get(j);
        ImGui.listBox("##RanglisteLISTE", new ImInt(0), löle);

        // String[] temp = new String[levelNames.size()];
        //ImGui.sameLine();ImGui.combo("Levels", new ImInt(0), temp);

        if (ImGui.button("Start Game"))
            Game.getUiManager().pushScreen(new HUDScreen());
        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
