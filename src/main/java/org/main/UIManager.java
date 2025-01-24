package org.main;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiContext;
import org.main.screens.*;

import java.util.Stack;


public class UIManager {

    private ImGuiContext uiContext;
    private final ImGuiImplGlfw glfwImpl = new ImGuiImplGlfw();
    private final ImGuiImplGl3 glImpl = new ImGuiImplGl3();
    private Stack<IScreen> screens = new Stack<IScreen>();

    public UIManager() {
        ImGui.createContext();
        glfwImpl.init(Game.getWindow().getNativeWindow(), true);
        glImpl.init("#version 330");
    }

    public void update() {
        glfwImpl.newFrame();
        glImpl.newFrame();
        ImGui.newFrame();

        Stack<IScreen> stackScreen = (Stack<IScreen>) screens.clone();
        boolean shouldRenderBehind = true;
        while (!stackScreen.isEmpty()) {
            IScreen currentScreen = stackScreen.pop();
            if (shouldRenderBehind || currentScreen.shouldAlwaysRender()) {
                currentScreen.update();
                currentScreen.render();
                currentScreen.resize(Game.getWindow().getWidth(), Game.getWindow().getHeight());
                shouldRenderBehind = currentScreen.shouldRenderBehind();
            }
        }

        ImGui.render();
        glImpl.renderDrawData(ImGui.getDrawData());
    }

    public void pushScreen(IScreen screen) {
        screen.onEntrance();
        screens.push(screen);
    }

    public void popScreen() {
        if (!screens.isEmpty()) {
            IScreen screen = screens.pop();
            screen.onExit();
        }
    }

    public IScreen getCurrentScreen() {
        if (screens.isEmpty())
            return null;

        return screens.peek();
    }
}
