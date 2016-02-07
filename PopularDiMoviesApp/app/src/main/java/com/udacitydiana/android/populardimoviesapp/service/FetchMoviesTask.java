package com.udacitydiana.android.populardimoviesapp.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.udacitydiana.android.populardimoviesapp.R;
import com.udacitydiana.android.populardimoviesapp.adapter.MovieAdapter;
import com.udacitydiana.android.populardimoviesapp.rest.model.Movie;

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
 * Created by Dimartoro on 12/21/15.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private Context mContext;
    private MovieAdapter movieAdapter;

    public FetchMoviesTask(){}

    public FetchMoviesTask(Context mContext0, MovieAdapter movieAdapter0){
        this.mContext=mContext0;
        this.movieAdapter=movieAdapter0;

    }

    public void setContext(Context context){
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null");
        }
        mContext = context;
    }

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

        String format = mContext.getString(R.string.format_value);
        String numberPage = mContext.getString(R.string.page_movie);

        try {
            final String MOVIE_BASE_URL =mContext.getString(R.string.api_base_movies);
            final String SORT_BY_PARAM =mContext.getString(R.string.api_param_sort_movies);
            final String SORT_BY_VALUE =mContext.getString(R.string.api_value_sort_movies);
            final String KEY_PARAM =mContext.getString(R.string.api_param_key_movies);
            final String KEY_VALUE =mContext.getString(R.string.api_value_key_movies);
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

            //movie=new Movie(posterPath,title,overview,raiting,releaseDate,id);

            //movieCollection.add(movie);

        }



        return movieCollection;
    }

}