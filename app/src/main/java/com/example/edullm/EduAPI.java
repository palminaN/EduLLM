package com.example.edullm;

import com.example.edullm.Models.ChildProfile;
import com.example.edullm.Models.LangueExercise;
import com.example.edullm.Models.LangueExerciseResult;
import com.example.edullm.Models.MajMdp;
import com.example.edullm.Models.MathExercise;
import com.example.edullm.Models.ParentProfile;
import com.example.edullm.Models.QuizExercise;
import com.example.edullm.Models.StateModel;
import com.example.edullm.Models.RegisterLoginRequest;
import com.example.edullm.Models.StorySession;
import com.example.edullm.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EduAPI {

    @HEAD("ping")
    Call<StateModel> ping();

    @POST("register/")
    Call<User> createUser(@Body RegisterLoginRequest u);



    @POST("login/")
    Call<User> connect(@Body RegisterLoginRequest u);

    @GET("parent/{parent_id}/children")
    Call<List<User>> getAllChildren(@Path("parent_id") int parent_id);

    @POST("parent/children")
    Call<User> createChild(@Query("parent_id") int parent_id, @Body RegisterLoginRequest newUser);

    @GET("child/{child_id}/profile")
    Call<User> getChildProfile(@Path("child_id") long childId);

    @GET("parent/{parent_id}/profile")
    Call<ParentProfile> getParentProfile(@Path("parent_id") long parentId);

    @GET("exercise/math")
    Call<MathExercise> getMathExercises();

    @GET("exercise/langue")
    Call<LangueExercise> getLangueExercises();

    @POST("exercise/langue")
    Call<LangueExerciseResult> submitLangueExercise(@Query("answer") String answer,
                                                    @Query("instruction") String instruction);

    @POST("story/start")
    Call<StorySession> startStory(@Query("theme") String theme,
                                  @Query("character") String character);

    @POST("story/continue")
    Call<StorySession> continueStory(@Query("theme") String theme,
                                     @Query("character") String character,
                                     @Query("previous") String previous
    );

    @GET("quiz")
    Call<QuizExercise> getQuizExercise();


    @PUT("parent/settings")
    Call<MajMdp> updateParentPassword(@Query("user_id") int user_id, @Query("new_password") String password);


    @PUT("parent/children/{child_id}")
    Call<User> updateChildPassword(@Path("child_id") int child_id,
                                                   @Query("parent_id")int parent_id,
                                                   @Body RegisterLoginRequest user);



}
