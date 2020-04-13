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
        List<Rating> ratings= entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game",game).getResultList();
        int averageRating = 0;
        for(Rating rating :ratings){
            averageRating+=rating.getRating();
        }
        return averageRating;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Rating playersRating = (Rating) entityManager.createNamedQuery("Rating.getRating").setParameter("player", player).setParameter("game", game).getSingleResult();
        return playersRating.getRating();
    }
}
