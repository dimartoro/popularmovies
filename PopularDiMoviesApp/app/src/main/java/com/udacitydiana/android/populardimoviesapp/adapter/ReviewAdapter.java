package com.udacitydiana.android.populardimoviesapp.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacitydiana.android.populardimoviesapp.R;
import com.udacitydiana.android.populardimoviesapp.rest.model.Review;

import java.util.List;


public class ReviewAdapter extends ArrayAdapter<Review> {

    private Context context;

    public ReviewAdapter(Activity context,List<Review> reviewCollection){
        super(context,0,reviewCollection);
        this.context=context;

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){

        Review review=getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, viewGroup,false);
        }


        TextView txtAuthor = (TextView)view.findViewById(R.id.txt_author_review_list);

        TextView txtReview = (TextView)view.findViewById(R.id.txt_review_user);

        txtAuthor.setText(review.getReviewAuthor());
        txtReview.setText(review.getReviewContent());


        return view;



    }


}
