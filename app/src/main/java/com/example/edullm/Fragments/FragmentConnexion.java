package com.example.edullm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.MainActivity;
import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.R;
import com.example.edullm.ViewModels.UserViewModel;

public class FragmentConnexion extends Fragment {

    public boolean isParentRadio = true;
    private UserViewModel userViewModel;

    public FragmentConnexion() {
        super(R.layout.fragment_connexion);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);


        RadioGroup userTypeGroup = view.findViewById(R.id.user_type_group);
        EditText username = view.findViewById(R.id.username);
        EditText password = view.findViewById(R.id.password);
        TextView usernameError = view.findViewById(R.id.username_error);
        TextView passwordError = view.findViewById(R.id.password_error);
        // TextView forgotPassword = view.findViewById(R.id.forgot_password);
        TextView registerPrompt = view.findViewById(R.id.register_prompt);


        MainActivity activity = (MainActivity) getActivity();
        if (activity == null){
            System.out.println("Activity is null");
        }

        userViewModel.getUser().observe(getViewLifecycleOwner(),user -> {
            passwordError.setVisibility(View.GONE);
            if(userViewModel.getUser().getValue() == null) {
                Log.d("ERROR","NO USER");
                passwordError.setText("Erreur de connexion, veuillez réessayer");
                passwordError.setVisibility(View.VISIBLE);
                return;
            }

            NavController navController = Navigation.findNavController(view);



            if(userViewModel.getUser().getValue().is_parent) {
                // userViewModel.
                Log.d("Parent", "Is a Parent");
                //
                navController.navigate(R.id.connexion_to_home_parent);
            } else {
                //
                navController.navigate(R.id.connexion_to_home_enfant);
                Log.d("Parent", "Is NOT a Parent");

            }
        });



        registerPrompt.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_connexion_to_inscription_parent);

        });

        userTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio0) { // Parent selected
                this.isParentRadio = true;
                username.setHint("E-mail parent");
                password.setHint("Mot de passe");
                username.setText("");
                password.setText("");
                usernameError.setText("Nom d'utilisateur incorrect");
                passwordError.setText("Mot de passe incorrect");

                //forgotPassword.setVisibility(View.VISIBLE);
                registerPrompt.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio1) { // Enfant selected
                this.isParentRadio = false;
                username.setHint("E-mail enfant");
                password.setHint("code secret");

                // forgotPassword.setVisibility(View.GONE);
                registerPrompt.setVisibility(View.GONE);
            }
        });

        Button connect = view.findViewById(R.id.btn_connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwordError.setVisibility(View.GONE);
                usernameError.setVisibility(View.GONE);


                String mail = username.getText().toString().trim();
                String pw = password.getText().toString().trim();
                boolean isValid = true;
                if(pw.isEmpty()) {
                    isValid = false;
                    passwordError.setText("Veuillez entrer votre mot de passe");
                    passwordError.setVisibility(View.VISIBLE);
                }

                if(mail.isEmpty()) {
                    isValid= false;
                    usernameError.setText("Veuillez entrer une adresse e-mail");
                    usernameError.setVisibility(View.VISIBLE);
                }

                if(!isValid) return;


                userViewModel.login(new RegisterLoginRequest(mail,pw));



            }
        });




    }







    }

