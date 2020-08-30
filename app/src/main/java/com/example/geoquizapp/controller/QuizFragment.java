package com.example.geoquizapp.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquizapp.QuizListFragment;
import com.example.geoquizapp.R;
import com.example.geoquizapp.repository.QuestionRepository;

import java.util.UUID;


public class QuizFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private static final String BUNDLE_KEY_CURRENT_INDEX = "currentIndex";
    private static final String BUNDLE_KEY_SCORE = "score";
    private static final String BUNDLE_KEY_ANSWERED_QUESTIONS = "answeredQuestions";
    private static final String BUNDLE_KEY_COUNT_OF_ANSWERS = "countOfAnswers";
    public static final String EXTRA_QUESTION_ANSWER = "com.example.geoquizapp.controller.questionAnswer";
    public static final String EXTRA_QUESTION_IS_ANSWERED = "com.example.geoquizapp.controller.questionIsAnswered";
    private static final int REQUEST_CODE_CHEAT = 0;
    private CheatFragment cheatFragment;

    private View view1;
    private View view2;
    private TextView mTextViewQuestion;
    private ImageButton mButtonTrue;
    private ImageButton mButtonFalse;
    private ImageButton mButtonNext;
    private ImageButton mButtonPrev;
    private ImageButton mButtonFirst;
    private ImageButton mButtonLast;
    // For score layout
    private ImageButton mButtonRefresh;
    private Button mCheatButton;
    private TextView mTextTrue;
    private TextView mTextFalse;
    // For score layout
    private TextView mTextDigit;
    private int mScore = 0;
    private int mCountOfAnswers = 0;
    private int mCurrentIndex = 0;

    private QuestionRepository mQuestionBank;

    private boolean[] mIsAnsweredQuestions = new boolean[6];

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets instance from questions repository
        mQuestionBank = QuestionRepository.getInstance();

        mCurrentIndex = getActivity().getIntent().getIntExtra(QuizListFragment.QuestionAdapter.EXTRA_QUESTION_INDEX, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        cheatFragment = (CheatFragment) fragmentManager.findFragmentById(R.id.cheat_fragment_container);

        //if we want to change logic we must first find the view objects (it must have "id")
        findViews(view);
        setListeners();

        updateQuestion();

        if (savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState: " + savedInstanceState);
            mCurrentIndex = savedInstanceState.getInt(BUNDLE_KEY_CURRENT_INDEX);
            mScore = savedInstanceState.getInt(BUNDLE_KEY_SCORE);
            mCountOfAnswers = savedInstanceState.getInt(BUNDLE_KEY_COUNT_OF_ANSWERS);
            mIsAnsweredQuestions = (boolean[])savedInstanceState.getSerializable(BUNDLE_KEY_ANSWERED_QUESTIONS);

            // Sets previous states to Question objects.
            setAnswers();

            checkAndShowScoreScreen();
        }
        else
            Log.d(TAG, "savedInstanceState is null.");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState log: " + mCurrentIndex);
        outState.putInt(BUNDLE_KEY_CURRENT_INDEX, mCurrentIndex);
        outState.putInt(BUNDLE_KEY_SCORE, mScore);
        outState.putInt(BUNDLE_KEY_COUNT_OF_ANSWERS, mCountOfAnswers);

        getIsAnsweredFlags();
        outState.putSerializable(BUNDLE_KEY_ANSWERED_QUESTIONS, mIsAnsweredQuestions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_CHEAT) {
            boolean isCheated = data.getBooleanExtra(CheatActivity.EXTRA_IS_CHEATER, false);
            mQuestionBank.getQuestion(mCurrentIndex).setCheated(isCheated);
        }
    }

    // Finds all needed views
    private void findViews(View view) {

        view1 = view.findViewById(R.id.main);
        view2 = view.findViewById(R.id.score);
        mTextViewQuestion = view.findViewById(R.id.txtview_question_text);
        mButtonTrue = view.findViewById(R.id.btn_true);
        mButtonFalse = view.findViewById(R.id.btn_false);
        mButtonNext = view.findViewById(R.id.btn_next);
        mButtonPrev = view.findViewById(R.id.btn_prev);
        mButtonFirst =  view.findViewById(R.id.btn_first);
        mButtonLast =  view.findViewById(R.id.btn_last);
        mTextTrue = view.findViewById(R.id.txt_true);
        mTextFalse = view.findViewById(R.id.txt_false);
        mCheatButton = view.findViewById(R.id.cheat_button);
        mButtonRefresh = view.findViewById(R.id.btn_refresh);
        mTextDigit = view.findViewById(R.id.textDigit);
    }

    // Sets listener for all buttons
    private void setListeners() {
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workOnAnswers(true);

                // Set intent
                /*Intent intent = new Intent();
                intent.putExtra(EXTRA_QUESTION_IS_ANSWERED, mQuestionBank.getQuestion(mCurrentIndex).isAnswered());
                getActivity().setResult(getActivity().RESULT_OK, intent);*/
            }
        });

        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workOnAnswers(false);
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.getSize();
                updateQuestion();
            }
        });

        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.getSize()) % mQuestionBank.getSize();
                updateQuestion();
            }
        });

        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = 0;
                updateQuestion();
            }
        });

        mButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mQuestionBank.getSize() - 1;
                updateQuestion();
            }
        });

        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = 0;
                mCountOfAnswers = 0;
                mScore = 0;
                // Changes visibility of layouts
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                unsetAnswers();
                updateQuestion();
                emptyIsCheatedArray();
            }
        });

        final CheatActivity cheatActivity = new CheatActivity();
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheatActivity.class);
                intent.putExtra(EXTRA_QUESTION_ANSWER, mQuestionBank.getQuestion(mCurrentIndex).isAnswerTrue());
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    private void workOnAnswers(boolean boolAns) {
        checkAnswer(boolAns);
        mQuestionBank.getQuestion(mCurrentIndex).setAnswered(true);
        activeButtons(false);
        mCountOfAnswers++;
        // If all questions are answered, changes layout to score layout
        checkAndShowScoreScreen();
    }

    private void checkAndShowScoreScreen() {
         if (mCountOfAnswers == mQuestionBank.getSize()) {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);

            mTextDigit.setText(" " + mScore);
        }
    }

    private void updateQuestion() {
        int questionTextResId = mQuestionBank.getQuestion(mCurrentIndex).getQuestionTextResId();
        mTextViewQuestion.setText(questionTextResId);

        // If one question is answered, disables its buttons
        // else enables them
        if (mQuestionBank.getQuestion(mCurrentIndex).isAnswered())
            activeButtons(false);
        else
            activeButtons(true);
    }

    // Sets buttons clickable and not clickable
    private void activeButtons(boolean activate) {
        if (!activate) {
            mTextTrue.setTextColor(Color.GRAY);
            mTextFalse.setTextColor(Color.GRAY);
            mButtonTrue.setClickable(false);
            mButtonFalse.setClickable(false);
        }
        else {
            mTextTrue.setTextColor(Color.BLACK);
            mTextFalse.setTextColor(Color.BLACK);
            mButtonTrue.setClickable(true);
            mButtonFalse.setClickable(true);
        }
    }

    // Sets all isAnswered fields of Question array, same az previous layout.
    private void setAnswers() {
        for (int i = 0; i < mQuestionBank.getSize(); i++)
            mQuestionBank.getQuestion(i).setAnswered(mIsAnsweredQuestions[i]);
    }

    // Unsets all answers for refresh
    private void unsetAnswers() {
        for (int i = 0; i < mQuestionBank.getSize(); i++) {
            mQuestionBank.getQuestion(i).setAnswered(false);
        }


    }

    // Puts false in mIsCheated Array elements.
    private void emptyIsCheatedArray() {
        for (int i = 0; i < mQuestionBank.getSize(); i++)
            mQuestionBank.getQuestion(i).setCheated(false);
    }

    // Gets values of mAnswered field of each Question element.
    private void getIsAnsweredFlags() {

        for (int i = 0; i < mQuestionBank.getSize(); i++)
            if (mQuestionBank.getQuestion(i).isAnswered())
                mIsAnsweredQuestions[i] = true;
    }

    // Shows appropriate toast for user's answer and if he entered correct answer, pluses to its score.
    private void checkAnswer(boolean userPressed) {

        if (mQuestionBank.getQuestion(mCurrentIndex).isCheated())
            Toast.makeText(getActivity(), R.string.judgment_toast, Toast.LENGTH_SHORT).show();
        else {
            if (mQuestionBank.getQuestion(mCurrentIndex).isAnswerTrue() == userPressed) {
                Toast.makeText(getActivity(), R.string.toast_correct, Toast.LENGTH_SHORT)
                        .show();
                mScore += 10;
            } else {
                Toast.makeText(getActivity(), R.string.toast_incorrect, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        // Shows score in top of page
        Toast toast = Toast.makeText(getActivity(), getString(R.string.toast_score)
                + " " + String.valueOf(mScore), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 130);
        toast.show();
    }

}