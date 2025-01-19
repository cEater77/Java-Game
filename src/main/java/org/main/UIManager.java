package org.main;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiContext;
import org.main.screens.EditorScreen;
import org.main.screens.IScreen;
import org.main.screens.InfoScreen;
import org.main.screens.StartScreen;

import java.util.Stack;


public class UIManager {

    public UIManager() {
        ImGui.createContext();
        glfwImpl.init(Game.getWindow().getNativeWindow(), true);
        glImpl.init("#version 330");
        pushScreen(new InfoScreen());
        pushScreen(new EditorScreen());
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

        ImGui.showDemoWindow();

        ImGui.render();
        glImpl.renderDrawData(ImGui.getDrawData());
    }

    public void pushScreen(IScreen screen) {
        screen.onEntrance();
        screens.push(screen);
    }

    public void popScreen() {
        IScreen screen = screens.pop();
        screen.onExit();
    }

    public IScreen getCurrentScreen() {
        return screens.peek();
    }

    private ImGuiContext uiContext;
    private final ImGuiImplGlfw glfwImpl = new ImGuiImplGlfw();
    private final ImGuiImplGl3 glImpl = new ImGuiImplGl3();
    private Stack<IScreen> screens = new Stack<IScreen>();
}
