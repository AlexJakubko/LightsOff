
package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.GameState;
import sk.tuke.gamestudio.game.core.Dot;

import sk.tuke.gamestudio.service.CommentServices.CommentException;
import sk.tuke.gamestudio.service.CommentServices.CommentService;
import sk.tuke.gamestudio.service.RatingServices.RatingException;
import sk.tuke.gamestudio.service.RatingServices.RatingService;
import sk.tuke.gamestudio.service.ScoreServices.ScoreService;


import java.util.Date;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/lightsoff")
public class LightsOffController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserController userController;

    private Field field;

    private int gameLevel = 1;

    private boolean firstSet = true;

    @RequestMapping
    public String lightsoff(String row, String column, Model model) {
        if (field == null && firstSet == true) {
            newGame();
        }
        try {
            if (field.getState() == GameState.PLAYING && firstSet == false) {
                if (row != null || column != null) {
                    field.shineDots(Integer.parseInt(row), Integer.parseInt(column));
                }
                if (field.getState() == GameState.SOLVED) {
                    firstSet = true;
                    if (userController.isLogged()) {
                        scoreService.addScore(new Score("LightsOff", field.getPlayersName(), field.getPlayersScore(), new Date()));
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        prepareModel(model);
        return "lightsoff";
    }

    @RequestMapping("/new")
    public String newGame(int level, Model model) {
        gameLevel = level;
        newGame();
        prepareModel(model);
        if (firstSet == true) {
            firstSet = false;
            return "redirect:/lightsoff";
        }
        return "lightsoff";
    }

    @RequestMapping("/retry")
    public String retryGame(Model model) {
        newGame();
        prepareModel(model);
        return "lightsoff";
    }

    @RequestMapping("/menu")
    public String menuGame(Model model) {
        firstSet = true;
        return "redirect:/lightsoff";
    }

    @RequestMapping("/addcomment")
    public String addComment(String comment) throws CommentException {
        commentService.addComment(new Comment(userController.getLoggedUser(), "LightsOff", comment, new Date()));
        return "redirect:/lightsoff";
    }

    @RequestMapping("/addrating")
    public String addRating(int rating) throws RatingException {
        ratingService.setRating(new Rating(userController.getLoggedUser(), "LightsOff", rating, new Date()));
        return "redirect:/lightsoff";
    }


    //    Tento pristup sice nie je idealny, ale pre zaciatok je najjednoduchsi
    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Dot dot = field.getDot(row, column);
                sb.append("<td>\n");
                if (field.equals(this.field))
                    sb.append("<a href='" + String.format("/lightsoff?row=%s&column=%s", row, column) + "'>\n");
                sb.append("<img width='40px' src='/images/lightsoff/" + getImageName(dot) + ".png'>");
                if (field.equals(this.field)) sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    public int getActualScore() {
        return field.getPlayersScore();
    }

    public int getPlayersRating() throws RatingException {
        int rating = ratingService.getRating("LightsOff", userController.getLoggedUser());
        return rating;
    }

    public GameState getGameState(){
        return field.getState();
    }

    public boolean isRated() throws RatingException {
        int rating = ratingService.getRating("LightsOff", userController.getLoggedUser());
        if (rating == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isFirstset() {
        return firstSet;
    }

    private String getImageName(Dot dot) {
        switch (dot.getState()) {
            case DONTSHINE:
                return "dontshine";
            case SHINE:
                return "shine";
        }
        throw new IllegalArgumentException("State is not supported " + dot.getState());
    }

    private void prepareModel(Model model) {
        try {
            model.addAttribute("scores", scoreService.getBestScores("LightsOff"));
            model.addAttribute("comments", commentService.getComments("LightsOff"));
            model.addAttribute("averageRating", ratingService.getAverageRating("LightsOff"));
        } catch (Exception e) {
            System.out.println("Loading models error!");
            e.printStackTrace();
        }
    }

    private void newGame() {
        field = new Field(5, 5, gameLevel, userController.getLoggedUser());
    }


}

