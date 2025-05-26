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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.R;

import java.util.Objects;

public class FragmentSetupQuiz extends Fragment {


    String numQ;
    String matiere;

    public FragmentSetupQuiz(){
        super(R.layout.fragment_setup_quiz);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn = view.findViewById(R.id.btnStartQuiz);
        EditText edt = view.findViewById(R.id.etNumQuestions);
        TextView tv = view.findViewById(R.id.tvTitle);


        Bundle bundle = getArguments();  // Get the passed bundle
        if (bundle != null) {
            this.matiere = bundle.getString("Matiere");
        } else {
            Log.d("Error","Pas de bundle");
        }

        tv.setText("Nous allons commencer le quiz de " + matiere);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numQ = edt.getText().toString().trim();
                if (numQ.isEmpty()) {

                    edt.setError("Entre un nombre de question :(");
                } else {
                    NavController navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();

                    int numQuestions = Integer.parseInt(numQ);
                    bundle.putInt("numQ",numQuestions);

                    if(Objects.equals(matiere, "Maths")) {

                        navController.navigate(R.id.quiz_debut_to_maths,bundle);

                    } else if(Objects.equals(matiere, "Grammaire")) {

                        navController.navigate(R.id.quiz_debut_to_grammaire,bundle);

                    } else if (Objects.equals(matiere, "Culture")) {
                        navController.navigate(R.id.quiz_debut_to_culture,bundle);

                    } else {
                        Log.d("Erreur","Pas de matiere");
                    }


                }
            }
        });

    }
}
