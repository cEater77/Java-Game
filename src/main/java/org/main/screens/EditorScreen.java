package org.main.screens;

import imgui.ImGui;
import imgui.flag.ImGuiKey;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector3f;
import org.main.Game;
import org.main.GameObjects.Block;
import org.main.GameObjects.GameObject;
import org.main.GameObjects.GameObjectType;
import org.main.Level;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class EditorScreen implements IScreen {

    private Vector3f currentPos = new Vector3f(0.0f);
    private Block.BlockTypeID selectedBlockID;
    private List<GameObject> selectedGameObjects = new ArrayList<>();
    private boolean shouldPlaceAfterPosChange = false;
    private boolean positionChanged = false;

    private GameObject selectUpToGameObject;

    private Stack<Action> undoActions = new Stack<>();
    private Stack<Action> redoActions = new Stack<>();

    private float[] tileSize = {80.0f};

    private ImInt index = new ImInt(0);

    class Action
    {

        public Action(List<GameObject> data, ActionType type)
        {
            this.actionData = data;
            this.actionType = type;
        }

        public List<GameObject> actionData = new ArrayList<>();
        public ActionType actionType;
    };

    enum ActionType
    {
        CREATED,
        DELETED,
        SELECTED,
        DESELECTED
    };

    @Override
    public void update() {

    }

    @Override
    public void render() {
        ImGui.begin("EditorScreen");
        if (ImGui.button("Go to start screen"))
            Game.getUiManager().pushScreen(new StartScreen());
        ImFloat x = new ImFloat(currentPos.x), y = new ImFloat(currentPos.y), z = new ImFloat(currentPos.z);
        ImGui.inputFloat("x", x, 1.0f);
        ImGui.inputFloat("y", y, 1.0f);
        ImGui.inputFloat("z", z, 1.0f);
        positionChanged = x.get() - currentPos.x != 0 || y.get() - currentPos.y != 0 || z.get() - currentPos.z != 0;
        currentPos.x = x.get();
        currentPos.y = y.get();
        currentPos.z = z.get();

        String[] values = new String[Block.BlockTypeID.values().length];
        for (int i = 0; i < values.length; i++) values[i] = Block.BlockTypeID.values()[i].toString().toLowerCase();
        ImGui.combo("Blocks to place", index, values);
        selectedBlockID = Block.BlockTypeID.values()[index.get()];

        Level currentLevel = Game.getCurrentActiveLevel();
        if (ImGui.button("Place Block") || positionChanged && shouldPlaceAfterPosChange) {
            Block block = new Block(Game.getBlockRegistry().getBlockByID(selectedBlockID));
            block.setPosition(new Vector3f(currentPos));

            currentLevel.addGameObject(block);
            addToUndo(Arrays.asList(block), ActionType.CREATED);
        }

        ImGui.sameLine();
        if(ImGui.button("Undo"))
        {
            undo();
        }

        ImGui.sameLine();
        if(ImGui.button("Redo"))
        {
            redo();
        }

        if (!selectedGameObjects.isEmpty()) {
            ImGui.sameLine();
            if (ImGui.button("delete Object")) {
                List<GameObject> deletedGameObjects = new ArrayList<>();
                selectedGameObjects.forEach(gameObject -> {
                    if (gameObject.getGameObjectType() == GameObjectType.PLAYER)
                        return;

                    deletedGameObjects.add(gameObject);
                });
                if(!deletedGameObjects.isEmpty()) {
                    addToUndo(deletedGameObjects, ActionType.DELETED);
                    Game.getCurrentActiveLevel().getGameObjects().removeAll(deletedGameObjects);
                    selectedGameObjects.removeAll(deletedGameObjects);
                }
            }
        }

        ImGui.sameLine();
        if (ImGui.radioButton("Place Automatically when changing the Position", shouldPlaceAfterPosChange)) {
            shouldPlaceAfterPosChange = !shouldPlaceAfterPosChange;
        }

        ImGui.sliderFloat("tileSize", tileSize, 30.0f, 150.0f);
        Game.getCurrentActiveLevel().getRenderer().setTileSize(tileSize[0]);

        ImGui.text("Click ctrl while selecting to select all Game Objects in between");
        if (ImGui.collapsingHeader("GameObjects")) {
            int lineNum = 0;
            List<GameObject> gameObjects = currentLevel.getGameObjects();
            for (GameObject gameObject : gameObjects) {
                if (ImGui.selectable(lineNum + " " + gameObject.toString(), selectedGameObjects.contains(gameObject))) {

                    if (ImGui.isKeyDown(ImGuiKey.LeftCtrl)) {
                        GameObject startSelectedGameObject = selectedGameObjects.get(selectedGameObjects.size() - 1);
                        int startIndex = Math.min(gameObjects.indexOf(startSelectedGameObject), gameObjects.indexOf(gameObject));
                        int endIndex = Math.max(gameObjects.indexOf(startSelectedGameObject), gameObjects.indexOf(gameObject));

                        List<GameObject> gameObjectsToBeSelected = gameObjects.subList(startIndex, endIndex + 1)
                                .stream().filter(g -> !selectedGameObjects.contains(g))
                                .collect(Collectors.toList());

                        selectedGameObjects.addAll(gameObjectsToBeSelected);
                        addToUndo(gameObjectsToBeSelected, ActionType.SELECTED);
                        continue;
                    }

                    if (!selectedGameObjects.contains(gameObject)) {
                        selectedGameObjects.add(gameObject);

                        addToUndo(Arrays.asList(gameObject), ActionType.SELECTED);
                    } else {
                        selectedGameObjects.remove(gameObject);
                        addToUndo(Arrays.asList(gameObject), ActionType.DESELECTED);
                    }
                }
                lineNum++;

                gameObject.setHighlight(selectedGameObjects.contains(gameObject));
            }
        }
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

    private void undo()
    {
        if(undoActions.isEmpty())
            return;

        Action currentAction = undoActions.pop();
        switch (currentAction.actionType)
        {
            case CREATED:
            {
                Game.getCurrentActiveLevel().getGameObjects().removeAll(currentAction.actionData);
                break;
            }

            case DELETED:
            {
                Game.getCurrentActiveLevel().getGameObjects().addAll(currentAction.actionData);
                break;
            }

            case SELECTED:
            {
                selectedGameObjects.removeAll(currentAction.actionData);
                break;
            }

            case DESELECTED:
            {
                selectedGameObjects.addAll(currentAction.actionData);
                break;
            }
        }

        redoActions.push(currentAction);
    }

    private void redo()
    {
        if(redoActions.isEmpty())
            return;

        Action currentAction = redoActions.pop();
        switch (currentAction.actionType)
        {
            case DELETED:
            {
                Game.getCurrentActiveLevel().getGameObjects().removeAll(currentAction.actionData);
                break;
            }

            case CREATED:
            {
                Game.getCurrentActiveLevel().getGameObjects().addAll(currentAction.actionData);
                break;
            }

            case DESELECTED:
            {
                selectedGameObjects.removeAll(currentAction.actionData);
                break;
            }

            case SELECTED:
            {
                selectedGameObjects.addAll(currentAction.actionData);
                break;
            }
        }

        undoActions.push(currentAction);
    }

    private void addToUndo(List<GameObject> data, ActionType actionType)
    {
        undoActions.push(new Action(data, actionType));
        while (!redoActions.isEmpty()) redoActions.pop();
    }
}
