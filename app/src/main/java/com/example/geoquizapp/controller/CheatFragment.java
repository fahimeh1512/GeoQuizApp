package com.example.geoquizapp.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.geoquizapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheatFragment
 * #newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheatFragment extends Fragment {

    private TextView mTextViewAnswer;
    private Button mShowCheat;
    private boolean mIsAnswerTrue;

    public CheatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cheat, container, false);
        Intent intent = getActivity().getIntent();
        mIsAnswerTrue = intent.getBooleanExtra(QuizFragment.EXTRA_QUESTION_ANSWER, false);
        findViews(view);
        setListeners();

        return view;
    }

    private void findViews(View view) {
        mTextViewAnswer = view.findViewById(R.id.txtview_answer);
        mShowCheat = view.findViewById(R.id.btn_show_answer);
    }

    private void setListeners() {
        mShowCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsAnswerTrue)
                    mTextViewAnswer.setText(R.string.button_true);
                else
                    mTextViewAnswer.setText(R.string.button_false);

                setCheatResult(true);
            }
        });
    }

    private void setCheatResult(boolean isCheater) {
        Intent intent = new Intent();
        intent.putExtra(CheatActivity.EXTRA_IS_CHEATER, isCheater);

        getActivity().setResult(getActivity().RESULT_OK, intent);
    }
}