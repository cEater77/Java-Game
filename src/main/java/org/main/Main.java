package org.main;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.init();
        game.run();
        game.deinit();
    }
}