package com.example.edullm.Models;


import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class QuizExercise {
    private String question;

    private Map<String, String> choices;

    @SerializedName("correct_answer")
    private String correctAnswer;

    // Getters
    public String getQuestion() {
        return question;
    }

    public Map<String, String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}