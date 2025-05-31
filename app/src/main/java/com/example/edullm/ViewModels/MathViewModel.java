package com.example.edullm.ViewModels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edullm.EduAPI;
import com.example.edullm.Models.MathExercise;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MathViewModel extends ViewModel {
     private EduAPI eduAPI;  // Retrofit API interface
    private final MutableLiveData<MathExercise> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private Long lastQuizExerciseId = null;  // null indicates first QuizExercise
    private int score = 0;
    private int totalQuizExercises = 0;

    public MathViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserViewModel.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eduAPI = retrofit.create(EduAPI.class);
    }


    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<MathExercise> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void startQuiz(int numExercises) {
        lastQuizExerciseId = null;
        score = 0;
        totalQuizExercises = numExercises;
        fetchNextQuestion();
    }

    public int getScore(){
        return score;
    }

    public void incrementScore(){
        score = score + 1;
    }

    public void fetchNextQuestion() {
        Call<MathExercise> call =  eduAPI.getMathExercises();
        call.enqueue(new Callback<MathExercise>() {
            @Override
            public void onResponse(Call<MathExercise> call, Response<MathExercise> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentQuestion.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to load question: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MathExercise> call, Throwable t) {
                errorMessage.postValue("Network error: " + t.getMessage());
            }

        });
    }
}