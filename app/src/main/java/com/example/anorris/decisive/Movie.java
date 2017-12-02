package com.example.anorris.decisive;

/**
 * Created by anorris on 2017-12-02.
 */

public class Movie {

    private String title;
    private int rank;
    private int votes;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void vote(){
        votes += 1;
    }

    public int getVotes(){
        return votes;
    }
}
