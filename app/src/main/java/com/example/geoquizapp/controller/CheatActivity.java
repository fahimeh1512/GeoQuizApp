package com.example.geoquizapp.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.geoquizapp.R;

public class CheatActivity extends AppCompatActivity {
    public static final String EXTRA_IS_CHEATER = "com.example.geoquizapp.controller.isCheater";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.cheat_fragment_container);

        if (fragment == null) {
            QuizFragment quizFragment = new QuizFragment();
            fragmentManager.beginTransaction().add(R.id.cheat_fragment_container, quizFragment).commit();
        }
    }


}