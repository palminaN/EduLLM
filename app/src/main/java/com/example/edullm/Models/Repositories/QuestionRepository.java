package com.example.edullm.Models.Repositories;

import com.example.edullm.EduAPI;
import com.example.edullm.Models.QuizExercise;

import retrofit2.Call;
import retrofit2.Callback;

public class QuestionRepository {
    private EduAPI api;

    public QuestionRepository(EduAPI api) {
        this.api = api;
    }
/*
    public void fetchNextQuestion(Long previousId, Callback<Question> callback) {
        Call<Question> call = api.getNextQuestion(previousId);
        call.enqueue(callback);
    }
*/

    // QUIZ question
    public void fetchQuizQuestion(Callback<QuizExercise> callback) {
        Call<QuizExercise> call = api.getQuizExercise();
        call.enqueue(callback);
    }
}

