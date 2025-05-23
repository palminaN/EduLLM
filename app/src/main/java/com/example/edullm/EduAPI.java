package com.example.edullm;

import com.example.edullm.Models.StateModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EduAPI {

    @GET("ping")
    Call<StateModel> ping();

}
