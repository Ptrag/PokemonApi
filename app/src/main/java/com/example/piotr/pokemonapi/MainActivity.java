package com.example.piotr.pokemonapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.piotr.pokemonapi.pokeAPI.API_Service;
import com.example.piotr.pokemonapi.pokeAPI.PokemonData;
import com.example.piotr.pokemonapi.pokeAPI.PokemonResults;
import com.example.piotr.pokemonapi.pokeAPI.RetClient;
import com.example.piotr.pokemonapi.pokeAPI.Utils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String TAG = SaveIntoDB.class.getSimpleName();
    private PokemonAdapter pokemonAdapter;
    private RecyclerView recyclerView;
    private int offset;
    private boolean canBeLoaded;
    private PokemonData pokemonData;
    private PokemonResults pokemonResults;
    private PokemonSQLite pokemonSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerPokemon);
        pokemonAdapter = new PokemonAdapter(this);
        recyclerView.setAdapter(pokemonAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //loads the rest of the data
                if(dy > 0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItemsCount = layoutManager.findFirstVisibleItemPosition();

                    if(canBeLoaded){
                        if ((visibleItemCount + pastVisibleItemsCount) >= totalItemCount){
                            canBeLoaded = false;
                            offset += 21;
                            getApiData(offset);
                        }
                    }
                }
            }
        });

        canBeLoaded = true;
        offset = 0;

        if(Utils.isNetworkAvailable(getApplicationContext())){
            getApiData(offset);
        }else{
            getApiDataFromDB();
        }


    }

    private void getApiDataFromDB() {

        pokemonSQLite = new PokemonSQLite(this);
        List<PokemonData> pokemonList = pokemonSQLite.getPokemon();
    }

    private void getApiData(int offset) {
        //calling the actual retrofit
        Call<PokemonResults> pokemonResultsCall = new RetClient().getAPIService().getApiData(21,offset);

        pokemonResultsCall.enqueue(new Callback<PokemonResults>() {
            @Override
            public void onResponse(Call<PokemonResults> call, Response<PokemonResults> response) {
                canBeLoaded = true;
                if(response.isSuccessful()){
                    PokemonResults pokemonResults = response.body();
                    ArrayList<PokemonData> pokemonList = pokemonResults.getResults();

                    SaveIntoDB task = new SaveIntoDB();
                    task.execute(pokemonData);

                    pokemonAdapter.addingPokemonToList(pokemonList);


                }else{
                    Log.e("POKEDEX", "error msg: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonResults> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Saving into db
    public class SaveIntoDB extends AsyncTask<PokemonData, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(PokemonData... params) {

            pokemonData = params[0];

            try{
                InputStream stream = new URL("http://pokeapi.co/media/sprites/pokemon/" + pokemonData.getNumber() + ".png")
                        .openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                pokemonData.setPhoto(bitmap);
                pokemonSQLite.addPokemon(pokemonData);
                //publishProgress(pokemonData);

            }catch (Exception e){
                Log.d(TAG, e.getMessage());

            }

            return null;
        }
        /*
        @Override
        protected void onProgressUpdate(PokemonData... values) {
            super.onProgressUpdate(values);

            Log.d(TAG, "Values got " + values[0].getName());
        }
        */


    }












    //handling search bar and dispaly results
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchResults(String query) {
        Call<PokemonResults> pokemonResultsCall = new RetClient().getAPIService().getApiData(21,offset);

    }
}
