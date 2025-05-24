package com.example.edullm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.edullm.EduAPI;
import com.example.edullm.MainActivity;
import com.example.edullm.Models.StateModel;
import com.example.edullm.Models.User;
import com.example.edullm.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnexionFragment extends Fragment {


    public boolean isParent = true;

    public ConnexionFragment() {
        super(R.layout.activity_connexion);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            System.out.println(activity.isConnected());
        } else {
            System.out.println("Activity is null");
        }

        RadioGroup userTypeGroup = view.findViewById(R.id.user_type_group);
        EditText username = view.findViewById(R.id.username);
        EditText password = view.findViewById(R.id.password);
        TextView usernameError = view.findViewById(R.id.username_error);
        TextView passwordError = view.findViewById(R.id.password_error);
        TextView forgotPassword = view.findViewById(R.id.forgot_password);
        TextView registerPrompt = view.findViewById(R.id.register_prompt);

        userTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio0) { // Parent selected
                this.isParent = true;
                username.setHint("Nom d'utilisateur");
                password.setHint("Mot de passe");
                username.setText("");
                password.setText("");
                usernameError.setText("Nom d'utilisateur incorrect");
                passwordError.setText("Mot de passe incorrect");

                forgotPassword.setVisibility(View.VISIBLE);
                registerPrompt.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio1) { // Enfant selected
                this.isParent = false;
                username.setHint("Nom de l'enfant");
                password.setHint("Code secret");
                username.setText("");
                password.setText("");
                usernameError.setText("Nom de l'enfant incorrect");
                passwordError.setText("Code secret incorrect");

                forgotPassword.setVisibility(View.GONE);
                registerPrompt.setVisibility(View.GONE);
            }
        });

        Button connect = view.findViewById(R.id.btn_connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = getUser();

                NavController navController = Navigation.findNavController(view);

                if(isParent) {

                    Log.d("Parent", "Is a Parent");
                    //
                    navController.navigate(R.id.connexion_to_home_parent);
                } else {
                    //
                    navController.navigate(R.id.connexion_to_home_enfant);
                    Log.d("Parent", "Is NOT a Parent");

                }

            }
        });




    }




    public User getUser() {
        MainActivity activity = (MainActivity) getActivity();
        Call<StateModel> call = activity.getApiService().ping();
        call.enqueue(new Callback<StateModel>() {
            @Override
            public void onResponse(Call<StateModel> call, Response<StateModel> response) {

                if(response.body() == null) {
                    // ERROR WITH API
                    return;
                }

                StateModel sm =response.body();
                Log.d("Test response", sm.getStatus());
            }

            @Override
            public void onFailure(Call<StateModel> call, Throwable t) {
                Log.d("Test response","Echec appel API");
            }
        });


        return null;
    }


    }

