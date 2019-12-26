package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.sync.MovieDataSynchronizer;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private MovieDataSynchronizer movieDataSynchronizer;

	@Override
	public List<Movie> search(String queryText) {
		// TODO: Step 4 => Please implement in-memory inverted index to search movie by
		// keyword.
		// You must find a way to build inverted index before you do an actual search.
		// Inverted index would looks like this:
		// -------------------------------
		// | Term | Movie Ids |
		// -------------------------------
		// | Star | 5, 8, 1 |
		// | War | 5, 2 |
		// | Trek | 1, 8 |
		// -------------------------------
		// When you search with keyword "Star", you will know immediately, by looking at
		// Term column, and see that
		// there are 3 movie ids contains this word -- 1,5,8. Then, you can use these
		// ids to find full movie object from repository.
		// Another case is when you find with keyword "Star War", there are 2 terms,
		// Star and War, then you lookup
		// from inverted index for Star and for War so that you get movie ids 1,5,8 for
		// Star and 2,5 for War. The result that
		// you have to return can be union or intersection of those 2 sets of ids.
		// By the way, in this assignment, you must use intersection so that it left for
		// just movie id 5.
		String[] queryWord = queryText.split(" ");
		List<Long> ids = new ArrayList<>();
		try {
			for (int i = 0; i < queryWord.length; i++) {
				List<Long> indexList = movieDataSynchronizer.getIndex().get(queryWord[i].toLowerCase());
				if (indexList != null) {
					if (ids.isEmpty()) {
						ids = indexList;
					} else {
						List<Long> res = new ArrayList<>();
						for (Long L : indexList) {
							if (ids.contains(L)) {
								res.add(L);
							}
						}
						ids = res;
					}
				} else
					return new ArrayList<Movie>();
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return movieRepository.findByIndex(ids);
	}
}
