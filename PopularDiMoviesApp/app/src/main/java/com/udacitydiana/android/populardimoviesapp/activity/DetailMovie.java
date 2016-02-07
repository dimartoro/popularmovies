package com.udacitydiana.android.populardimoviesapp.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.udacitydiana.android.populardimoviesapp.R;
import com.udacitydiana.android.populardimoviesapp.fragments.DetailMovieFragment;


public class DetailMovie extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable("URI", getIntent().getData());

            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail_movie, fragment)
                    .commit();

        }

    }

}