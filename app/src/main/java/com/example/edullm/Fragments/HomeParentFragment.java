package com.example.edullm.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.R;
import com.example.edullm.ViewModels.UserViewModel;

public class HomeParentFragment extends Fragment {

    UserViewModel userViewModel;

    public HomeParentFragment() {
        super(R.layout.fragment_profil_parent);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnManageParentAccount = view.findViewById(R.id.btnManageParentAccount);

        btnManageParentAccount.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putInt("id",-1);

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_home_parent_to_change_pwd,bundle);


        });

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        Button btn_gestion_enfants = view.findViewById(R.id.btnManageChildAccounts);
        Button btnLogOutParent = view.findViewById(R.id.btnLogOutParent);

        userViewModel.isLoggedIn().observe(getViewLifecycleOwner(),  isLoggedIn -> {
            if(!isLoggedIn) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.home_parent_to_connexion);

            }
        });

        btnLogOutParent.setOnClickListener(v -> {
            userViewModel.logout();
        });
        btn_gestion_enfants.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.home_parent_to_gestion_enfants);

        });

    }
}
