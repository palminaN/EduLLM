package com.example.edullm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edullm.EduAPI;
import com.example.edullm.Models.QuizExercise;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;

public class QuizViewModel extends ViewModel {

    private  EduAPI eduAPI;  // Retrofit API interface
    private final MutableLiveData<QuizExercise> currentQuizExercise = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private Long lastQuizExerciseId = null;  // null indicates first QuizExercise
    private int score = 0;
    private int totalQuizExercises = 0;

    public QuizViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserViewModel.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eduAPI = retrofit.create(EduAPI.class);
    }

    public LiveData<QuizExercise> getCurrentQuizExercise() {
        return currentQuizExercise;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuizExercises() {
        return totalQuizExercises;
    }

    public void startQuiz(int numExercises) {
        lastQuizExerciseId = null;
        score = 0;
        totalQuizExercises = numExercises;
        fetchNextQuizExercise();
    }

    public void fetchNextQuizExercise() {
        isLoading.postValue(true);
        Call<QuizExercise> call = eduAPI.getQuizExercise();
        call.enqueue(new Callback<QuizExercise>() {
            @Override
            public void onResponse(Call<QuizExercise> call, Response<QuizExercise> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    QuizExercise QuizExercise = response.body();
                    currentQuizExercise.postValue(QuizExercise);
                    totalQuizExercises++;
                } else {
                    errorMessage.postValue("Failed to load QuizExercise. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<QuizExercise> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Network error: " + t.getMessage());
            }
        });
    }

    public void submitAnswer(String selectedKey) {
        QuizExercise QuizExercise = currentQuizExercise.getValue();
        if (QuizExercise == null) return;

        if (selectedKey.equals(QuizExercise.getCorrectAnswer())) {
            score++;
        }
    }
}
