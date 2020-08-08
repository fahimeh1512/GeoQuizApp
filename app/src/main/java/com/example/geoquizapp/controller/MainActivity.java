package com.example.geoquizapp.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquizapp.R;
import com.example.geoquizapp.model.Question;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BUNDLE_KEY_CURRENT_INDEX = "currentIndex";
    private static final String BUNDLE_KEY_SCORE = "score";
    private static final String BUNDLE_KEY_ANSWERED_QUESTIONS = "answeredQuestions";
    private static final String BUNDLE_KEY_COUNT_OF_ANSWERS = "countOfAnswers";/*
    private static final String BUNDLE_KEY_MAIN_VISIBILITY = "main_visibility";
    private static final String BUNDLE_KEY_SCORE_VISIBILITY = "score_visibility";*/


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
    private TextView mTextTrue;
    private TextView mTextFalse;
    // For score layout
    private TextView mTextDigit;
    private int mScore = 0;
    private int mCountOfAnswers = 0;
    private int mCurrentIndex = 0;/*
    private int mMainVisibility;
    private int mScoreVisibility;*/

    private Question[] mQuestionBank = {
            new Question(R.string.question_australia, false),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, false),
            new Question(R.string.question_asia, false)
    };

    private boolean[] mIsAnsweredQuestions = new boolean[6];

    /**
     * This method is used to crete ui for activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate log: " + mCurrentIndex);

        //this method will create the layout
        //inflate: creating object of xml layout
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState: " + savedInstanceState);/*
            mMainVisibility = savedInstanceState.getInt(BUNDLE_KEY_MAIN_VISIBILITY);
            mScoreVisibility = savedInstanceState.getInt(BUNDLE_KEY_SCORE_VISIBILITY);*/
            mCurrentIndex = savedInstanceState.getInt(BUNDLE_KEY_CURRENT_INDEX);
            mScore = savedInstanceState.getInt(BUNDLE_KEY_SCORE);
            mCountOfAnswers = savedInstanceState.getInt(BUNDLE_KEY_COUNT_OF_ANSWERS);
            mIsAnsweredQuestions = (boolean[])savedInstanceState.getSerializable(BUNDLE_KEY_ANSWERED_QUESTIONS);/*
            int main_id = savedInstanceState.getInt("main_id");
            int score_id = savedInstanceState.getInt("score_id");*/

            // Sets previous states to Question objects.
            setAnswers();

            // Sets previous states to views.
            /*view1 = findViewById(main_id);
            view2 = findViewById(score_id);
            view1.setVisibility(mMainVisibility);
            view2.setVisibility(mScoreVisibility);*/
            checkAndShowScoreScreen();
        }
        else
            Log.d(TAG, "savedInstanceState is null.");


        //if we want to change logic we must first find the view objects (it must have "id")
        findViews();
        setListeners();

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart log: " + mCurrentIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume log: " + mCurrentIndex);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause log: " + mCurrentIndex);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop log: " + mCurrentIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy log: " + mCurrentIndex);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState log: " + mCurrentIndex);/*
        mMainVisibility = view1.getVisibility();
        mScoreVisibility = view2.getVisibility();
        outState.putInt(BUNDLE_KEY_MAIN_VISIBILITY, mMainVisibility);
        outState.putInt(BUNDLE_KEY_SCORE_VISIBILITY, mScoreVisibility);*/
        outState.putInt(BUNDLE_KEY_CURRENT_INDEX, mCurrentIndex);
        outState.putInt(BUNDLE_KEY_SCORE, mScore);
        outState.putInt(BUNDLE_KEY_COUNT_OF_ANSWERS, mCountOfAnswers);/*
        outState.putCharSequence(BUNDLE_KEY_TEXT_DIGIT, mTextDigit.getText());*/
        /*outState.putInt("main_id", R.id.main);
        outState.putInt("score_id", R.id.score);*/
        getIsAnsweredFlags();
        outState.putSerializable(BUNDLE_KEY_ANSWERED_QUESTIONS, mIsAnsweredQuestions);
    }

    // Finds all needed views
    private void findViews() {

        view1 = findViewById(R.id.main);
        view2 = findViewById(R.id.score);
        mTextViewQuestion = findViewById(R.id.txtview_question_text);
        mButtonTrue = findViewById(R.id.btn_true);
        mButtonFalse = findViewById(R.id.btn_false);
        mButtonNext = findViewById(R.id.btn_next);
        mButtonPrev = findViewById(R.id.btn_prev);
        mButtonFirst =  findViewById(R.id.btn_first);
        mButtonLast =  findViewById(R.id.btn_last);
        mTextTrue = findViewById(R.id.txt_true);
        mTextFalse = findViewById(R.id.txt_false);
        mButtonRefresh = findViewById(R.id.btn_refresh);
        mTextDigit = findViewById(R.id.textDigit);
    }

    // Sets listener for all buttons
    private void setListeners() {
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workOnAnswers(true);
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
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
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
                mCurrentIndex = mQuestionBank.length - 1;
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
            }
        });
    }

    private void workOnAnswers(boolean boolAns) {
        checkAnswer(boolAns);
        mQuestionBank[mCurrentIndex].setAnswered(true);
        activeButtons(false);
        mCountOfAnswers++;
        // If all questions are answered, changes layout to score layout
        checkAndShowScoreScreen();
    }

    private void checkAndShowScoreScreen() {
        if (mCountOfAnswers == mQuestionBank.length) {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);

            mTextDigit.setText(" " + mScore);
        }
    }

    private void updateQuestion() {
        int questionTextResId = mQuestionBank[mCurrentIndex].getQuestionTextResId();
        mTextViewQuestion.setText(questionTextResId);

        // If one question is answered, disables its buttons
        // else enables them
        if (mQuestionBank[mCurrentIndex].isAnswered())
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
        for (int i = 0; i < mQuestionBank.length; i++)
            mQuestionBank[i].setAnswered(mIsAnsweredQuestions[i]);
    }

    // Unsets all answers for refresh
    private void unsetAnswers() {
        for (Question element: mQuestionBank)
            element.setAnswered(false);

    }

    // Gets values of mAnswered field of each Question element.
    private void getIsAnsweredFlags() {

        for (int i = 0; i < mQuestionBank.length; i++)
            if (mQuestionBank[i].isAnswered())
                mIsAnsweredQuestions[i] = true;
    }

    // Shows appropriate toast for user's answer and if he enterd correct answer, plusses to its score.
    private void checkAnswer(boolean userPressed) {

        if (mQuestionBank[mCurrentIndex].isAnswerTrue() == userPressed) {
            Toast.makeText(MainActivity.this, R.string.toast_correct, Toast.LENGTH_SHORT)
                    .show();
            mScore += 10;
        } else {
            Toast.makeText(MainActivity.this, R.string.toast_incorrect, Toast.LENGTH_SHORT)
                    .show();
        }

        // Shows score in top of page
        Toast toast = Toast.makeText(MainActivity.this, getString(R.string.toast_score)
                + " " + String.valueOf(mScore), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 130);
        toast.show();
    }
}