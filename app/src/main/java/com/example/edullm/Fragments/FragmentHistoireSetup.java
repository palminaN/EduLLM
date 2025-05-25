package com.example.edullm.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.R;

public class FragmentHistoireSetup extends Fragment {

    public FragmentHistoireSetup() {
        super(R.layout.fragment_histoire_setup);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnCommencer = view.findViewById(R.id.btnStartStory);
        TextView tv1 = view.findViewById(R.id.etTheme);
        TextView tv2 = view.findViewById(R.id.etHero);




        btnCommencer.setOnClickListener(v -> {
            String theme = tv1.getText().toString().trim();
            String hero = tv2.getText().toString().trim();
            boolean isValid = true;

            if(theme.isEmpty()) {
             isValid = false;
            }


            if(hero.isEmpty()) {
                isValid = false;
            }


            if(isValid) {
                Bundle bundle = new Bundle();
                bundle.putString("theme",theme);
                bundle.putString("hero",hero);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.histoire_setup_to_histoire,bundle);

            }

        });


    }
}
