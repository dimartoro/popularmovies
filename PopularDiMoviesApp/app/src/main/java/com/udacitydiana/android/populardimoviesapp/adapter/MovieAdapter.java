package com.udacitydiana.android.populardimoviesapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacitydiana.android.populardimoviesapp.R;
import com.udacitydiana.android.populardimoviesapp.model.Movie;

import java.util.List;

/**
 * Created by Dimartoro on 12/12/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie>{

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Context context;

    public MovieAdapter(Activity context,List<Movie>movieCollection){
        super(context,0,movieCollection);
        this.context=context;

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){

        Movie movie=getItem(position);

        final String PATH_BASE_IMG= "http://image.tmdb.org/t/p/w185/";

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, viewGroup,false);
            }

        ImageView iconView = (ImageView)view.findViewById(R.id.img_movie);
        TextView title = (TextView)view.findViewById(R.id.title_movie);

        Picasso
                .with(context)
                .load(PATH_BASE_IMG+movie.posterPath)
                .into(iconView);

        title.setText(movie.title);

        return view;



    }

}
