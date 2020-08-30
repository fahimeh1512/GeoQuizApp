package com.example.geoquizapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoquizapp.controller.CheatActivity;
import com.example.geoquizapp.controller.MainActivity;
//import com.example.geoquizapp.controller.QuestionAdapter;
import com.example.geoquizapp.controller.QuizFragment;
import com.example.geoquizapp.model.Question;
import com.example.geoquizapp.repository.QuestionRepository;

import java.util.List;

public class QuizListFragment extends Fragment {

    private RecyclerView  mRecyclerView;
    private QuestionRepository mQuestionRepository;
    private int mQuestionPosition;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == QuestionAdapter.REQUEST_CODE_IS_ANSWERED) {
            //boolean isCheated = data.getBooleanExtra(CheatActivity.EXTRA_IS_CHEATER, false);
            boolean isAnswered = data.getBooleanExtra(QuizFragment.EXTRA_QUESTION_IS_ANSWERED,false);
            mQuestionRepository.getQuestion(mQuestionPosition).setAnswered(isAnswered);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*for (int i = 0; i < mQuestionRepository.getSize(); i++) {
            boolean isAnswered = mQuestionRepository.getQuestion(i).isAnswered();
        }*/
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_quiz_list);
    }

    private void initViews(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Question> questions = mQuestionRepository.getQuestionBank();

        // set adapter
        QuestionAdapter questionAdapter = new QuestionAdapter(questions, getContext());
        mRecyclerView.setAdapter(questionAdapter);
        //questionAdapter.
    }

    // Adapter and holder

    private class QuestionHolder extends RecyclerView.ViewHolder {

        private TextView mQuestionText;
        private CheckBox mCheckBox;
        private Question mQuestion;


        // Create view holder as inner class
        public QuestionHolder(@NonNull View itemView) {
            super(itemView);

            findViews(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Refactore creating new intent.
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    mQuestionPosition = getAdapterPosition();
                    intent.putExtra(QuestionAdapter.EXTRA_QUESTION_INDEX, getAdapterPosition());
                    //mContext.startActivity(intent);
                    /*Activity origin = (Activity)mContext;*/
                    getActivity().startActivityForResult(intent, QuestionAdapter.REQUEST_CODE_IS_ANSWERED);

                }
            });


        }


        private void findViews(View view) {
            mQuestionText = view.findViewById(R.id.question_text);
            mCheckBox = view.findViewById(R.id.question_is_answered);
        }

        public void bind(Question question) {
            mQuestion = question;
            mQuestionText.setText(mQuestion.getQuestionTextResId());
            boolean bool = mQuestion.isAnswered();
            mCheckBox.setChecked(bool);/*
            mCheckBox.setClickable(false);*/
        }

    }

    public class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {
        private List<Question> mQuestions;
        private Context mContext;

        public static final String EXTRA_QUESTION_INDEX = "com.example.geoquizapp.questionIndex";
        public static final int REQUEST_CODE_IS_ANSWERED = 1;

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
            //mQuestion item = mQuestionRepository.getQuestion(position);
            /*Log.d("BIND", "onBindViewHolder: ");
            Toast.makeText(mContext, "bindviewholder", Toast.LENGTH_SHORT).show();*/

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
    }

}
