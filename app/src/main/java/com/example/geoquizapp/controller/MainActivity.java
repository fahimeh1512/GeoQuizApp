package com.example.geoquizapp.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.geoquizapp.R;

public class MainActivity extends AppCompatActivity {



    /**
     * This method is used to crete ui for activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this method will create the layout
        //inflate: creating object of xml layout
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            QuizFragment quizFragment = new QuizFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, quizFragment).commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
/*
        Log.d(TAG, "onStart log: " + mCurrentIndex);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
//
//        Log.d(TAG, "onResume log: " + mCurrentIndex);
    }

    @Override
    protected void onPause() {
        super.onPause();
//
//        Log.d(TAG, "onPause log: " + mCurrentIndex);
    }

    @Override
    protected void onStop() {
        super.onStop();
/*
        Log.d(TAG, "onStop log: " + mCurrentIndex);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
/*
        Log.d(TAG, "onDestroy log: " + mCurrentIndex);*/
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {

    }*/


}