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
import com.example.edullm.ViewModels.VocabViewModel;

public class FragmentGrammaire extends Fragment {

    private VocabViewModel vocabViewModel;

    private int numQuestion;
    private boolean answered;

    private int currentQuestionNumber = 1;

    public FragmentGrammaire() {
        super(R.layout.fragment_vocab);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvQuestion = view.findViewById(R.id.tvVocabPrompt);
        EditText reponseEnfant = view.findViewById(R.id.etVocab);
        Button btnNextVocab = view.findViewById(R.id.btnNextVocab);


        Bundle bundle = getArguments();
        if (bundle != null) {
            numQuestion = bundle.getInt("numQ");
            Log.d("Fragment", "Received value from bundle: " + numQuestion);

            // Use myValue as needed
        } else {
            numQuestion = 5;
            Log.d("Fragment", "No arguments passed to this Fragment");
        }


        vocabViewModel = new ViewModelProvider(this).get(VocabViewModel.class);

        vocabViewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), langueExercise -> {
            tvQuestion.setText(langueExercise.getInstruction());
            btnNextVocab.setText("Valider");

        });

        vocabViewModel.getCurrentQuestionFeedback().observe(getViewLifecycleOwner(), langueExerciseResult -> {

            if (langueExerciseResult.getFeedback().contains("incorrecte")) {
                tvQuestion.setText("Continue d'essayer !");

            } else {
                vocabViewModel.incrementScore();
                tvQuestion.setText("Bien joue !");
            }

        });

        vocabViewModel.startQuiz(numQuestion);

        btnNextVocab.setOnClickListener(v -> {

            if (currentQuestionNumber > numQuestion) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("numQ", numQuestion);
                bundle1.putInt("score", vocabViewModel.getScore());

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.grammaire_to_resultats, bundle1);
            }

            if(vocabViewModel.getCurrentQuestion() == null) return;

            if (!answered) {

                String reponse = reponseEnfant.getText().toString().trim();

                if (reponse.isEmpty()) return;

                vocabViewModel.fetchFeedBack(reponse);

                answered = true;
                currentQuestionNumber++;
                btnNextVocab.setText("Question suivante");


            } else {
                answered = false;
                vocabViewModel.fetchNextQuestion();

            }


        });
    }
}
