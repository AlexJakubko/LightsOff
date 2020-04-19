package sk.tuke.gamestudio;

import sk.tuke.gamestudio.game.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.*;
import sk.tuke.gamestudio.game.core.Field;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.util.Scanner;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {

    public static final String PURPLE = "\u001B[35m";
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";

    public static void main(String[] args) {

        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
        //SpringApplication.run(SpringClient.class, args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui) {
        return args -> ui.play();
    }

    @Bean
    public ConsoleUI consoleUI(Field field) {
        return new ConsoleUI(field);
    }

    @Bean
    public Field field() {
        int level = 0;
        System.out.print(YELLOW+"Please insert your name here:"+RESET);
        String playersName = new Scanner(System.in).nextLine();
        System.out.println(GREEN+"Control of the game:");
        System.out.println("The player's task is to turn off all lights on the board. Clicking a light box changes ");
        System.out.println("its status from on to off, but the same happens with a field adjacent ");
        System.out.println("to the north, south, east, and west of the clicked field.");
        System.out.println("You are using coordinates.For the row coodinates use letters and for columns use numbers");
        System.out.println("Form of command :SA0   S-Select, A-row , 0-column"+RESET);
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
        return field;
    }

    @Bean
    public ScoreService scoreService() {
//        return new ScoreServiceJPA();
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
//        return new CommentServiceJPA();
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService(){
//        return new RatingServiceJPA();
        return new RatingServiceRestClient();
    }

}
