package com.example.firebasedatabaseproject.newmap.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApi {
    @GET
    Call<JsonObject> getDataFromGoogleApi (@Url String url);
}
