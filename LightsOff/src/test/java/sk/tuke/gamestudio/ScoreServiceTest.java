package sk.tuke.gamestudio;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreServices.ScoreService;
import sk.tuke.gamestudio.service.ScoreServices.ScoreServiceRestClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ScoreServiceTest {

    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "alexej1";
    public static final String CLEAR_DB ="delete from score";

    private ScoreService service = new ScoreServiceRestClient();
    
    @Test
    public void addScore() throws Exception {
        clearDb();
        Score score = new Score("LightsOff", "Alex", 100, new Date());
        service.addScore(score);
        List<Score> scores = service.getBestScores("LightsOff");

        assertEquals("Alex", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());
        assertEquals("LightsOff", scores.get(0).getGame());
    }
    @Test
    public void getBestScores() throws Exception {
        clearDb();
        Score score = new Score("LightsOff", "Alex", 101, new Date());
        service.addScore(score);
        score = new Score("LightsOff", "Duri", 102, new Date());
        service.addScore(score);
        score = new Score("LightsOff", "Tomas", 103, new Date());
        service.addScore(score);
        List<Score> scores = service.getBestScores("LightsOff");

        assertEquals("Alex", scores.get(2).getPlayer());
        assertEquals(101, scores.get(2).getPoints());
        assertEquals("LightsOff", scores.get(2).getGame());

        assertEquals("Duri", scores.get(1).getPlayer());
        assertEquals(102, scores.get(1).getPoints());
        assertEquals("LightsOff", scores.get(1).getGame());

        assertEquals("Tomas", scores.get(0).getPlayer());
        assertEquals(103, scores.get(0).getPoints());
        assertEquals("LightsOff", scores.get(0).getGame());
    }

    private void clearDb() throws Exception {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(CLEAR_DB);
            ps.executeUpdate();
        }catch (SQLException e) {
            throw new Exception("Error clearing database ", e);
        }
    }
}

