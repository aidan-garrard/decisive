package com.example.anorris.decisive;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mSearchMovie;
    private Spinner mVotingSpinner;
    private Button mBeginVoting;
    private Button mSearchButton;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_grid);
        mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSearchMovie = (EditText) findViewById(R.id.movie_search_bar);
        mSearchMovie.setHint("Search Movie");

        mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apikey = "&apikey=df08b009";
                String movieName = mSearchMovie.getText().toString();
                String[] exploded = movieName.split(" ");
                StringBuilder joined = new StringBuilder();
                for (int i=0; i < exploded.length; i++) {
                    if (exploded[i+1] != null)
                        joined.append(exploded[i]).append("+");
                    else joined.append(exploded[i]);
                }
                String searchString = "http://www.omdbapi.com/?t=" + joined.toString() + apikey;
                URL url = null;
                try {
                    url = new URL(searchString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                URLConnection urlc = null;
                try {
                    urlc = url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                urlc.setDoOutput(true);
                urlc.setAllowUserInteraction(false);
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String l = null;
                try {
                    l = br.readLine();
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Gson g = new Gson();
                Movie thisMovie = g.fromJson(l, Movie.class);
            }
        });

        mVotingSpinner = (Spinner) findViewById(R.id.voting_system_spinner);
        ArrayAdapter<CharSequence> votingSpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.voting_systems, android.R.layout.simple_spinner_item);
        votingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mVotingSpinner.setAdapter(votingSpinnerAdapter);

        mBeginVoting = (Button) findViewById(R.id.begin_voting_button);
        mBeginVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = VotingActivity.newIntent(context, 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        updateUI();
    }

    private void updateUI() {
        MoviePool moviePool = MoviePool.get(this);
        List<Movie> movies = moviePool.getmMovies();

        if (mAdapter == null){
            mAdapter = new MovieAdapter(movies);
            mRecyclerView.setAdapter(mAdapter);
        } else{
            mAdapter.notifyDataSetChanged();
        }
    }

    private class MovieHolder extends RecyclerView.ViewHolder{
        public Movie mMovie;

        private TextView movieTitle;

        public MovieHolder(View itemView) {
            super(itemView);

            movieTitle = itemView.findViewById(R.id.movie_title);
        }

        public void bindMovie(Movie movie){
            mMovie = movie;
            movieTitle.setText(movie.getTitle());
        }
    }

    public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private List<Movie> mMovies;

        public MovieAdapter(List<Movie> movies) {
            mMovies = movies;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View itemView = LayoutInflater.from(context).inflate(R.layout.movie_card_layout,parent,false);
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



}
