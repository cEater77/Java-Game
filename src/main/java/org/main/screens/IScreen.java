package org.main.screens;

public interface IScreen {

    void update();
    void render();
    void resize(int width, int height);
    void onEntrance();
    void onExit();
    boolean shouldRenderBehind();
    boolean shouldAlwaysRender();
}
