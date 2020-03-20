package sk.tuke.gamestudio.game;

import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.consoleui.ConsoleUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int level = 0;
        while (true) {
            System.out.println("Please insert levels difficulty (1 to 10) or for exit insert X ");
            System.out.print("Insert here:");
            String input = new Scanner(System.in).nextLine().toUpperCase().trim();

            if ("X".equals(input))
                System.exit(0);

            Scanner scanner = new Scanner(input);
                if (scanner.hasNextInt()) {
                    level = scanner.nextInt();
                    if((level>0)&&(level<=10))
                      break;
                } else{
                System.out.println("Wrong input! Try again.");
            }
        }
        Field field = new Field(5, 5, level);
        ConsoleUI hra = new ConsoleUI(field);
        hra.play();
    }
}
