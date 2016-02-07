package com.udacitydiana.android.populardimoviesapp.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacitydiana.android.populardimoviesapp.R;
import com.udacitydiana.android.populardimoviesapp.rest.model.Trailer;

import java.util.List;



public class TrailerAdapter  extends ArrayAdapter<Trailer> {

    private Context context;

    public TrailerAdapter(Activity context,List<Trailer> trailerCollection){
        super(context,0,trailerCollection);
        this.context=context;

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){

        Trailer trailer=getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trailer, viewGroup,false);
        }

        final ImageView iconView = (ImageView)view.findViewById(R.id.img_trailer);
        TextView title = (TextView)view.findViewById(R.id.title_trailer);


        Picasso
                .with(context)
                .load(R.mipmap.ic_arrowplay)

                .into(iconView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {


                    }
                });

        title.setText(trailer.getTrailerName());

        return view;



    }



}
