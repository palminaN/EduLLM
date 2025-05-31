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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edullm.Models.User;
import com.example.edullm.R;
import com.example.edullm.ViewModels.UserViewModel;
import com.example.edullm.recyclerViewEnfants.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentGestionEnfants extends Fragment {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> userList;

    UserViewModel userViewModel;

    public FragmentGestionEnfants(){


        super(R.layout.fragment_parent_gestion_enfants);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button ajouterEnfant = view.findViewById(R.id.add_child_button);
        ajouterEnfant.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.gestion_enfants_to_inscription_enfant);
        });

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();

        userViewModel.getChildren().observe(getViewLifecycleOwner(),users -> {

            userList = users;
            userAdapter = new UserAdapter(userList);
            recyclerView.setAdapter(userAdapter);
        });

        userViewModel.getUserChildren();




    }
}
