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

    private static final String GAME_NAME = "LightsOff";
    private final Field field;
    private ScoreService scoreService = new ScoreServiceJDBC();
    private  CommentService commentService = new CommentServiceJDBC();
    private RatingService ratingService = new RatingExceptionJDBC();

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() throws CommentException {
        printScores();
        int points = 0;
        do {
            System.out.println();
            printField();
            processInput();
            points++;
        } while (field.getState() == GameState.PLAYING);
        printField();
        if (field.getState() == GameState.SOLVED) {
            System.out.println("You won!!!");
            System.out.println("");
            scoreService.addScore(new Score(GAME_NAME,field.getPlayersName(),points,new java.util.Date()));
            printComments();
            addComment();
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
            System.out.println();
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
            }else{
                System.out.println("Wrong input!");
            }
        }
    }

    private void printScores() {
        try {
            List<Score> scores = scoreService.getBestScores(GAME_NAME);
            int index = 1;
            System.out.println();
            System.out.println("Top 10 players score");
            System.out.println();
            System.out.println("No. Player          Score      PlayedOn");
            System.out.println("----------------------------------------------------");
            for (Score score : scores) {
                System.out.format("%2d. %-16s %4d      %s\n", index, score.getPlayer(), score.getPoints(), score.getPlayedOn());
                index++;
            }
            System.out.println();
            System.out.println("Playing field ");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printComments() {
        try {
            List<Comment> comments = commentService.getComments(GAME_NAME);
            int index = 1;
            System.out.println();
            System.out.println("Comments:");
            System.out.println("------------------------------------------");
            for (Comment comment : comments) {
                System.out.format("%2d. %-16s %s\n", index, comment.getPlayer(),comment.getComment());
                index++;
            }
            System.out.println();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addComment() {

        System.out.println("You want leave comment? Answer: Yes or No");

        while(true) {
            System.out.print("Answer:");
            String input = new Scanner(System.in).nextLine().trim().toUpperCase();
            if ("YES".equals(input)) {
                System.out.println("Write your comment: ");
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
                System.exit(0);
            } else {
                System.out.println("Wrong input! Try again.");
            }
        }
    }

    private void addRating() throws RatingException {
        System.out.println("Please rate our game (1 to 5)");
        while(true) {
            System.out.print("Rate:");
            String input = new Scanner(System.in).nextLine();
            Scanner scanner = new Scanner(input);
            if(scanner.hasNextInt()){
                int rate = scanner.nextInt();
                if((rate>0)&&(rate<=5)) {
                    ratingService.setRating(new Rating(field.getPlayersName(), GAME_NAME, rate, new Date()));
                    return;
                }
            }else{
            System.out.println("Wrong input ! Try again.");
        }
        }
    }
}


