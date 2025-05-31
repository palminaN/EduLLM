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
import com.example.edullm.Models.MajMdp;
import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.Models.User;
import com.example.edullm.R;
import com.example.edullm.ViewModels.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentChangerMdp extends Fragment {

    int id;
    User child;
    UserViewModel userViewModel;

    public FragmentChangerMdp() {
        super(R.layout.fragment_change_password);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        Button cancel_button = view.findViewById(R.id.cancel_button);
        EditText new_password_input = view.findViewById(R.id.new_password_input);
        EditText confirm_new_password_input = view.findViewById(R.id.confirm_new_password_input);
        Button change_password_button = view.findViewById(R.id.change_password_button);


        Bundle bundle = getArguments();

        if(bundle == null || userViewModel.getUser() == null) {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_change_pwd_to_home_parent);
        } else {
            id = bundle.getInt("id");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserViewModel.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EduAPI apiService = retrofit.create(EduAPI.class);

        cancel_button.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_change_pwd_to_home_parent);

        });


        change_password_button.setOnClickListener(v -> {

            String pw1 = new_password_input.getText().toString().trim();
            String pw2 = confirm_new_password_input.getText().toString().trim();


            if(pw1.isEmpty() || pw2.isEmpty() || !pw1.equals(pw2)) {
                // TODO ADD ERROR MESSAGE
                return;
            }

           if(id < 0) {
               // id < 0 => parent changing his password

               Call<MajMdp> call =apiService.updateParentPassword(userViewModel.getUser().getValue().getId(), pw1);
               call.enqueue(new Callback<MajMdp>() {
                   @Override
                   public void onResponse(Call<MajMdp> call, Response<MajMdp> response) {
                       if(response.isSuccessful()) {
                           NavController navController = Navigation.findNavController(view);
                           navController.navigate(R.id.action_change_pwd_to_home_parent);
                       }
                   }

                   @Override
                   public void onFailure(Call<MajMdp> call, Throwable t) {
                        Log.d("ERROR","COULD NOT UPDATE PARENT PROFILE");
                   }
               });





           } else {
               // else one of the children
               // 1. get child
               Call<User> call= apiService.getChildProfile(id);
               call.enqueue(new Callback<User>() {
                   @Override
                   public void onResponse(Call<User> call, Response<User> response) {
                       if(response.isSuccessful() && response.body() != null) {
                           child = response.body();
                       }
                   }

                   @Override
                   public void onFailure(Call<User> call, Throwable t) {
                        Log.d("ERROR","COULD NOT GET CHILD PROFILE");
                   }
               });

               if(child == null) {
                   // TODO ?? ADD ERROR MESSAGE ON SCREEN
                   return;
               }

               RegisterLoginRequest arg = new RegisterLoginRequest(child.getEmail(),pw1);

               Call<User> call1 = apiService.updateChildPassword(id,userViewModel.getUser().getValue().getId(),arg);
               call1.enqueue(new Callback<User>() {
                   @Override
                   public void onResponse(Call<User> call, Response<User> response) {
                       if(response.isSuccessful()) {
                           NavController navController = Navigation.findNavController(view);
                           navController.navigate(R.id.action_change_pwd_to_home_parent);
                       }
                   }

                   @Override
                   public void onFailure(Call<User> call, Throwable t) {
                        Log.d("ERROR","COULD NOT UPDATE CHILD PASSWORD");
                   }
               });


           }



        });

    }
}
