package com.example.edullm.ViewModels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edullm.Models.StorySession;
import com.example.edullm.EduAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoireViewModel extends ViewModel {

    private EduAPI eduAPI;

    private final MutableLiveData<StorySession> currentStorySession = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();


    public HistoireViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserViewModel.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eduAPI = retrofit.create(EduAPI.class);
    }

    public LiveData<StorySession> getCurrentStorySession() {
        return currentStorySession;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // 🌟 Start a story
    public void startStory(String theme, String character) {
        isLoading.postValue(true);
        Call<StorySession> call = eduAPI.startStory(theme,character);
        call.enqueue(new Callback<StorySession>() {
            @Override
            public void onResponse(Call<StorySession> call, Response<StorySession> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    currentStorySession.postValue(response.body());

                } else {
                    errorMessage.postValue("Erreur de démarrage de l'histoire. Code : " + response.code());
                }
                Log.d("TEST", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<StorySession> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Erreur réseau : " + t.getMessage());
            }
        });
    }

    // 🌟 Continue the story
    public void continueStory(String theme, String character, String previous) {
        isLoading.postValue(true);
        Call<StorySession> call = eduAPI.continueStory(theme, character, previous);
        call.enqueue(new Callback<StorySession>() {
            @Override
            public void onResponse(Call<StorySession> call, Response<StorySession> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    currentStorySession.postValue(response.body());
                } else {
                    errorMessage.postValue("Erreur en continuant l'histoire. Code : " + response.code());
                }
                Log.d("TEST CONTINUE", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<StorySession> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Erreur réseau : " + t.getMessage());
            }
        });
    }
}