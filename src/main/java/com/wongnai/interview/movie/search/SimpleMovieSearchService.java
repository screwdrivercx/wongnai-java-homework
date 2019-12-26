package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		// TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class

		List<Movie> result = new ArrayList<>();
		if (!queryText.contains(" ")) {
			MoviesResponse movieList = movieDataService.fetchAll();
			for (int i = 0; i < movieList.size(); i++) {
				String movieTitle = movieList.get(i).getTitle();
				Boolean found = Arrays.asList(movieTitle.toLowerCase().split(" ")).contains(queryText.toLowerCase());
				if (found) {
					List<String> movieActors = movieList.get(i).getCast();
					Movie movie = new Movie(movieTitle);
					movie.setActors(movieActors);
					result.add(movie);
				}
			}
		}
		return result;
	}
}
