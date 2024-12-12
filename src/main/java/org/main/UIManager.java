package org.main;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiContext;


public class UIManager {

    public void init(long window)
    {
        ImGui.createContext();
        glfwImpl.init(window, true);
        glImpl.init("#version 330");
    }

    public void update()
    {
        glfwImpl.newFrame();
        glImpl.newFrame();
        ImGui.newFrame();

        ImGui.begin("test");
        if(ImGui.button("test"))
        {
            ImGui.text("Hello World");
        }
        ImGui.end();

        ImGui.showDemoWindow();

        ImGui.render();
        glImpl.renderDrawData(ImGui.getDrawData());
    }
    private ImGuiContext uiContext;
    private final ImGuiImplGlfw glfwImpl = new ImGuiImplGlfw();
    private final ImGuiImplGl3 glImpl = new ImGuiImplGl3();
}
