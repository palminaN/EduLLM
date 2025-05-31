package com.example.edullm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.R;
import com.example.edullm.ViewModels.UserViewModel;

import org.w3c.dom.Text;

public class HomeEnfantFragment extends Fragment implements View.OnClickListener{

    UserViewModel userViewModel;

    public HomeEnfantFragment() {
        super(R.layout.fragment_home_enfant);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button maths = view.findViewById(R.id.btnMaths);
        Button grammaire = view.findViewById(R.id.btnGrammaire);
        Button deconnexion = view.findViewById(R.id.btnLogOutEnfant);
        Button culture = view.findViewById(R.id.btnCulture);
        Button histoire = view.findViewById(R.id.btnHistoires);
        TextView bienvenu = view.findViewById(R.id.tv_bienvenue);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        deconnexion.setOnClickListener(v -> {
            userViewModel.logout();
        });

        userViewModel.isLoggedIn().observe(getViewLifecycleOwner(),isLogged -> {
            if(!isLogged) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.home_enfant_to_connexion);
            }
        });


        if(userViewModel.getUser() == null) {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.home_enfant_to_connexion);
        }



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