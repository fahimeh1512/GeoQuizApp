package com.example.geoquizapp.controller;

import androidx.fragment.app.Fragment;

import com.example.geoquizapp.QuizListFragment;

public class QuizListActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new QuizListFragment();
    }
}
