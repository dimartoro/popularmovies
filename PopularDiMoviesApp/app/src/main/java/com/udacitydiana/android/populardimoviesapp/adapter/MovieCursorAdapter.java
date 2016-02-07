package com.udacitydiana.android.populardimoviesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacitydiana.android.populardimoviesapp.R;
import com.udacitydiana.android.populardimoviesapp.data.MovieContract;
import com.udacitydiana.android.populardimoviesapp.util.Constant;



public class MovieCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieCursorAdapter.class.getSimpleName();

    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
        String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));

        final ImageView iconView = (ImageView)view.findViewById(R.id.img_movie);
        TextView txtTitle = (TextView)view.findViewById(R.id.title_movie);

        Picasso
                .with(context)
                .load(Constant.PATH_BASE_IMG + posterPath)

                .into(iconView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Log.v(LOG_TAG, "error loading image   :");
                        Picasso.with(context)
                                .load(R.mipmap.noimageavailable) // image url goes here
                                .placeholder(iconView.getDrawable())
                                .into(iconView);


                    }
                });
        txtTitle.setText(title);


    }
}
