package com.example.piotr.pokemonapi.pokeAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API_Service {
    @GET("pokemon")
    Call<PokemonResults>getApiData(@Query("limit") int limit,@Query("offset") int offset);

}
