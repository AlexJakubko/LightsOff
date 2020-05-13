package sk.tuke.gamestudio.service.RatingServices;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        if (getRating(rating.getGame(), rating.getPlayer()) != -1) {
            entityManager.createNamedQuery("Rating.setRating").setParameter("rating", rating.getRating()).setParameter("player", rating.getPlayer()).setParameter("game", rating.getGame()).executeUpdate();
        } else {
            entityManager.persist(rating);
        }
    }


    @Override
    public int getAverageRating(String game) {
        try {
            double averageRating = (double) entityManager.createNamedQuery("Rating.getAverageRating")
                    .setParameter("game", game).getSingleResult();
            return (int) Math.round(averageRating);

        } catch (NullPointerException e) {
            System.out.println("null in ratings");
        }
        return 0;
    }

    @Override
    public int getRating(String game, String player) {
        Rating playersRating;
        try {
            playersRating = (Rating) entityManager.createNamedQuery("Rating.getRating").setParameter("player", player).setParameter("game", game).getSingleResult();
        } catch (Exception e) {
            return -1;
        }
        return playersRating.getRating();
    }
}
