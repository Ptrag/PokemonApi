package com.example.piotr.pokemonapi.pokeAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetClient {
    private API_Service api_service;

    public RetClient(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUtils.BASE_URL_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create()).build();

        api_service = retrofit.create(API_Service.class);
    }

    public API_Service getAPIService(){
        return api_service;
    }
}
