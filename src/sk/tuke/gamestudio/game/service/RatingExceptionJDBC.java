package sk.tuke.gamestudio.game.service;

import sk.tuke.gamestudio.game.entity.Rating;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class RatingExceptionJDBC implements RatingService{
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "alexej1";

    public static final String INSERT_RATING =
            "INSERT INTO rating (player,game,rating, ratedon) VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (player,game) DO UPDATE" +
                    "SET VALUES (?, ?, ?, ?)";

    public static final String AVERAGE_RATING =
            "SELECT rating FROM rating WHERE game = ?";

    public static final String SELECT_PLAYER =
            "SELECT rating FROM rating WHERE game=? AND player = ?";


    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(INSERT_RATING)){
                ps.setString(1, rating.getPlayer());
                ps.setString(2, rating.getGame());
                ps.setInt(3, rating.getRating());
                ps.setDate(4, new Date(rating.getRatedon().getTime()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Error saving rating", e);
        }
    }
    @Override
    public int getAverageRating(String game) throws RatingException {
        List<Rating> ratings = new ArrayList<>();
        int averageRating = 0;
        int elementCount = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(AVERAGE_RATING)){
                ps.setString(2, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        averageRating += rs.getInt(3);
                        elementCount++;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading average rating", e);
        }
        return averageRating/elementCount;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        int rating = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_PLAYER)) {
                ps.setString(1, game);
                ps.setString(2, player);
                try(ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        rating = rs.getInt(1);
                }
                }
            }
            } catch (SQLException e) {
                throw new RatingException("Error loading " + player + " rating", e);
            }
            return rating;
        }

    public static void main(String[] args) throws Exception {
        Rating rating = new Rating("duri","lightoff",4,new java.util.Date());
        RatingService ratingService = new RatingExceptionJDBC();
        ratingService.setRating(rating);
        System.out.println(ratingService.getRating("lightoff","a lex"));
    }
}
