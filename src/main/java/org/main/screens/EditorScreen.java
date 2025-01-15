package org.main.screens;

import imgui.ImGui;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector3f;
import org.main.Game;
import org.main.GameObjects.Block;
import org.main.GameObjects.GameObject;
import org.main.GameObjects.GameObjectType;
import org.main.Level;

import java.util.ArrayList;
import java.util.List;

public class EditorScreen implements IScreen {

    private Vector3f currentPos = new Vector3f(0.0f);
    private Block.BlockTypeID selectedBlockID;
    private List<GameObject> selectedGameObjects = new ArrayList<>();
    private boolean shouldPlaceAfterPosChange = false;
    private boolean positionChanged = false;

    private int deleteFrom;
    private int deleteTo;

    @Override
    public void update() {

    }

    @Override
    public void render() {
        ImGui.begin("EditorScreen");
        if (ImGui.button("Go to start screen"))
            Game.getUiManager().pushScreen(new StartScreen());
        ImFloat x = new ImFloat(currentPos.x),y = new ImFloat(currentPos.y),z = new ImFloat(currentPos.z);
        ImGui.inputFloat("x", x, 1.0f);
        ImGui.inputFloat("y",y, 1.0f);
        ImGui.inputFloat("z",z, 1.0f);
        positionChanged = x.get() - currentPos.x != 0 || y.get() - currentPos.y != 0 || z.get() - currentPos.z != 0;
        currentPos.x = x.get(); currentPos.y = y.get(); currentPos.z = z.get();

        ImInt index = new ImInt(0);
        String[] values = new String[Block.BlockTypeID.values().length]; for(int i = 0; i < values.length; i++) values[i] = Block.BlockTypeID.values()[i].toString().toLowerCase();
        ImGui.combo("Blocks to place",index,values);
        selectedBlockID = Block.BlockTypeID.values()[index.get()];

        Level currentLevel = Game.getCurrentActiveLevel();

        if(ImGui.button("Place Block") || positionChanged && shouldPlaceAfterPosChange)
        {
            Block block = new Block(Game.getBlockRegistry().getBlockByID(selectedBlockID));
            block.setPosition(new Vector3f(currentPos));

            currentLevel.addGameObject(block);
        }

        if(!selectedGameObjects.isEmpty()) {
            ImGui.sameLine();
            if (ImGui.button("delete Object")) {

                selectedGameObjects.forEach(gameObject -> {
                    if(gameObject.getGameObjectType() == GameObjectType.PLAYER)
                        return;
                    currentLevel.getGameObjects().remove(gameObject);
                });
            }
        }

        ImGui.sameLine();
        if(ImGui.radioButton("Place Automatically when changing the Position", shouldPlaceAfterPosChange))
        {
            shouldPlaceAfterPosChange = !shouldPlaceAfterPosChange;
        }

        if(ImGui.collapsingHeader("GameObjects"))
        {
            int lineNum = 0;
            for(GameObject gameObject : currentLevel.getGameObjects())
            {
                if(ImGui.selectable(lineNum +  " " + gameObject.toString(), selectedGameObjects.contains(gameObject)))
                {
                    if(!selectedGameObjects.contains(gameObject))
                        selectedGameObjects.add(gameObject);
                    else
                        selectedGameObjects.remove(gameObject);
                }
                lineNum++;
            }
        }

        ImGui.end();
    }

    @Override
    public void resize(int width, int height) {

    }
}
