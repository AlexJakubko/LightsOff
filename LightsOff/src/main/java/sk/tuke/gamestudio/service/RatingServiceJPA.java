package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.merge(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        int averageRating=(int) Math.round((float)entityManager.createQuery("SELECT AVG(e.rating) FROM Rating e WHERE e.game=:game")
                .setParameter("game",game).getSingleResult());
        return averageRating;

    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Rating playersRating = (Rating) entityManager.createNamedQuery("Rating.getRating").setParameter("game", game)
                .setParameter("player", player).getSingleResult();

        return playersRating.getRating();
    }
}
