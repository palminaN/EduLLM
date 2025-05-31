package com.example.edullm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.EduAPI;
import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.Models.User;
import com.example.edullm.R;
import com.example.edullm.ViewModels.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentInscriptionEnfant  extends Fragment {

    UserViewModel userViewModel;

    public FragmentInscriptionEnfant(){
        super(R.layout.fragment_ajouter_enfant);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        EditText emailInput = view.findViewById(R.id.email_input);
        EditText passwordInput = view.findViewById(R.id.password_input);
        EditText confirmPasswordInput = view.findViewById(R.id.confirm_password_input);
        Button createAccountButton = view.findViewById(R.id.create_account_button);
        Button goBackButton = view.findViewById(R.id.go_back_button);

        goBackButton.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.inscription_enfant_to_profil_parent);

        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String pw1 = passwordInput.getText().toString().trim();
                String pw2 = confirmPasswordInput.getText().toString().trim();
                boolean isValid = true;
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserViewModel.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                EduAPI eduAPI = retrofit.create(EduAPI.class);

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
                    Call<User> call = eduAPI.createChild(userViewModel.getUser().getValue().getId(),newRegisterLoginRequest);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()) {
                                NavController navController = Navigation.findNavController(view);
                                navController.navigate(R.id.inscription_enfant_to_profil_parent);

                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("ERROR","COULD NOT CREATE CHILD");
                        }
                    });

                }
            }
        });




    }
}
