package sk.tuke.gamestudio;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingServices.RatingService;
import sk.tuke.gamestudio.service.RatingServices.RatingServiceRestClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RatingServiceTest {

    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "alexej1";
    public static final String INSERT_COMMENT ="delete from rating";

    private RatingService service = new RatingServiceRestClient();

    @Test
    public void setRating() throws Exception {
        clearDb();
        Rating rating = new Rating("Alex", "LightsOff", 5, new Date());
        service.setRating(rating);
        rating = new Rating("Jakub", "LightsOff", 3, new Date());
        service.setRating(rating);

        int playersRating = service.getRating("LightsOff","Alex");
        assertEquals(5, playersRating);

        playersRating = service.getRating("LightsOff","Jakub");
        assertEquals(3,playersRating);
    }

    @Test
    public void getAverageRating() throws Exception {
        clearDb();
        Rating rating = new Rating("Alex", "LightsOff", 5, new Date());
        service.setRating(rating);
        rating = new Rating("Jakub", "LightsOff", 3, new Date());
        service.setRating(rating);

        int averageRating = service.getAverageRating("LightsOff");

        assertEquals(4,averageRating);
    }

    @Test
    public void getRating() throws Exception {
        clearDb();
        Rating rating = new Rating("Alex", "LightsOff", 2, new Date());
        service.setRating(rating);
        rating = new Rating("Jakub", "LightsOff", 4, new Date());
        service.setRating(rating);

        int playersRating = service.getRating("LightsOff","Alex");
        assertEquals(2, playersRating);

        playersRating = service.getRating("LightsOff","Jakub");
        assertEquals(4,playersRating);
    }

    private void clearDb() throws Exception {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement(INSERT_COMMENT);
            ps.executeUpdate();
        }catch (SQLException e) {
            throw new Exception("Error clearing database ", e);
        }
    }
}