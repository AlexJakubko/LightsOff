package sk.tuke.gamestudio.service.CommentServices;

import sk.tuke.gamestudio.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment){
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String game){
        return entityManager.createNamedQuery("Comment.getComments")
                .setParameter("game", game).setMaxResults(5).getResultList();
    }
}
