package sk.tuke.gamestudio.service;

import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Arrays;
import java.util.List;

public class RatingServiceRestClient implements RatingService {
    private static final String URL = "http://localhost:8080/api/rating";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setRating(Rating rating){
        restTemplate.postForEntity(URL, rating, Rating.class);
    }

//    @Override
//    public int getAverageRating(String game) {
//
//    }

    @Override
    public int getRating(String game, String player) {
        return 0;
    }
}

