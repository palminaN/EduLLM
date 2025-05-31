package com.example.edullm.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edullm.EduAPI;
import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.Models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserViewModel extends ViewModel {
    private MutableLiveData<Boolean> logged_in = new MutableLiveData<>();
    private EduAPI apiService;

    private MutableLiveData<List<User>> children = new MutableLiveData<>();

    private MutableLiveData<User> user = new MutableLiveData<>();

    public static String baseUrl = "https://edullm.onrender.com/";

    public UserViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserViewModel.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(EduAPI.class);
    }

    public MutableLiveData<List<User>> getChildren() {
        return children;
    }

    public LiveData<Boolean> isLoggedIn() {
        return logged_in;
    }


    public LiveData<User> getUser() {return user;}

    public void register(RegisterLoginRequest u) {
        Call<User> call = apiService.createUser(u);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("APICALL", String.valueOf(response));
                if (response.isSuccessful() && response.body() != null) {
                    user.postValue(response.body());
                    logged_in.postValue(true);
                } else {
                    logged_in.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                logged_in.postValue(false);
            }
        });
    }



    public RegisterLoginRequest login(RegisterLoginRequest u) {

        Call<User> call = apiService.connect(u);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("APICALL", String.valueOf(response));
                if (response.isSuccessful() && response.body() != null) {
                    user.postValue(response.body());
                    logged_in.postValue(true);
                } else {
                    user.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                logged_in.postValue(false);
            }
        });

        return null;

    }

    public void getUserChildren() {
        if(getUser() == null || !getUser().getValue().is_parent) return;


        Call<List<User>> call = apiService.getAllChildren(getUser().getValue().getId());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("APICALL", String.valueOf(response));
                if (response.isSuccessful() && response.body() != null) {
                    children.postValue(response.body());
                    logged_in.postValue(true);
                } else {
                    logged_in.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("API GET CHILDREN" , "ERROR FETCHING DATA");
            }
        });


    }

    public  void logout() {
        user.postValue(null);
        logged_in.postValue(false);
        List<User> emptyList = new ArrayList<>();
        children.postValue(emptyList);
    }


}