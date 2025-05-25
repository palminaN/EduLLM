package com.example.edullm.Models.Repositories;

import com.example.edullm.EduAPI;
import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.Models.UserID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {
    private EduAPI apiService;

    public static String baseUrl = "https://edullm.onrender.com/";

    public UserRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserRepository.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(EduAPI.class);
    }

    public void registerUser(RegisterLoginRequest request, Callback<UserID> callback)  {
        Call<UserID> call = apiService.createUser(request);
        call.enqueue(callback);


    }



    public void loginUser(RegisterLoginRequest request, Callback<UserID> callback) {
        Call<UserID> call = apiService.connect(request);
        call.enqueue(callback);
    }
}
