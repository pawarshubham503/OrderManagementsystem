package io.javabrains.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		//get all rated movie Ids
		

		
UserRating ratings= restTemplate.getForObject("http://localhost:8082/ratingsdata/users"+userId,UserRating.class);
				
		
		//for each movie id ,call movie info service and get details
		
		 return ratings.getUserRating().stream()
	                .map(rating -> {
	                  Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
						/*
						 * Movie movie =webClientBuilder.build()       //api call alternate (restTemplate)using
						 * webClient builder .get()                  //i will do a get
						 * .uri("http://localhost:8082/movies/")                 //the url we need to access
						 * .retrieve()//go do the fetch .bodyToMono(Movie.class)        //whatever body u get
						 *                                 back convert it into of this class ie movie.class .block();
						 */
	                	return new CatalogItem(movie.getName(), "Description", rating.getRating());
	                })
	                .collect(Collectors.toList());

	}
}
