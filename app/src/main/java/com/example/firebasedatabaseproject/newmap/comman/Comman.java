package com.example.firebasedatabaseproject.newmap.comman;

import com.example.firebasedatabaseproject.newmap.service.IGoogleApi;
import com.example.firebasedatabaseproject.newmap.service.RetrofitClient;

public class Comman {
    public static final String baseURL = "https://maps.googleapis.com";
    public static IGoogleApi getGooglApi(){
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
