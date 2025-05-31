package com.example.edullm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.EduAPI;
import com.example.edullm.R;
import com.example.edullm.ViewModels.QuizViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Map;
import java.util.Objects;

public class FragmentCulture extends Fragment  implements View.OnClickListener {

    private boolean answered = false;
    private String choice = "A";
    private String reponse = "NULL";
    private  int currentQuestionNumber= 1;

    int numQuestion;


    private QuizViewModel quizViewModel;

    public FragmentCulture() {
        super(R.layout.fragment_culture);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonA = view.findViewById(R.id.A);
        Button buttonB = view.findViewById(R.id.B);
        Button buttonC = view.findViewById(R.id.C);
        Button buttonValider = view.findViewById(R.id.btnNext);
        TextView textViewQuestion = view.findViewById(R.id.tvQuestion);
        TextView indicator = view.findViewById(R.id.tvQuestion2);
        ShimmerFrameLayout shimmerLayout = view.findViewById(R.id.shimmerLayout);
        shimmerLayout.startShimmer();
        ConstraintLayout contentLayout = view.findViewById(R.id.contentLayout);






        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.numQuestion = bundle.getInt("numQ");
            Log.d("Fragment", "Received value from bundle: " + numQuestion);

            // Use myValue as needed
        } else {
            this.numQuestion = 5;
            Log.d("Fragment", "No arguments passed to this Fragment");
        }



        quizViewModel.getCurrentQuizExercise().observe(getViewLifecycleOwner(), question -> {
            if (question != null) {
                textViewQuestion.setText(question.getQuestion());

                // Affiche les choix (Map)
                Map<String, String> choices = question.getChoices();
                Log.d("TEST", choices.toString());
                buttonA.setText("A: " + choices.get("A"));
                buttonB.setText("B: " + choices.get("B"));
                buttonC.setText("C: " + choices.get("C"));
                indicator.setText(currentQuestionNumber + "/" + numQuestion);
                reponse = question.getCorrectAnswer();
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);  // Optionally hide shimmer
                contentLayout.setVisibility(View.VISIBLE);  // Show real content



            } else {
                Log.d("API","QUESTION NULL");
            }
        });

        quizViewModel.startQuiz(numQuestion);

        buttonA.setOnClickListener(v -> {
            quizViewModel.submitAnswer("A");
            answered = true;
            textViewQuestion.setText("La bonne reponse est la " + reponse);



        });

        buttonB.setOnClickListener(v -> {
            quizViewModel.submitAnswer("B");
            answered = true;
            textViewQuestion.setText("La bonne reponse est la " + reponse);



        });

        buttonC.setOnClickListener(v -> {
            quizViewModel.submitAnswer("C");
            answered = true;
            textViewQuestion.setText("La bonne reponse est la " + reponse);



        });

        buttonValider.setOnClickListener(v -> {

            if(currentQuestionNumber >= numQuestion && answered) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("numQ",numQuestion);
                bundle1.putInt("score",quizViewModel.getScore());

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.culture_to_resultats,bundle1);
            }

            if(answered) {
                quizViewModel.fetchNextQuizExercise();
                answered = false;
                currentQuestionNumber++;
            }

        });
    }

    @Override
    public void onClick(View v) {

    }
}

