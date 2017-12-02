package com.example.anorris.decisive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by anorris on 2017-12-02.
 */

public class CondorcetFragment extends Fragment {

    public static final String ARG_MOVIE_LIST = "movies_list";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_condorcet_layout, container, false);

        return view;

    }


    public static CondorcetFragment newInstance(){
        CondorcetFragment condorcetFragment = new CondorcetFragment();
        return condorcetFragment;
    }
}
