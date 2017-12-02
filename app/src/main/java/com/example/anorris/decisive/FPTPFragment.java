package com.example.anorris.decisive;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anorris on 2017-12-02.
 */

public class FPTPFragment extends Fragment implements View.OnClickListener{

    private static final String ARGS_NUM_USERS = "number_users";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    private Button mFinishedButton;
    private List<Movie> movies;

    private int votes;
    private int users;

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

        if (mAdapter == null){
            mAdapter = new MovieAdapter(movies);
            mRecyclerView.setAdapter(mAdapter);
        } else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finished_button:
                countVotes();

            default:

        }

    }

    public void countVotes(){
        Movie winner = movies.get(0);
        List<Movie> winners = new ArrayList<>();
        winners.add(winner);

        for (int i = 0; i < movies.size(); i++){
            if(winner.getVotes() < movies.get(i).getVotes()){
                winners.removeAll(winners);
                winner = movies.get(i);
                winners.add(winner);
            } else if (winner.getVotes() == movies.get(i).getVotes() && !winner.equals(movies.get(i))){
                winners.add(movies.get(i));
            }
        }

        String message;
        if (winners.size() == 1){
            message = winner.getTitle() + " is the movie you should watch";
        } else{
            message = "";
            for (int i = 0; i < winners.size(); i++){
                if (i == winners.size()-1) {
                    message += winners.get(i).getTitle();
                } else{
                    message += winners.get(i).getTitle() + " or ";
                }
            }
            message += " are the movies you should watch. Blame this poor voting system for giving you a tie";
        }

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Results:");
        alertDialog.setMessage(message);
        alertDialog.show();


    }


    private class MovieHolder extends RecyclerView.ViewHolder{
        public Movie mMovie;

        private TextView movieTitle;
        private Button voteButton;


        public MovieHolder(View itemView) {
            super(itemView);

            movieTitle = itemView.findViewById(R.id.movie_title);
            voteButton = itemView.findViewById(R.id.vote_button);
        }


        public void bindMovie(final Movie movie){
            mMovie = movie;
            movieTitle.setText(movie.getTitle());
            voteButton.setText("Vote");
            voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (votes < users) {
                        votes += 1;
                        movie.vote();
                    } else{
                        Toast.makeText(getActivity(), "Everyone has voted.", Toast.LENGTH_LONG).show();
                    }
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
            View itemView = LayoutInflater.from(context).inflate(R.layout.movie_fptp_layout,parent,false);
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

    public static FPTPFragment newInstance(int numUsers){
        Bundle args = new Bundle();
        args.putInt(ARGS_NUM_USERS, numUsers);


        FPTPFragment fptpFragment = new FPTPFragment();
        fptpFragment.setArguments(args);
        return fptpFragment;
    }


}
