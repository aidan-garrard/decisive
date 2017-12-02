package com.example.anorris.decisive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by anorris on 2017-12-02.
 */

public class VotingActivity extends AppCompatActivity{

    public static final String EXTRA_VOTING_SYSTEM = "com.anorris.android.decisive.voting_id";
    public static final String EXTRA_NUMBER_USERS = "com.anorris.android.decisive.number_users";

    public static Intent newIntent(Context packageContext, int votingSystem, int numUsers){
        Intent intent = new Intent(packageContext, VotingActivity.class);
        intent.putExtra(EXTRA_VOTING_SYSTEM, votingSystem);
        intent.putExtra(EXTRA_NUMBER_USERS, numUsers);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_layout);

        int votingSystem = (int) getIntent().getSerializableExtra(EXTRA_VOTING_SYSTEM);
        int numUsers = (int) getIntent().getSerializableExtra(EXTRA_NUMBER_USERS);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.voting_fragment_container);

        if (fragment == null){
            fragment = FPTPFragment.newInstance(numUsers);
            fragmentManager.beginTransaction().add(R.id.voting_fragment_container, fragment).commit();
        }
    }
}
