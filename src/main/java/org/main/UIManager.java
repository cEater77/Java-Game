package org.main;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiContext;
import org.main.screens.Screen;
import org.main.screens.StartScreen;

import java.util.Stack;


public class UIManager {

    public void init(long window)
    {
        ImGui.createContext();
        glfwImpl.init(window, true);
        glImpl.init("#version 330");
        pushScreen(new StartScreen());
    }

    public void update()
    {
        glfwImpl.newFrame();
        glImpl.newFrame();
        ImGui.newFrame();

        if(!screens.isEmpty())
        {
            Screen currentScreen = screens.peek();
            currentScreen.resize(Application.window.getWidth(), Application.window.getHeight());
            currentScreen.update();
            currentScreen.render();
        }

        ImGui.showDemoWindow();

        ImGui.render();
        glImpl.renderDrawData(ImGui.getDrawData());
    }

    public void pushScreen(Screen screen)
    {
        screens.push(screen);
    }

    public void popScreen()
    {
        screens.pop();
    }

    private ImGuiContext uiContext;
    private final ImGuiImplGlfw glfwImpl = new ImGuiImplGlfw();
    private final ImGuiImplGl3 glImpl = new ImGuiImplGl3();
    private Stack<Screen> screens = new Stack<Screen>();
}
