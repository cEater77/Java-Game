package org.main.screens;

import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.main.Game;

import java.beans.Visibility;
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
    int lastselecteddifficulty;
    int getLastselectedvisibilty;
    List<String> ranglistePosition = Arrays.asList("1. ", "2. ", "3. ", "4. ", "5. ", "6. ", "7. ", "8. ", "9. ", "10. ");

    int[] visibilty = {80};

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

        ImGui.text("");

        ImGui.setNextItemWidth(ImGui.getWindowSizeX()/3);
        ImGui.inputTextWithHint("##SpielerNameEingabe", "Spielername eingeben", playername);
        playernamelength = playername.getLength();
        //if(ImGui.getWindowSizeX() >= 400){ ImGui.sameLine(280); ImGui.text("Hallo");}
        String[] temp = new String[levelNames.size()];
        for (int i = 0; i < levelNames.size(); i++) temp[i] = levelNames.get(i);
        ImGui.sameLine(ImGui.getWindowSizeX()/3*2+8);
        ImGui.setNextItemWidth(ImGui.getWindowSizeX()/3);
        ImGui.combo("Levels", new ImInt(0), temp);

        ImGui.text("" + ImGui.getWindowSizeX());

        ImGui.text("");
        ImGui.sameLine(0, ImGui.getWindowSizeX() - ((ImGui.getWindowSizeX()-10)/3));
        ImGui.text("Schwierigkeit:");

        ImGui.text("");
        ImGui.sameLine(0, ImGui.getWindowSizeX() - (ImGui.getWindowSizeX()/3));
        ImGui.setNextItemWidth(ImGui.getWindowSizeX()/3);
        if(ImGui.sliderInt("##Schwierigkeitsslider", difficultyIndex, 0, 3, Schwierigkeit.values()[difficultyIndex[0]].toString().toLowerCase())){
            if(Schwierigkeit.values()[difficultyIndex[0]] == Schwierigkeit.EINFACH){
                visibilty[0] = 80;
            } else if (Schwierigkeit.values()[difficultyIndex[0]] == Schwierigkeit.MITTEL) {
                visibilty[0] = 50;
            } else if (Schwierigkeit.values()[difficultyIndex[0]] == Schwierigkeit.SCHWER) {
                visibilty[0] = 20;
            }
        }

        ImGui.text("");
        ImGui.sameLine(0, ImGui.getWindowSizeX() - ((ImGui.getWindowSizeX()-10)/3));
        ImGui.text("Visibility:");

        ImGui.text("");
        ImGui.sameLine(0, ImGui.getWindowSizeX() - (ImGui.getWindowSizeX()/3));
        ImGui.setNextItemWidth(ImGui.getWindowSizeX()/3);
        if(ImGui.sliderInt("##visibilityslider",visibilty , 1, 100)){//Visibility
            if (visibilty[0] == 20) {
                difficultyIndex[0] = Schwierigkeit.SCHWER.ordinal();
            }else if (visibilty[0] == 50) {
                difficultyIndex[0] = Schwierigkeit.MITTEL.ordinal();
            }else if (visibilty[0] == 80) {
                difficultyIndex[0] = Schwierigkeit.EINFACH.ordinal();
            }else if (visibilty[0] != 20 && visibilty[0] != 50 && visibilty[0] != 80) {
                difficultyIndex[0] = Schwierigkeit.INDIVIDUELL.ordinal();
            }
        }

        ImGui.text("");

        ImGui.text("");
        ImGui.sameLine(0, ImGui.getWindowSizeX() - (ImGui.getWindowSizeX()/3-2));
        if (ImGui.checkbox("Rangliste", ranglisteactive)) {
            if (r == 0) {
                ranglisteactive = true;
                r = 1;
                if (z == 0) {
                    timerverändert = 1;
                    z = 1;
                    timeractive = true;
                }
                lastselecteddifficulty = difficultyIndex[0];
                difficultyIndex[0] = Schwierigkeit.SCHWER.ordinal();
                getLastselectedvisibilty = visibilty[0];
                visibilty[0] = 20;
            }else {
                ranglisteactive = false;
                r = 0;
                if (z == 1 && timerverändert == 1) {
                    z = 0;
                    timerverändert = 0;
                    timeractive = false;
                }
                difficultyIndex[0] = lastselecteddifficulty;
                visibilty[0] = getLastselectedvisibilty;
            }
        }
        if (r == 1 && Schwierigkeit.values()[difficultyIndex[0]] != Schwierigkeit.SCHWER) {
            r = 0;
            ranglisteactive = false;
            if (z == 1 && timerverändert == 1) {
                z = 0;
                timerverändert = 0;
                timeractive = false;
            }
            difficultyIndex[0] = lastselecteddifficulty;
            visibilty[0] = getLastselectedvisibilty;
        }else if (r == 1 && z==0) {
            r = 0;
            ranglisteactive = false;
            if (z == 1 && timerverändert == 1) {
                z = 0;
                timerverändert = 0;
                timeractive = false;
            }
            difficultyIndex[0] = lastselecteddifficulty;
            visibilty[0] = getLastselectedvisibilty;
        }

        ImGui.text("Rangliste:");
        ImGui.sameLine(ImGui.getWindowSizeX()/9*2);
        ImGui.text("Zeit:");

        String[] löle = new String[ranglistePosition.size()];
        for (int j = 0; j < ranglistePosition.size(); j++) löle[j] = ranglistePosition.get(j);
        ImGui.setNextItemWidth(ImGui.getWindowSizeX()/3);
        ImGui.listBox("##RanglisteLISTE", new ImInt(0), löle);


        ImGui.sameLine(ImGui.getWindowSizeX()/3*2+10);
        if (ImGui.checkbox("Timer", timeractive)) {
            if (z == 0) {
                timeractive = true;
                z = 1;
            } else {
                timeractive = false;
                z = 0;
            }
        }






        // String[] temp = new String[levelNames.size()];
        //ImGui.sameLine();ImGui.combo("Levels", new ImInt(0), temp);

        if (ImGui.button("Start Game"))
            Game.getUiManager().pushScreen(new HUDScreen());
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
