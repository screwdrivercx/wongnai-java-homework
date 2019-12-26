package com.wongnai.interview.movie.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Component
public class MovieDataServiceImpl implements MovieDataService {
	public static final String MOVIE_DATA_URL
			= "https://raw.githubusercontent.com/prust/wikipedia-movie-data/master/movies.json";

	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public MoviesResponse fetchAll() {
		//TODO:
		// Step 1 => Implement this method to download data from MOVIE_DATA_URL and fix any error you may found.
		// Please noted that you must only read data remotely and only from given source,
		// do not download and use local file or put the file anywhere else.
		MoviesResponse res = new MoviesResponse();
		try {
			JSONArray jsonArr = readJSONfromURL(MOVIE_DATA_URL);

			for(int i=0;i<jsonArr.length();i++){
				JSONObject object = jsonArr.getJSONObject(i);
				MovieData movie = new MovieData();

				List<String> cast = new ArrayList<>();
				JSONArray castList = object.getJSONArray("cast");
				for (int j=0; j<castList.length(); j++){
					cast.add(castList.get(j).toString());
				}

				List<String> genres = new ArrayList<>();
				JSONArray genresList = object.getJSONArray("genres");
				for (int j=0; j<genresList.length(); j++){
					genres.add(genresList.get(j).toString());
				}

				movie.setTitle(object.getString("title"));
				movie.setYear(object.getInt("year"));
				movie.setCast(cast);
				movie.setGenres(genres);

				res.add(movie);
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONArray readJSONfromURL(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
}
