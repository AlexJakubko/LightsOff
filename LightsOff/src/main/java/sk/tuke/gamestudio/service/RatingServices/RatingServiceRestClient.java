package sk.tuke.gamestudio.service.RatingServices;

import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;


public class RatingServiceRestClient implements RatingService {
    private static final String URL = "http://localhost:8080/api/rating";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setRating(Rating rating){
        restTemplate.postForEntity(URL, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) {
        return restTemplate.getForEntity(URL + "/" + game,int.class).getBody();
    }
    @Override
    public int getRating(String game, String player) {
        return restTemplate.getForEntity(URL + "/" + game+"/"+player,int.class).getBody();
    }
}

