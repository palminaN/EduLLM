package com.example.edullm;

import com.example.edullm.Models.ChildProfile;
import com.example.edullm.Models.LangueExercise;
import com.example.edullm.Models.LangueExerciseResult;
import com.example.edullm.Models.LangueExerciseSubmission;
import com.example.edullm.Models.MathExercise;
import com.example.edullm.Models.ParentProfile;
import com.example.edullm.Models.QuizExercise;
import com.example.edullm.Models.StateModel;
import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.Models.StoryContinueRequest;
import com.example.edullm.Models.StorySession;
import com.example.edullm.Models.StoryStartRequest;
import com.example.edullm.Models.UserID;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EduAPI {

    @HEAD("ping")
    Call<StateModel> ping();

    @POST("register/")
    Call<UserID> createUser(@Body RegisterLoginRequest u);

    @POST("login/")
    Call<UserID> connect(@Body RegisterLoginRequest u);
    @GET("child/{child_id}/profile")
    Call<ChildProfile> getChildProfile(@Path("child_id") long childId);

    @GET("parent/{parent_id}/profile")
    Call<ParentProfile> getParentProfile(@Path("parent_id") long parentId);

    @GET("exercice/math")
    Call<List<MathExercise>> getMathExercises();

    @GET("exercice/langue")
    Call<List<LangueExercise>> getLangueExercises();

    @POST("exercice/langue")
    Call<LangueExerciseResult> submitLangueExercise(@Body LangueExerciseSubmission submission);

    @POST("story/start")
    Call<StorySession> startStory(@Body StoryStartRequest request);

    @POST("story/continue")
    Call<StorySession> continueStory(@Body StoryContinueRequest request);

    @GET("exercice/quiz")
    Call<List<QuizExercise>> getQuizExercises();

}
