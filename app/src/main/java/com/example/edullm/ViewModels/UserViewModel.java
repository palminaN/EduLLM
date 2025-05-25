package com.example.edullm.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edullm.EduAPI;
import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.Models.UserID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserViewModel extends ViewModel {
    private MutableLiveData<Boolean> logged_in = new MutableLiveData<>();
    private EduAPI apiService;

    private MutableLiveData<UserID> uid = new MutableLiveData<>();

    public static String baseUrl = "https://edullm.onrender.com/";

    public UserViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserViewModel.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(EduAPI.class);
    }

    public LiveData<Boolean> isLoggedIn() {
        return logged_in;
    }

    public void register(RegisterLoginRequest user) {
        Call<UserID> call = apiService.createUser(user);
        call.enqueue(new Callback<UserID>() {
            @Override
            public void onResponse(Call<UserID> call, Response<UserID> response) {
                Log.d("APICALL", String.valueOf(response));
                if (response.isSuccessful() && response.body() != null) {
                    uid.postValue(response.body());
                    logged_in.postValue(true);
                } else {
                    logged_in.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<UserID> call, Throwable t) {
                logged_in.postValue(false);
            }
        });
    }


    public void getParent() {

    }


    public RegisterLoginRequest login(RegisterLoginRequest u) {

        Call<UserID> call = apiService.connect(u);
        call.enqueue(new Callback<UserID>() {
            @Override
            public void onResponse(Call<UserID> call, Response<UserID> response) {
                Log.d("APICALL", String.valueOf(response));
                if (response.isSuccessful() && response.body() != null) {
                    uid.postValue(response.body());
                    logged_in.postValue(true);
                } else {
                    logged_in.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<UserID> call, Throwable t) {
                logged_in.postValue(false);
            }
        });

        return null;

    }
}