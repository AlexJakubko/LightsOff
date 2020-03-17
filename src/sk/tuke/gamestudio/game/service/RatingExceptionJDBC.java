package sk.tuke.gamestudio.game.service;

import sk.tuke.gamestudio.game.entity.Rating;
import sk.tuke.gamestudio.game.entity.Score;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class RatingExceptionJDBC implements RatingService{
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "alexej1";

    public static final String INSERT_RATING =
            "INSERT INTO rating (player,game,rating, ratedon) VALUES (?, ?, ?, ?)";

    public static final String SELECT_AVG_RATING =
            "SELECT rating FROM rating WHERE game = ?";
    public static final String SELECT_RATING =
            "SELECT player,game,rating,ratedon FROM rating WHERE game=? && player = ?";


    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(INSERT_RATING)){
                ps.setString(1, rating.getGame());
                ps.setString(2, rating.getPlayer());
                ps.setInt(3, rating.getRating());
                ps.setDate(4, new Date(rating.getRatedon().getTime()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ScoreException("Error saving rating", e);
        }
    }
    @Override
    public int getAverageRating(String game) throws RatingException {
        List<Rating> ratings = new ArrayList<>();
        int averageRating = 0;
        int elementCount = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_AVG_RATING)){
                ps.setString(2, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        averageRating += rs.getInt(3);
                        elementCount++;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Error loading average rating", e);
        }
        return averageRating/elementCount;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_RATING)){
                ps.setString(2, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while() {

                    }
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Error loading ratings", e);
        }
        return ratings;
    }
    public static void main(String[] args) throws Exception {
        Rating rating = new Rating("peto","lightoff",4,new java.util.Date());
        RatingService ratingService = new RatingExceptionJDBC();
        ratingService.setRating(rating);
    }
}
