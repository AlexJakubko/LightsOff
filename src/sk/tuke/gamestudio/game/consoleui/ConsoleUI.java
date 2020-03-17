package sk.tuke.gamestudio.game.consoleui;

import sk.tuke.gamestudio.game.core.Dot;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.GameState;
import sk.tuke.gamestudio.game.entity.Score;
import sk.tuke.gamestudio.game.service.ScoreService;

import java.util.Scanner;
import java.util.Collections;
import java.util.Date;
import java.util.List;



public class ConsoleUI {

    private static final String GAME_NAME = "LightOff";
    private final Field field;
    private ScoreService scoreService;


    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        printScores();
        do {
            System.out.println();
            printField();
            processInput();
        } while (field.getState() == GameState.PLAYING);

        printField();

        if (field.getState() == GameState.SOLVED) {
            System.out.println("You won!!!");
            scoreService.addScore(
            new Score(GAME_NAME,System.getProperty("user.name"),5,new Date()));
        } else
            System.out.println("Sorry!!!");
    }


    public void printField() {
        printFieldHeader();
        printFieldBody();
    }


    private void printFieldHeader() {
        System.out.print(" ");
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.print("  ");
            System.out.print(column + 1);
        }
        System.out.println();
    }

    private void printFieldBody() {
        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print((char) ('A' + row));
            System.out.print(' ');
            for (int column = 0; column < field.getColumnCount(); column++) {
                final Dot dot = field.getDot(row, column);
                switch (dot.getState()) {
                    case SHINE:
                        System.out.print(" X ");
                        break;
                    case DONTSHINE:
                        System.out.print(" O ");
                }
            }
            System.out.println();
        }
    }


    protected void processInput() {
        while (true) {
            System.out.print("Enter input (e.g. SA0, X): ");
            String input = new Scanner(System.in).nextLine().trim().toUpperCase();

            if ("X".equals(input))
                System.exit(0);

            if (input.length() >= 3) {
                try {
                    int row = input.charAt(1) - 'A';
                    int column = Integer.parseInt(input.substring(2)) - 1;
                    if (row >= 0 && row < field.getRowCount() && column >= 0 && column < field.getColumnCount()) {
                        if (input.charAt(0) == 'S') {
                            field.shineDots(row, column);
                        }
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("You put wrong Number Format");
                }
            }
        }
    }
    private void printScores() {
        List<Score> scores = scoreService.getBestScores(GAME_NAME);

        Collections.sort(scores);

        System.out.println("Top scores:");
        for (Score s : scores) {
            System.out.println(s);
        }
    }

}


