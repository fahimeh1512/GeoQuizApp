package com.example.geoquizapp.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoquizapp.R;
import com.example.geoquizapp.model.Question;

import java.util.List;

import static com.example.geoquizapp.QuizListFragment.EXTRA_QUESTION_ID;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {
    private List<Question> mQuestions;
    private Context mContext;

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_question_list, parent, false);
        QuestionHolder questionHolder = new QuestionHolder(view);

        return questionHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
        holder.bind(mQuestions.get(position));
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public QuestionAdapter(List<Question> questions, Context context) {
        mQuestions = questions;
        mContext = context;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
    }

    class QuestionHolder extends RecyclerView.ViewHolder {

        private TextView mQuestionText;
        private CheckBox mCheckBox;
        private Question mQuestion;

        // Create view holder as inner class
        public QuestionHolder(@NonNull View itemView) {
            super(itemView);

            findViews(itemView);
        }

        private void findViews(View view) {
            mQuestionText = view.findViewById(R.id.question_text);
            mCheckBox = view.findViewById(R.id.question_is_answered);
        }

        private void bind(Question question) {
            mQuestion = question;
            mQuestionText.setText(mQuestion.getQuestionTextResId());
            mCheckBox.setChecked(mQuestion.isAnswered());
            mCheckBox.setClickable(false);
        }

        /*@Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra(EXTRA_QUESTION_ID, mQuest);
            startActivity(intent);

        }*/
    }
}
