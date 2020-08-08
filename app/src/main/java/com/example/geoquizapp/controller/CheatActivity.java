package com.example.geoquizapp.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.geoquizapp.R;

public class CheatActivity extends AppCompatActivity {
    private TextView mTextViewAnswer;
    private Button mShowCheat;
    private boolean mIsAnswerTrue;

    public static final String EXTRA_IS_CHEATER = "com.example.geoquizapp.controller.isCheater";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        Intent intent = getIntent();

        mIsAnswerTrue = intent.getBooleanExtra(MainActivity.EXTRA_QUESTION_ANSWER, false);

        findViews();

        setListeners();
    }

    private void findViews() {
        mTextViewAnswer = findViewById(R.id.txtview_answer);
        mShowCheat = findViewById(R.id.btn_show_answer);
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
        intent.putExtra(EXTRA_IS_CHEATER, isCheater);

        setResult(RESULT_OK, intent);
    }
}