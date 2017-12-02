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

    private static final String ARGS_NUM_USERS = "number_users";
    public static final String TAG = "CondercetFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mFinishedButton;
    private List<Movie> movies;
    private Context context;
    private int movieCount;

    private int users;
    private int currentUser = 0;
    private int [][] ranks;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_condorcet_layout, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.condorcet_recycler_view);

        context = getActivity();
        mFinishedButton = (Button) view.findViewById(R.id.finished_button);
        mFinishedButton.setOnClickListener(this);

        users = getArguments().getInt(ARGS_NUM_USERS);

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
                    ranks[currentUser] = userRanks;
                }
                if (currentUser == users-1){
                    Movie chosen = getWinner(ranks);
                }
                else{
                    currentUser += 1;
                }

            default:

        }
    }

    private Movie getWinner(int[][] rankings){

        Log.d(TAG, "Made it to voting" + ranks[0][0]);

        int[] leftover = round(rankings);
        if (leftover.length == 1){
            Movie winner = movies.get(leftover[0]);
        } else {
            int[][] newRankings = new int[users][movieCount];
            for (int i = 0; i< leftover.length; i++){
            }
        }

        return new Movie();
    }
    private int[] round(int[][]rankings) {
        float [] votePercentages = new float[movieCount];
        for (int i = 0; i < movies.size(); i++) {
            int rankedFirst = 0;
            for (int j = 0; i < users; i++) {
                if (rankings[j][i] == 1)
                    rankedFirst++;
            }
            float ratio = rankedFirst / users;
            votePercentages[i] = ratio;
        }
        boolean found = false;
        float minPercentage = votePercentages[0];
        int minIndex = 0;
        for (int i = 0; i < votePercentages.length; i++) {
            int lowest = 0;
            if (votePercentages[i] < minPercentage) {
                minPercentage = votePercentages[i];
                minIndex = i;
            }
            if (votePercentages[i] > 0.5)
                found = true;
                int[] winner = new int[1];
                winner[0] = i;
                return winner;
        }
        int[] leftover = new int[movieCount -1];
        movieCount--;
        for (int i=0; i<movieCount; i++){
            if (i > minIndex){
                leftover[i-1] = i;
            } else if (i < minIndex){
                leftover[i] = i;
            }
        }
        return leftover;
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



    public static CondorcetFragment newInstance(int numUsers){
        Bundle args = new Bundle();
        args.putInt(ARGS_NUM_USERS, numUsers);


        CondorcetFragment condorcetFragment = new CondorcetFragment();
        condorcetFragment.setArguments(args);
        return condorcetFragment;
    }
}

