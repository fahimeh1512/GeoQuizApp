package com.example.geoquizapp.repository;

import com.example.geoquizapp.R;
import com.example.geoquizapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
    private static QuestionRepository sInstance;
    private List<Question> mQuestionBank = new ArrayList<>();

    private int[] mQuestions = {
            R.string.question_australia,
            R.string.question_oceans,
            R.string.question_mideast,
            R.string.question_africa,
            R.string.question_americas,
            R.string.question_asia,
            R.string.question_australia,
            R.string.question_oceans,
            R.string.question_mideast,
            R.string.question_africa,
            R.string.question_americas,
            R.string.question_asia,
            R.string.question_australia,
            R.string.question_oceans,
            R.string.question_mideast,
            R.string.question_africa,
            R.string.question_americas,
            R.string.question_asia,
            R.string.question_australia,
            R.string.question_oceans,
            R.string.question_mideast,
            R.string.question_africa,
            R.string.question_americas,
            R.string.question_asia
    };

    private QuestionRepository() {
        generateQuestionBank();
    }

    public static QuestionRepository getInstance() {
        if (sInstance == null)
            sInstance = new QuestionRepository();
        return sInstance;
    }

    public List<Question> getQuestionBank() {
        return mQuestionBank;
    }

    public Question getQuestion(int index) {
        return mQuestionBank.get(index);
    }

    public int getSize() {
        return mQuestionBank.size();
    }

    private void generateQuestionBank() {
        for (int i = 0; i < mQuestions.length; i++) {
            Question question = new Question(mQuestions[i], i % 2 == 0);
            /*question.setQuestionTextResId(mQuestions[i]);
            question.setAnswerTrue(i % 2 == 0);*/

            mQuestionBank.add(question);
        }

    }
}
