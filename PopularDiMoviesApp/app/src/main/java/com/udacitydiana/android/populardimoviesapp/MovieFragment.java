package com.udacitydiana.android.populardimoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.udacitydiana.android.populardimoviesapp.activity.DetailMovie;
import com.udacitydiana.android.populardimoviesapp.adapter.MovieAdapter;
import com.udacitydiana.android.populardimoviesapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public  class MovieFragment extends Fragment {

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();


    private ArrayList<Movie> movieList=new ArrayList<Movie>();
    private MovieAdapter movieAdapter;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {



        View fragmento = inflater.inflate(R.layout.fragment_movie, container, false);
        movieAdapter = new MovieAdapter(getActivity(), movieList);



        GridView gridView = (GridView)fragmento.findViewById(R.id.gridView);
        gridView.setAdapter(movieAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movieItem = movieAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailMovie.class)
                        .putExtra("com.udacitydiana.android.populardimoviesapp.model.Movie", movieItem);
                startActivity(intent);


                //Toast.makeText(getActivity(),forecast,Toast.LENGTH_SHORT);
            }
        });

        return fragmento;
    }

    public void updateMovies(){
        FetchMoviesTask movieTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String direction = prefs.getString(getString(R.string.pref_order_direction_key), getString(R.string.pref_order_direction_default));
        String field = prefs.getString(getString(R.string.pref_order_field_key), getString(R.string.pref_order_field_default));
        String orderByField = field+"."+direction;


        movieTask.execute(orderByField);
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<Movie>  doInBackground(String... params) {
            //String[] movieFromJson=null;

            ArrayList<Movie> movieFromJson=null;

            if(params.length==0){
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            String format = getString(R.string.format_value);
            String numberPage = getString(R.string.page_movie);

            try {
                final String MOVIE_BASE_URL =getString(R.string.api_base_movies);
                final String SORT_BY_PARAM =getString(R.string.api_param_sort_movies);
                final String SORT_BY_VALUE =getString(R.string.api_value_sort_movies);
                final String KEY_PARAM =getString(R.string.api_param_key_movies);
                final String KEY_VALUE =getString(R.string.api_value_key_movies);
                final String ORDER_BY ="sort_by";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(ORDER_BY, params[0])
                        .appendQueryParameter(KEY_PARAM, KEY_VALUE)
                        .build();

                Log.v(LOG_TAG, "builtUri:" + builtUri);


                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    moviesJsonStr = null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in pars
                    moviesJsonStr = null;
                }
                moviesJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast JSON String:" + moviesJsonStr);

                try {
                    movieFromJson = getMovieDataFromJson(moviesJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            } catch (IOException e) {
                Log.e("Movie Fragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                moviesJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Movie Fragment", "Error closing stream", e);
                    }
                }
            }




            return movieFromJson;
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> arrayMovies) {
            if (arrayMovies != null) {
                movieAdapter.clear();
                for(Movie mov : arrayMovies) {
                    movieAdapter.add(mov);
                }
            }
        }



    }


    private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)throws JSONException {


        JSONObject forecastJson = new JSONObject(movieJsonStr);



        JSONArray movieArray = forecastJson.getJSONArray("results");

        ArrayList<Movie> movieCollection=new ArrayList<Movie>();
        Movie movie=null;


        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movieObject = movieArray.getJSONObject(i);
            String title = movieObject.getString("title");
            String overview = movieObject.getString("overview");
            double raiting = movieObject.getDouble("vote_average");
            String posterPath=movieObject.getString("poster_path");
            String releaseDate=movieObject.getString("release_date");
            int id=movieObject.getInt("id");

            movie=new Movie(posterPath,title,overview,raiting,releaseDate,id);

            movieCollection.add(movie);

        }



        return movieCollection;
    }



}
