package sk.tuke.gamestudio.game.consoleui;

import sk.tuke.gamestudio.game.core.Dot;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.GameState;
import sk.tuke.gamestudio.game.entity.Comment;
import sk.tuke.gamestudio.game.entity.Rating;
import sk.tuke.gamestudio.game.entity.Score;
import sk.tuke.gamestudio.game.service.*;

import java.util.Date;
import java.util.Scanner;
import java.util.List;

    public class ConsoleUI {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";

        private static final String GAME_NAME = "LightsOff";
    private final Field field;
    private ScoreService scoreService = new ScoreServiceJDBC();
    private  CommentService commentService = new CommentServiceJDBC();
    private RatingService ratingService = new RatingExceptionJDBC();

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        printScores();
        do {
            System.out.print(GREEN+"Your score:"+ RED+field.getPlayersScore()+RESET);
            System.out.println();
            System.out.println("Playing field ");
            printField();
            processInput();
        } while (field.getState() == GameState.PLAYING);
            printField();
            System.out.println(GREEN+ "You won!!!" + RESET);
            System.out.println("");
            scoreService.addScore(new Score(GAME_NAME,field.getPlayersName(),field.getPlayersScore(),new java.util.Date()));
            printComments();

    }
        public void printField() {
        printFieldHeader();
        printFieldBody();
    }

    private void printFieldHeader() {
        System.out.print(" ");
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.print(RED+"  ");
            System.out.print(column + 1);
        }
        System.out.println(RESET);
    }

    private void printFieldBody() {
        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print(BLUE+(char) ('A'+ row));
            System.out.print(RESET+' ');
            for (int column = 0; column < field.getColumnCount(); column++) {
                final Dot dot = field.getDot(row, column);
                switch (dot.getState()) {
                    case SHINE:
                        System.out.print(YELLOW+" O "+RESET);
                        break;
                    case DONTSHINE:
                        System.out.print(" X ");
                }
            }
            System.out.println();
        }
    }


    protected void processInput() {
        while (true) {
            System.out.println();
            System.out.print(YELLOW+"Enter input (e.g. SA0, X): "+RESET);
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
                    System.out.println(RED+"You put wrong Number Format"+RESET);
                }
            }else{
                System.out.println(RED+"Wrong input!"+RESET);
            }
        }
    }

    private void printScores() {
        try {
            List<Score> scores = scoreService.getBestScores(GAME_NAME);
            int index = 1;
            System.out.println();
            System.out.println(GREEN+"Best score: "+RESET);
            System.out.println("No. Player          Score      PlayedOn");
            System.out.println("----------------------------------------------------");
            for (Score score : scores) {
                System.out.format("%2d. %-16s %4d      %s\n", index, score.getPlayer(), score.getPoints(), score.getPlayedOn());
                index++;
            }
            System.out.println();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printComments() {
        System.out.println(PURPLE+"Would you like to open the comment section?  Insert: 'Yes' or 'No'"+RESET);
        while (true) {
            System.out.print(YELLOW+"Answer:"+RESET);
            String input = new Scanner(System.in).nextLine().trim().toUpperCase();
            if ("YES".equals(input)) {
                try {
                List<Comment> comments = commentService.getComments(GAME_NAME);
                int index = 1;
                System.out.println();
                System.out.println(GREEN+"Comments:"+RESET);
                System.out.println("------------------------------------------");
                for (Comment comment : comments) {
                    System.out.format("%2d. %-16s %s\n", index, comment.getPlayer(), comment.getComment());
                    index++;
                }
                System.out.println();
            }catch (Exception e){
                e.printStackTrace();
            }
                addComment();
                return;
            } else if ("NO".equals(input)) {
                return;
                } else {
            System.out.println(RED+"Wrong input! Try again."+RESET);
            }
        }
    }
    private void addComment(){
        System.out.println(PURPLE+"Would you like to leave a comment and rating? Answer: Yes or No"+RESET);
        while(true) {
            System.out.print(YELLOW+"Answer:"+RESET);
            String input = new Scanner(System.in).nextLine().trim().toUpperCase();
            if ("YES".equals(input)) {
                System.out.println(YELLOW+"Write your comment: "+RESET);
                input = new Scanner(System.in).nextLine();
                try {
                    commentService.addComment(new Comment(field.getPlayersName(),GAME_NAME,input,new Date()));
                } catch (CommentException e) {
                    e.printStackTrace();
                }
                try {
                    addRating();
                } catch (RatingException e) {
                    e.printStackTrace();
                }
                return;
            } else if ("NO".equals(input)) {
                return;
            } else {
                System.out.println(RED+"Wrong input! Try again."+RESET);
            }
        }
    }

    private void addRating() throws RatingException {
        System.out.println(PURPLE+"Please rate our game (1 to 5)"+RESET);
        while(true) {
            System.out.print(YELLOW+"Rate:"+RESET);
            String input = new Scanner(System.in).nextLine();
            Scanner scanner = new Scanner(input);
            if(scanner.hasNextInt()){
                int rate = scanner.nextInt();
                if((rate>0)&&(rate<=5)) {
                    ratingService.setRating(new Rating(field.getPlayersName(), GAME_NAME, rate, new Date()));
                    return;
                }
            }else{
            System.out.println(RED+"Wrong input ! Try again."+RESET);
        }
        }
    }
}


