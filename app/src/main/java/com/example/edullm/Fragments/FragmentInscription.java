package com.example.edullm.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.R;
import com.example.edullm.ViewModels.UserViewModel;

public class FragmentInscription extends Fragment {

    private UserViewModel userViewModel;

    public FragmentInscription(){
        super(R.layout.fragment_inscription);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        TextView connect = view.findViewById(R.id.login_link);
        Button btnRegister = view.findViewById(R.id.create_account_btn);
        TextView tv_email = view.findViewById(R.id.email);
        TextView tv_password = view.findViewById(R.id.password);
        TextView tv_password_confirm = view.findViewById(R.id.confirm_password);

        userViewModel.isLoggedIn().observe(getViewLifecycleOwner(), isRegistered -> {
            if (isRegistered) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.inscription_parent_to_connexion);
            } else {
                // Update UI for failure
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tv_email.getText().toString().trim();
                String pw1 = tv_password_confirm.getText().toString().trim();
                String pw2 = tv_password.getText().toString().trim();
                boolean isValid = true;

                if(email.isEmpty()){
                    isValid = false;

                }

                if(pw1.isEmpty()) {
                    isValid = false;
                }

                if (pw2.isEmpty()) {
                    isValid = false;
                }

                if(!pw1.equals(pw2)) {
                    isValid = false;
                }


                if(isValid) {
                    RegisterLoginRequest newRegisterLoginRequest = new RegisterLoginRequest(email,pw1);
                    userViewModel.register(newRegisterLoginRequest);

                }

            }



        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.inscription_parent_to_connexion);
            }
        });
    }



}
