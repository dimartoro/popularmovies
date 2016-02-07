package com.udacitydiana.android.populardimoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.udacitydiana.android.populardimoviesapp.activity.DetailMovie;
import com.udacitydiana.android.populardimoviesapp.activity.SettingsActivity;
import com.udacitydiana.android.populardimoviesapp.fragments.DetailMovieFragment;


public class MainActivity extends AppCompatActivity implements MovieFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    boolean mTwoPane=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(findViewById(R.id.container_detail_movie)!=null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_detail_movie, new DetailMovieFragment(),DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else{
            mTwoPane=false;
        }



//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new MovieFragment())
//                    .commit();
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //String location = Utility.getPreferredLocation( this );
        // update the location in our second pane using the fragment manager
//        if (location != null && !location.equals(mLocation)) {
//            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
//            if ( null != ff ) {
//                ff.onLocationChanged();
//            }
//            mLocation = location;
//        }
        //FragmentManager ff = getFragmentManager();
        //MovieFragment movieFragment = (MovieFragment)ff.findFragmentById(R.id.fragment_movie);

//       MovieFragment movieFragment = (MovieFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movie);
//
//        if ( null != movieFragment ) {
//            movieFragment.takeFirstMovie();
//
//        }
  /*
        DetailMovieFragment detailMovieFragment = (DetailMovieFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        if(null!=detailMovieFragment){

                detailMovieFragment.onDetailMovieFragmentChanged();

        }
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //    return true;
        //}

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(Uri movieUri) {

        if(mTwoPane){
            Bundle  args = new Bundle();
            args.putParcelable("URI",movieUri);

            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail_movie, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        }else{
            Intent intent = new Intent(this, DetailMovie.class)
                    .setData(movieUri);
            startActivity(intent);

        }

    }

    @Override
    public void onMovieFirst(Uri movieUri) {

        if(mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable("URI", movieUri);
            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(args);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail_movie, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        }

    }
}