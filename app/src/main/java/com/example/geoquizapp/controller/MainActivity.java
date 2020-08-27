package com.example.geoquizapp.controller;

import androidx.fragment.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new QuizFragment();
    }

}