package com.example.anorris.decisive;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by anorris on 2017-12-02.
 */

public class CondorcetFragment extends Fragment implements View.OnClickListener{

    public static final String ARG_MOVIE_LIST = "movies_list";
    public static final String TAG = "CondercetFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mFinishedButton;
    private List<Movie> movies;
    private Context context;
    private int movieCount;

    int users = 5;
    int currentUser = 0;
    int [][] ranks;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_condorcet_layout, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.condorcet_recycler_view);

        context = getActivity();
        mFinishedButton = (Button) view.findViewById(R.id.finished_button);
        mFinishedButton.setOnClickListener(this);

        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity()); // set up the layout manager for RecyclerViewer
        mRecyclerView.setLayoutManager(mLayoutManager);

        updateUI();
    }

    private void updateUI(){
        MoviePool moviePool = MoviePool.get(getActivity());
        List<Movie> movies = moviePool.getmMovies();
        this.movies = movies;
        movieCount = movies.size();
        ranks = new int[movieCount][users];

        if (mAdapter == null){
            mAdapter = new MovieAdapter(movies);
            mRecyclerView.setAdapter(mAdapter);
        } else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finished_button:
                int[] userRanks = new int[movieCount];
                int i = 0;
                for (Movie movie : movies) {
                    userRanks[i] = movie.getRank();
                    i++;
                }
                for (int j=0; j < movieCount; j++){
                    ranks[j][currentUser] = userRanks[j];
                }
                if (currentUser == users-1){
                    Movie chosen = Vote(ranks);
                }
                else{
                    currentUser += 1;
                }

            default:

        }
    }

    private Movie Vote(int[][] rankings){

        Log.d(TAG, "Made it to voting" + ranks[0][0]);
        return new Movie();
    }

    private class MovieHolder extends RecyclerView.ViewHolder{
        public Movie mMovie;

        private TextView movieTitle;
        private Spinner votingSpinner;

        public MovieHolder(View itemView) {
            super(itemView);

            movieTitle = itemView.findViewById(R.id.movie_title);
            votingSpinner = itemView.findViewById(R.id.voting_rank_spinner);
            Integer[] items = new Integer[movieCount];
            for (int i = 0; i<movieCount; i++){
                items[i] = i+1;
            }
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                    getActivity(), android.R.layout.simple_spinner_item, items);
            votingSpinner.setAdapter(adapter);

        }

        public void bindMovie(final Movie movie){
            mMovie = movie;
            movieTitle.setText(movie.getTitle());

            votingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int rank = Integer.parseInt(votingSpinner.getSelectedItem().toString());
                    movie.setRank(rank);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }


    public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private List<Movie> mMovies;

        public MovieAdapter(List<Movie> movies) {
            mMovies = movies;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View itemView = LayoutInflater.from(context).inflate(R.layout.movie_condorcet_layout,parent,false);
            return new MovieHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position){
            Movie movie = mMovies.get(position);
            holder.bindMovie(movie);
        }

        @Override
        public int getItemCount(){
            return mMovies.size();
        }
    }



    public static CondorcetFragment newInstance(){
        CondorcetFragment condorcetFragment = new CondorcetFragment();
        return condorcetFragment;
    }
}

