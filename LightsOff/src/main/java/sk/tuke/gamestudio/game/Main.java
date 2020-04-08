package sk.tuke.gamestudio.game;

import sk.tuke.gamestudio.game.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.core.Field;

import java.util.Scanner;

public class Main {
    public static final String PURPLE = "\u001B[35m";
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";

    public static void main(String[] args) {
        int level = 0;
        System.out.print(YELLOW+"Please insert your name here:"+RESET);
        String playersName = new Scanner(System.in).nextLine();
        System.out.println(GREEN+"Control of the game:");
        System.out.println("The player's task is to turn off all lights on the board. Clicking a light box changes ");
        System.out.println("its status from on to off, but the same happens with a field adjacent ");
        System.out.println("to the north, south, east, and west of the clicked field.");
        System.out.println("You are using coordinates.For the row coodinates use letters and for columns use numbers");
        System.out.println("Form of command :SA0   S-Select, A-row , 0-column"+RESET);
        while (true) {
            while(true){
            System.out.println(PURPLE+"Please insert levels difficulty (1 to 10) or for exit insert X "+RESET);
            System.out.print(YELLOW+"Insert here:"+RESET);
            String input = new Scanner(System.in).nextLine().toUpperCase().trim();
            if ("X".equals(input))
                System.exit(0);
            Scanner scanner = new Scanner(input);
            if (scanner.hasNextInt()) {
                level = scanner.nextInt();
                if ((level > 0) && (level <= 10))
                    break;
            } else {
                System.out.println(RED+"Wrong input! Try again."+RESET);
            }
            }
            Field field = new Field(5, 5, level, playersName);
            ConsoleUI hra = new ConsoleUI(field);
            hra.play();
            System.out.println(PURPLE+"Do you like to play again or end the game ? Insert: Play or End"+RESET);
            while(true){
                System.out.print(YELLOW+"Answer:"+RESET);
                String input = new Scanner(System.in).nextLine().trim().toUpperCase();
                if("PLAY".equals(input)) {
                    break;
                }else if("END".equals(input)){
                    System.exit(0);
                }else{
                    System.out.println(RED+"Wrong input! Try again! "+RESET);
                }
            }
        }
    }
}
