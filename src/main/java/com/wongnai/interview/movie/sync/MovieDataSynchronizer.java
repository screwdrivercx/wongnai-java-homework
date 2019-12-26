package com.wongnai.interview.movie.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;

	private Map<String, List<Long>> index;

	@Transactional
	public void forceSync() {
		//TODO: implement this to sync movie into repository
		//www.baeldung.com/spring-data-crud-repository-save
		MoviesResponse movieList = movieDataService.fetchAll();
		index = new HashMap<>();

		Long i= (long) 0;

		for(MovieData Movie : movieList){
			String title = Movie.getTitle();
			Movie movie = new Movie(title);
			List<String> actors = new ArrayList<>(Movie.getCast());
			movie.setActors(actors);
			movie.setId(i);
			movieRepository.save(movie);
			
			//inverted index
			String[] words = title.split(" ");
			for(int j=0;j<words.length;j++){
				String word = words[j].toLowerCase();
				if(index.containsKey(word)){
					index.get(word).add(i);
				}
				else {
					index.put(word, new ArrayList<>());
					index.get(word).add(i);
				}
			}
			i++;
		}
	}

	public Map<String, List<Long>> getIndex(){
		return this.index;
	}
}
