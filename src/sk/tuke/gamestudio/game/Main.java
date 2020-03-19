package sk.tuke.gamestudio.game;

import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.consoleui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Field field = new Field(5, 5, 5);
        ConsoleUI hra = new ConsoleUI(field);
        hra.play();
    }
}
