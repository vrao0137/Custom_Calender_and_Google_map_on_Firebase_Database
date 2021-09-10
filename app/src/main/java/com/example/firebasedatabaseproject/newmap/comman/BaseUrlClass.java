package com.example.firebasedatabaseproject.newmap.comman;

import com.example.firebasedatabaseproject.newmap.retrofit.IGoogleApi;
import com.example.firebasedatabaseproject.newmap.retrofit.RetrofitClient;

public class BaseUrlClass {
    public static final String baseURL = "https://maps.googleapis.com";
    public static IGoogleApi getGooglApi(){
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
