package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Rating.getRating",
                query = "SELECT s FROM Rating s WHERE s.player=:player AND s.game=:game"),
        @NamedQuery(name = "Rating.getAverageRating",
                query = "SELECT AVG (s.rating) FROM Rating s WHERE s.game=:game"),
})
public class Rating implements Comparable <Rating> , Serializable {
    @Id
    @GeneratedValue

    private int ident;

    private String player;

    private String game;

    private int rating;

    private Date ratedon;

    public Rating(){
    }

    public Rating(String player, String game, int rating, Date ratedon) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedon = ratedon;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedon() {
        return ratedon;
    }

    public void setRatedon(Date ratedon) {
        this.ratedon = ratedon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rating{");
        sb.append("player='").append(player).append('\'');
        sb.append(", game='").append(game).append('\'');
        sb.append(", rating=").append(rating);
        sb.append(", ratedon=").append(ratedon);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Rating o) {
        if(getRatedon() == null || o.getRatedon() == null) return 0;
        return getRatedon().compareTo(o.getRatedon());
    }

}
