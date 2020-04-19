package sk.tuke.gamestudio;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentServices.CommentService;
import sk.tuke.gamestudio.service.CommentServices.CommentServiceRestClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class CommentServiceTest {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "alexej1";
    public static final String CLEAR_DB ="delete from comment";

    private CommentService service = new CommentServiceRestClient();

    @Test
    public void addComment() throws Exception {
        clearDb();
        Comment comment = new Comment("Alex", "LightsOff", "Super hra!", new Date());
        service.addComment(comment);
        comment = new Comment("Jaro", "LightsOff", "Skvela hra!", new Date());
        service.addComment(comment);

        List<Comment> comments = service.getComments("LightsOff");

        assertEquals(2, comments.size());

        assertEquals("Alex", comments.get(1).getPlayer());
        assertEquals("Super hra!", comments.get(1).getComment());

        assertEquals("Jaro", comments.get(0).getPlayer());
        assertEquals("Skvela hra!", comments.get(0).getComment());
    }

    @Test
    public void getComments() throws Exception {
        clearDb();
        Comment comment = new Comment("Alex", "LightsOff", "Super hra!", new Date());
        service.addComment(comment);
        comment = new Comment("Jaro", "LightsOff", "Skvela hra!", new Date());
        service.addComment(comment);

        List<Comment> comments = service.getComments("LightsOff");


        assertEquals("Alex", comments.get(1).getPlayer());
        assertEquals("Super hra!", comments.get(1).getComment());

        assertEquals("Jaro", comments.get(0).getPlayer());
        assertEquals("Skvela hra!", comments.get(0).getComment());
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