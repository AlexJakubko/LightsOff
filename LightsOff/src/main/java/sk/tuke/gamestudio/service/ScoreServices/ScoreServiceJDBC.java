package sk.tuke.gamestudio.service.ScoreServices;

import sk.tuke.gamestudio.entity.Score;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ScoreServiceJDBC implements ScoreService {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "alexej1";

    public static final String INSERT_SCORE =
    "INSERT INTO score (game, player, points, playedon) VALUES (?, ?, ?, ?)";

    public static final String SELECT_SCORE =
        "SELECT game, player, points, playedon FROM score WHERE game = ? ORDER BY points ASC LIMIT 5;";

    public static final String UPDATE =
            "UPDATE score SET points = ?"
                    + " where player = ? and game = ?";

    public static final String EXIST =
            "SELECT player,points FROM score"
                    + " where player = ? and game = ?";


    @Override
    public void addScore(Score score) throws ScoreException {
        String statement = null;
        ResultSet rs = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(EXIST)) {
                ps.setString(1, score.getPlayer());
                ps.setString(2, score.getGame());
                rs = ps.executeQuery();
                if (rs.next()) {
                            if(score.getPoints() >=rs.getInt(2))
                            {
                                return;
                            }
                            statement = UPDATE;
                } else statement = INSERT_SCORE;
            }
        } catch (SQLException e) {
            throw new ScoreException("Error saving score", e);
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                if (statement.equals(UPDATE)) {
                    ps.setInt(1, score.getPoints());
                    ps.setString(2, score.getPlayer());
                    ps.setString(3, score.getGame());
                } else {
                    ps.setString(1, score.getGame());
                    ps.setString(2, score.getPlayer());
                    ps.setInt(3, score.getPoints());
                    ps.setDate(4, new Date(score.getPlayedOn().getTime()));
                }
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new ScoreException("Error saving score", e);
            }
        } catch (SQLException e) {
            throw new ScoreException("Error saving score", e);
        }
    }
            @Override
    public List<Score> getBestScores(String game) throws ScoreException {
        List<Score> scores = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_SCORE)){
                    ps.setString(1, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        Score score = new Score(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        scores.add(score);
                    }
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Error loading score", e);
        }
        return scores;
    }

//    public static void main(String[] args) throws Exception {
//        Score score = new Score("LightsOff", "Peter", 11, new java.util.Date());
//        ScoreService scoreService = new ScoreServiceJDBC();
//        scoreService.addScore(score);
//        System.out.println(scoreService.getBestScores("LightsOff"));
//    }
}
