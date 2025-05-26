package com.example.edullm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edullm.EduAPI;
import com.example.edullm.Models.LangueExercise;
import com.example.edullm.Models.LangueExerciseResult;
import com.example.edullm.Models.MathExercise;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VocabViewModel extends ViewModel {

    private EduAPI eduAPI;
    private final MutableLiveData<LangueExercise> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<LangueExerciseResult> currentQuestionFeedback = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private Long lastQuizExerciseId = null;
    private int score = 0;
    private int totalQuizExercises = 0;

    public VocabViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserViewModel.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eduAPI = retrofit.create(EduAPI.class);
    }


    public LiveData<LangueExercise> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<LangueExerciseResult> getCurrentQuestionFeedback() {
        return currentQuestionFeedback;
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
        Call<LangueExercise> call =  eduAPI.getLangueExercises();
        call.enqueue(new Callback<LangueExercise>() {
            @Override
            public void onResponse(Call<LangueExercise> call, Response<LangueExercise> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentQuestion.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to load question: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LangueExercise> call, Throwable t) {
                errorMessage.postValue("Network error: " + t.getMessage());
            }

        });
    }

    public void fetchFeedBack(String answer) {
        Call<LangueExerciseResult> call =  eduAPI.submitLangueExercise(answer,currentQuestion.getValue().getInstruction());
        call.enqueue(new Callback<LangueExerciseResult>() {
            @Override
            public void onResponse(Call<LangueExerciseResult> call, Response<LangueExerciseResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentQuestionFeedback.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to load question: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LangueExerciseResult> call, Throwable t) {
                errorMessage.postValue("Network error: " + t.getMessage());
            }

        });
    }

}
