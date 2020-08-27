package com.example.geoquizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoquizapp.controller.QuestionAdapter;
import com.example.geoquizapp.model.Question;
import com.example.geoquizapp.repository.QuestionRepository;

import java.util.List;

public class QuizListFragment extends Fragment {
    public static final String EXTRA_QUESTION_ID = "com.example.geoquizapp.questionId";

    private RecyclerView  mRecyclerView;
    private QuestionRepository mQuestionRepository;

    public QuizListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets instance from questions repository
        mQuestionRepository = QuestionRepository.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_list, container, false);

        findViews(view);
        initViews(view);

        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_quiz_list);
    }

    private void initViews(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Question> questions = mQuestionRepository.getQuestionBank();

        // set adapter
        mRecyclerView.setAdapter(new QuestionAdapter(questions, getContext()));
    }
}
