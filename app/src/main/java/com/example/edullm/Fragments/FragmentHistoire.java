package com.example.edullm.Fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.edullm.ViewModels.HistoireViewModel;

public class FragmentHistoire extends Fragment {

    private String theme;
    private String hero;

    private String storyMemory = "";

    int i = 0;

    private HistoireViewModel histoireViewModel = new HistoireViewModel();

    public FragmentHistoire() {
        super(R.layout.fragment_histoire);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView histoireContainer = view.findViewById(R.id.histoireContainer);
        Button btnArret = view.findViewById(R.id.btnArreter);
        Button btnContinuer = view.findViewById(R.id.btnContinueStory);
        btnArret.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_histoire_to_home_enfant);

        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            theme = bundle.getString("theme");
            hero = bundle.getString("hero");
            Log.d("Fragment", "Received value from bundle: " + theme);

            // Use myValue as needed
        } else {
            theme = "Enfance";
            hero = "Le Petit Prince";
            Log.d("Fragment", "No arguments passed to this Fragment");
        }


        histoireViewModel.getCurrentStorySession().observe(getViewLifecycleOwner(),storySession -> {

            String[] storyNew = storySession.getStory().split(" ");
            int length = storyNew.length;

            Handler handler = new Handler();

            Runnable runnable = new Runnable() {
                public void run() {
                    storyMemory += " " + storyNew[i];
                    i++;
                    histoireContainer.setText(storyMemory);



                    if(i < length) {
                        handler.postDelayed(this,150);
                    }


                }
            };

            handler.postDelayed(runnable,500);




            i = 0;

        });

        histoireViewModel.startStory(theme,hero);

        btnContinuer.setOnClickListener(v -> {

            histoireViewModel.continueStory(theme,hero,storyMemory);

        });



    }
}
