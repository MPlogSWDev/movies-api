package dev.plog.movies.utility;

import dev.plog.movies.models.Movie;
import dev.plog.movies.models.Review;
import dev.plog.movies.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.SelectionOperators.First.first;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public Review createReview(String reviewBody, String imdbId){
        Review review = repository.insert(new Review(reviewBody, LocalDateTime.now(), LocalDateTime.now()));


        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();

                return review;
    }


}
