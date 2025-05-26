package com.example.edullm.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.R;

public class HomeParentFragment extends Fragment {

    public HomeParentFragment() {
        super(R.layout.fragment_profil_parent);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_gestion_enfants = view.findViewById(R.id.btnManageChildAccounts);

        btn_gestion_enfants.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.home_parent_to_gestion_enfants);

        });

    }
}
