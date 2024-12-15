package org.main.screens;

public interface Screen {
    public void update();
    public void render();
    public void resize(int width, int height);
}
