package com.udacitydiana.android.populardimoviesapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.udacitydiana.android.populardimoviesapp.adapter.MovieCursorAdapter;
import com.udacitydiana.android.populardimoviesapp.data.MovieContract;
import com.udacitydiana.android.populardimoviesapp.rest.component.InjectHelper;
import com.udacitydiana.android.populardimoviesapp.rest.model.Movie;
import com.udacitydiana.android.populardimoviesapp.rest.model.MovieResult;
import com.udacitydiana.android.populardimoviesapp.rest.service.ApiMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
/**
 * A placeholder fragment containing a simple view.
 */
public  class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public interface Callback{

        public void onMovieSelected(Uri movieUri);
        public void onMovieFirst(Uri movieUri);

    }

    Cursor firstCursor = null;
    private  GridView gridView;
    private static final int FORECAST_LOADER = 0;
    private  static String FIRST_LOAD_MOVIE = "0";
    private Uri uriMovie;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";


    @Inject
    ApiMovie apiMovie;

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();


    private ArrayList<Movie> movieList=new ArrayList<Movie>();
    //private MovieAdapter movieAdapter;
    private MovieCursorAdapter movieAdapter;

    public MovieFragment() {
    }


    void onMovieChanged( ) {
        updateMovies();
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        InjectHelper.getRootComponent().inject(this);

        //getLoaderManager().initLoader(FORECAST_LOADER, null, this);



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putParcelableArrayList("movies", movieList);

        if(mPosition!=GridView.INVALID_POSITION){
            outState.putInt(SELECTED_KEY,mPosition);
        }

        super.onSaveInstanceState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View fragmento = inflater.inflate(R.layout.fragment_movie, container, false);

        movieAdapter = new MovieCursorAdapter(getActivity(), null, 0);

        gridView = (GridView)fragmento.findViewById(R.id.gridView);
        gridView.setAdapter(movieAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String movieID;

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String showMovies = prefs.getString(getString(R.string.pref_selection_movie_key), getString(R.string.pref_selection_movie_default));


                    if (showMovies.equals("all_movies")) {
                        movieID = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                        uriMovie = MovieContract.MovieEntry.buildMovieWithID(movieID);
                    } else {
                        movieID = cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID));
                        uriMovie = MovieContract.FavoriteEntry.buildMovieWithID(movieID);
                    }



                    /*
                    Intent intent;
                    intent = new Intent(getActivity(), DetailMovie.class)
                            .setData(uriMovie);
                    startActivity(intent);
                    */

                    ((Callback) getActivity()).onMovieSelected(uriMovie);
                }
                mPosition = position;
            }
        });

        if(savedInstanceState!=null && savedInstanceState.containsKey(SELECTED_KEY)){
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }




        if(mPosition!=ListView.INVALID_POSITION){
            gridView.setSelection(mPosition);
        }


        return fragmento;
    }



    public Uri getUriMovie(){
        return uriMovie;
    }

    public void updateMovies(){
        //movieAdapter.swapCursor(null);
        getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String showMovies = prefs.getString(getString(R.string.pref_selection_movie_key), getString(R.string.pref_selection_movie_default));

        if(showMovies.equals("all_movies")) {



            String direction = prefs.getString(getString(R.string.pref_order_direction_key), getString(R.string.pref_order_direction_default));
            String field = prefs.getString(getString(R.string.pref_order_field_key), getString(R.string.pref_order_field_default));
            String orderByField = field + "." + direction;

            String apiKey = getActivity().getString(R.string.api_value_key_movies);


            Call<MovieResult> callResultMovie = apiMovie.getResultMovie(orderByField, apiKey);

            callResultMovie.enqueue(new retrofit.Callback<MovieResult>() {

                @Override
                public void onResponse(Response<MovieResult> response, Retrofit retrofit) {
                    MovieResult movieResult = response.body();
                    List<Movie> listMovies = movieResult.getResults();

                    Vector<ContentValues> cVVector = new Vector<ContentValues>(listMovies.size());
                    Movie movie = null;
                    for (int i = 0; i < listMovies.size(); i++) {
                        movie = listMovies.get(i);
                        ContentValues movieValues = new ContentValues();
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());

                        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                        cVVector.add(movieValues);


                    }

                    int inserted = 0;
                    // add to database
                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        inserted = getActivity().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
                    }




                }

                @Override
                public void onFailure(Throwable t) {

                }

            });

        }



    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);



    }

    public void takeFirstMovie(){


    }



    @Override
    public void onStart(){
        super.onStart();

        updateMovies();
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String direction = prefs.getString(getString(R.string.pref_order_direction_key), getString(R.string.pref_order_direction_default));
        String field = prefs.getString(getString(R.string.pref_order_field_key), getString(R.string.pref_order_field_default));
        String orderByField = field+"."+direction;




        String showMovies = prefs.getString(getString(R.string.pref_selection_movie_key), getString(R.string.pref_selection_movie_default));

        Uri showUriMovie = null;

        if(showMovies.equals("all_movies")){
            showUriMovie=MovieContract.MovieEntry.CONTENT_URI;
        }else{
            showUriMovie=MovieContract.FavoriteEntry.CONTENT_URI;

        }


        return new CursorLoader(getActivity(),
                showUriMovie,
                null,
                null,
                null,
                orderByField);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        movieAdapter.swapCursor(cursor);


        gridView.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (FIRST_LOAD_MOVIE.equals("0") ) {


                    if(movieAdapter!=null) {
                        if(!movieAdapter.isEmpty()) {

                            firstCursor = (Cursor) movieAdapter.getItem(0);
                            FIRST_LOAD_MOVIE = "1";
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            String showMovies = prefs.getString(getString(R.string.pref_selection_movie_key), getString(R.string.pref_selection_movie_default));

                            String movieID;
                            Uri uriDefaultMovie;

                            if (showMovies.equals("all_movies")) {
                                movieID = firstCursor.getString(firstCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                                uriDefaultMovie = MovieContract.MovieEntry.buildMovieWithID(movieID);
                            } else {
                                movieID = firstCursor.getString(firstCursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID));
                                uriDefaultMovie = MovieContract.FavoriteEntry.buildMovieWithID(movieID);
                            }

                            ((Callback) getActivity()).onMovieFirst(uriDefaultMovie);
                        }

                    }

                }
            }
        },1000);



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


        movieAdapter.swapCursor(null);
    }






}