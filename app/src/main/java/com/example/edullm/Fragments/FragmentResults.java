package com.example.edullm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.R;

public class FragmentResults  extends Fragment {

    int numQuestion=1;
    int score=0;

    int ratio = 0;

    public FragmentResults() {
        super(R.layout.fragment_resultats);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textResultats = view.findViewById(R.id.tvVocabPrompt);
        TextView resultats = view.findViewById(R.id.tvVocabFeedback);
        Button retourHome = view.findViewById(R.id.btnNextVocab);

        retourHome.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.resultats_to_home_enfant);
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            numQuestion = bundle.getInt("numQ");
            score = bundle.getInt("score");
            ratio = score / numQuestion;
            Log.d("Fragment", "Received value from bundle: " + numQuestion);

            // Use myValue as needed
        } else {
            numQuestion = 5;
            Log.d("Fragment", "No arguments passed to this Fragment");
        }

        resultats.setText("Ton score : " + score +"/" + numQuestion );

        if(ratio > 0.75) {
            textResultats.setText("Youpi ! Tu as super bien reussi le quiz ! ");
        } else if(ratio > 0.50) {
            textResultats.setText("Super ! tu as as eu bon a pleins de question !");
        } else if(ratio > 0.25) {
            textResultats.setText("Tu commences bien continue comme ca !");
        } else {
            textResultats.setText("Continue de jouer si tu veux t'ameliorer avec moi !");
        }


    }
}
