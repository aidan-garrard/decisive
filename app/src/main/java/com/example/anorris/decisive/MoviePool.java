package com.example.anorris.decisive;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anorris on 2017-12-02.
 */

public class MoviePool{

    private static MoviePool sMoviePool;
    private List<Movie> mMovies;
    private Context mContext;

    public static MoviePool get(Context context) {
        if (sMoviePool == null) {
            sMoviePool = new MoviePool(context);
        }
        return sMoviePool;
    }

    private MoviePool(Context context){
        mMovies = new ArrayList<>();
    }

    public List<Movie> getmMovies(){
        return mMovies;
    }
}
