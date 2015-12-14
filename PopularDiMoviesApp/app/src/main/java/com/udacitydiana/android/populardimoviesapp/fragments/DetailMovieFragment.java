package com.udacitydiana.android.populardimoviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacitydiana.android.populardimoviesapp.R;
import com.udacitydiana.android.populardimoviesapp.model.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment {
    final String PATH_BASE_IMG= "http://image.tmdb.org/t/p/w185/";

    public DetailMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragmento= inflater.inflate(R.layout.fragment_detail_movie, container, false);

        TextView txtTitle=  (TextView) fragmento.findViewById(R.id.txt_title_movie_detail);



        ImageView posterImage = (ImageView) fragmento.findViewById(R.id.img_movie_detail);

        TextView txtDate=  (TextView) fragmento.findViewById(R.id.txt_date_release);

        TextView txtRating=  (TextView) fragmento.findViewById(R.id.txt_rating_release);

        TextView txtOverview=  (TextView) fragmento.findViewById(R.id.txt_overview_movie_detail);


        Intent intent = getActivity().getIntent();

        if (intent != null) {
            Movie movieItem = (Movie) intent.getParcelableExtra("com.udacitydiana.android.populardimoviesapp.model.Movie");


            txtTitle.setText(movieItem.title);

            txtDate.setText("Date : " +movieItem.getReleaseDate());

            txtRating.setText("Rating : " + String.valueOf(movieItem.rating)  );

            txtOverview.setText(movieItem.synopsis);


            Picasso
                    .with(getActivity())
                    .load(PATH_BASE_IMG+movieItem.posterPath)
                    .into(posterImage);





        }



        return fragmento;
    }
}