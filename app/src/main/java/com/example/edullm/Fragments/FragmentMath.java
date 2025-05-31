package com.example.edullm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.R;
import com.example.edullm.ViewModels.MathViewModel;
import com.example.edullm.ViewModels.QuizViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

public class FragmentMath extends Fragment {

    private int numQuestion;
    private boolean answered = false;

    private  int currentQuestionNumber= 1;


    public FragmentMath() {
        super(R.layout.fragment_maths);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvQuestion = view.findViewById(R.id.tvProblem);
        TextView tvNumQ = view.findViewById(R.id.tvNumQuestion);
        EditText reponseEnfant = view.findViewById(R.id.etMathAnswer);
        Button btnNextMath = view.findViewById(R.id.btnNextMath);
        MathViewModel mathViewModel = new ViewModelProvider(this).get(MathViewModel.class);
        tvQuestion.setAlpha(0f);
        tvNumQ.setAlpha(0f);
        mathViewModel.getCurrentQuestion().observe(getViewLifecycleOwner(),mathExercise -> {
            tvNumQ.setText(currentQuestionNumber + "/" + numQuestion);
            tvQuestion.setText( mathExercise.getQuestion());
            tvQuestion.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setListener(null);

            tvNumQ.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setListener(null);

            btnNextMath.setText("Valider");


        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            numQuestion = bundle.getInt("numQ");
            Log.d("Fragment", "Received value from bundle: " + numQuestion);

            // Use myValue as needed
        } else {
            numQuestion = 5;
            Log.d("Fragment", "No arguments passed to this Fragment");
        }

        mathViewModel.startQuiz(numQuestion);

        btnNextMath.setOnClickListener(v -> {

            if(currentQuestionNumber > numQuestion) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("numQ",numQuestion);
                bundle1.putInt("score",mathViewModel.getScore());

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.maths_to_resultats,bundle1);
            }

            if(!answered) {
                int bonneReponse= (int) mathViewModel.getCurrentQuestion().getValue().getAnswer();
                tvQuestion.setText("La bonne reponse est " + String.valueOf(bonneReponse));
                int reponse = Integer.parseInt(reponseEnfant.getText().toString());
                answered = true;
                currentQuestionNumber++;
                btnNextMath.setText("Question suivante");
                if(reponse == mathViewModel.getCurrentQuestion().getValue().answer) {
                    mathViewModel.incrementScore();
                }


            } else {
                answered = false;
                mathViewModel.fetchNextQuestion();

            }

        });





    }
}
