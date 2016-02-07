package com.udacitydiana.android.populardimoviesapp.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacitydiana.android.populardimoviesapp.R;
import com.udacitydiana.android.populardimoviesapp.adapter.ReviewAdapter;
import com.udacitydiana.android.populardimoviesapp.adapter.TrailerAdapter;
import com.udacitydiana.android.populardimoviesapp.data.MovieContract;
import com.udacitydiana.android.populardimoviesapp.rest.component.InjectHelper;
import com.udacitydiana.android.populardimoviesapp.rest.model.Movie;
import com.udacitydiana.android.populardimoviesapp.rest.model.Review;
import com.udacitydiana.android.populardimoviesapp.rest.model.ReviewResult;
import com.udacitydiana.android.populardimoviesapp.rest.model.Trailer;
import com.udacitydiana.android.populardimoviesapp.rest.model.TrailerResult;
import com.udacitydiana.android.populardimoviesapp.rest.service.ApiMovie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment implements LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = DetailMovieFragment.class.getSimpleName();
    static final String DETAIL_MOVIE_URI="URI";

    final String PATH_BASE_IMG= "http://image.tmdb.org/t/p/w185/";

    @Bind(R.id.img_movie_detail) ImageView posterImage;
    @Bind(R.id.txt_title_movie_detail) TextView txtTitle;
    @Bind(R.id.txt_date_release) TextView txtDate;
    @Bind(R.id.txt_rating_release) TextView txtRating;
    @Bind(R.id.txt_overview_movie_detail) TextView txtOverview;

    @Bind(R.id.listReviewView) ListView listViewReview;

    private View fragmento;

    private static final int DETAIL_LOADER = 0;

    Movie movie;

    private TrailerAdapter trailerAdapter;
    private ArrayList<Trailer> trailerList = new ArrayList<Trailer>();


    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList = new ArrayList<Review>();

    Uri mUri;

    @Inject
    ApiMovie apiMovie;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    public DetailMovieFragment() {
    }




    //create menu to save favorite
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        InjectHelper.getRootComponent().inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_detail_dropdown_movie, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.action_save_favorite){

            ContentValues favoriteValues = new ContentValues();


            favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
            favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_OVERVIEW, movie.getOverview());
            favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_TITLE, movie.getTitle());


            getActivity().getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI,favoriteValues);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void onDetailMovieFragmentChanged(){
//        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle arguments = getArguments();
        if (arguments!=null)
            mUri=arguments.getParcelable("URI");



        fragmento= inflater.inflate(R.layout.fragment_detail_movie, container, false);
        ButterKnife.bind(this, fragmento);


        reviewAdapter = new ReviewAdapter(getActivity(),reviewList);
        listViewReview.setAdapter(reviewAdapter);



        trailerAdapter = new TrailerAdapter(getActivity(),trailerList);

        ListView listViewTrailers = (ListView)fragmento.findViewById(R.id.listTrailerView);
        listViewTrailers.setAdapter(trailerAdapter);

        listViewTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Trailer trailerItem = trailerAdapter.getItem(position);
                String mVideoId = trailerItem.getTrailerKey();
                String video_path = "http://www.youtube.com/watch?v=" + mVideoId;
                //Uri url = Uri.parse(video_path);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(video_path));
                startActivity(i);


            }
        });



        return fragmento;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Intent intent = getActivity().getIntent();

        /*if (intent == null || intent.getData() == null) {
            return null;
        }*/

        if(null!=mUri){
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null,
                    null,
                    null,
                    null
            );


        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        if (!cursor.moveToFirst()) { return; }

        String movieID = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
        String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
        String rating = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY));
        String voteAverage = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
        String overView = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
        String pathPoster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));

        movie= new Movie();
        movie.setId(Integer.parseInt(movieID));
        movie.setOverview(overView);
        movie.setPosterPath(pathPoster);
        movie.setReleaseDate(releaseDate);
        movie.setTitle(title);
        movie.setVoteAverage(Double.parseDouble(voteAverage));



        txtTitle.setText(title);

        txtDate.setText("Date : " +releaseDate);

        txtRating.setText("Rating : " + voteAverage );

        txtOverview.setText(overView);


        Picasso
                .with(getActivity())
                .load(PATH_BASE_IMG + pathPoster)
                .into(posterImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(getContext())
                                .load(R.mipmap.noimageavailable) // image url goes here
                                .placeholder(posterImage.getDrawable())
                                .into(posterImage);


                    }
                });


        String apiKey = getActivity().getString(R.string.api_value_key_movies);



        Call<TrailerResult> callResultTrailer = apiMovie.getResultTrailer(movieID, apiKey);

        callResultTrailer.enqueue(new retrofit.Callback<TrailerResult>() {
            @Override
            public void onResponse(Response<TrailerResult> response, Retrofit retrofit) {
                TrailerResult trailerResult = response.body();
                List<Trailer> listTrailer = trailerResult.getResults();

                trailerAdapter.clear();


                trailerAdapter.addAll(listTrailer);






            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


        Log.v(LOG_TAG, "movieID URL :" + movieID);
        Log.v(LOG_TAG, "apiKey URL :" + apiKey);


        Call<ReviewResult> callResultReview = apiMovie.getResultReviews(movieID,apiKey);

        callResultReview.enqueue(new retrofit.Callback<ReviewResult>() {
            @Override
            public void onResponse(Response<ReviewResult> response, Retrofit retrofit) {
                ReviewResult reviewResult = response.body();
                Log.v(LOG_TAG, "reviewResult:" + reviewResult.getTotalResults());


                //
                reviewAdapter.clear();

                reviewList = reviewResult.getResults();
                reviewAdapter.addAll(reviewList);


            }

            @Override
            public void onFailure(Throwable t) {

            }
        });



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}