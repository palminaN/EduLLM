package com.example.edullm;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Boolean connected;
    Retrofit retrofit;

    EduAPI apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setConnected(false);
        setContentView(R.layout.main_layout);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://edullm.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(EduAPI.class);
    }



    public Boolean isConnected() {
        return connected;
    }


    public void setConnected(Boolean state){

        this.connected = state;

    }


    public EduAPI getApiService() {
        return apiService;
    }
}
