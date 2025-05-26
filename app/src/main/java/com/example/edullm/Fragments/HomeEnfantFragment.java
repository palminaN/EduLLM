package com.example.edullm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.R;

public class HomeEnfantFragment extends Fragment implements View.OnClickListener{

    public HomeEnfantFragment() {
        super(R.layout.fragment_home_enfant);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button maths = view.findViewById(R.id.btnMaths);
        Button grammaire = view.findViewById(R.id.btnGrammaire);
        Button culture = view.findViewById(R.id.btnCulture);
        Button histoire = view.findViewById(R.id.btnHistoires);

        maths.setOnClickListener(this);
        grammaire.setOnClickListener(this);
        culture.setOnClickListener(this);
        histoire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.home_enfant_to_histoire);
            }
        });
    }

    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(v);
        Bundle bundle = new Bundle();
        int id = v.getId();
        if (id == R.id.btnMaths) {
            bundle.putString("Matiere","Maths");
        } else if (id == R.id.btnCulture) {
            bundle.putString("Matiere","Culture");
        } else if (id == R.id.btnGrammaire) {
            bundle.putString("Matiere","Grammaire");
        } else {
            Log.d("Bizarre","Jsp sur quoi t'as clique x(");
            bundle.putString("Matiere","Error");
        }

        navController.navigate(R.id.home_enfant_to_setup_quiz,bundle);

    }
}