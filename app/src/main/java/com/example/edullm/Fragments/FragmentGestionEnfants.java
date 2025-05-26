package com.example.edullm.Fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
        super(R.layout.fragment_parent_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Example data - replace with your API call data
        userList = new ArrayList<>();
        userList.add(userViewModel.getUser().getValue());

        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);


    }
}
