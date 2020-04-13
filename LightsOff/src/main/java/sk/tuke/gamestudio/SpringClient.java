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

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
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
        return new Field(5, 5, 1,"Alex");
    }


    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
//        return new ScoreServiceJDBC();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
//        return new CommentServiceJDBC();
    }

    @Bean
    public RatingService ratingService(){
        return new RatingServiceJPA();
//        return new RatingServiceJDBC();
    }

}
