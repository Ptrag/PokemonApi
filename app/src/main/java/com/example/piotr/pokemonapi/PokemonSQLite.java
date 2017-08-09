package com.example.piotr.pokemonapi;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.piotr.pokemonapi.pokeAPI.PokemonData;
import com.example.piotr.pokemonapi.pokeAPI.Utils;

import java.util.ArrayList;
import java.util.List;

public class PokemonSQLite extends SQLiteOpenHelper{

    private static final String DB_NAME = "pokemonApiOffline";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "POKEMONAPI";
    private static final String TAG = PokemonSQLite.class.getSimpleName();
    private static final String DROP_QUERY = "DROP TABLE IF EXIST " + TABLE_NAME;
    private static final String GET_QUERY = "SELECT * FROM " + TABLE_NAME;

    public PokemonSQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         try{
             db.execSQL("CREATE TABLE " + TABLE_NAME + "" +
                     "(_id INTEGER PRIMARY AUTOINCREMENT," +
                     "POKEDEXID INTEGER," +
                     "NAME TEXT," +
                     "IMAGE_RESOURCE_ID blob);" );

         }catch (SQLiteException ex){
             Log.d(TAG, ex.getMessage());
         }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_QUERY);
        this.onCreate(db);
    }

    public void addPokemon(PokemonData pokemonData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("POKEDEXID", pokemonData.getNumber());
        values.put("NAME" , pokemonData.getName());
        values.put("IMAGE_RESOURCE_ID" , Utils.getPhotoByteOfArray(pokemonData.getPhoto()));

        db.insert(TABLE_NAME , null , values);
        db.close();
    }

    public List<PokemonData> getPokemon(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(GET_QUERY, null);

        List<PokemonData> pokemonDataList = new ArrayList<>();

        if(cursor.getCount() > 0 ){

            if(cursor.moveToFirst()){
                do {
                    PokemonData pokemonData = new PokemonData();
                    pokemonData.setName(cursor.getColumnName(cursor.getColumnIndex("NAME")));

                    pokemonDataList.add(pokemonData);

                }while (cursor.moveToNext());
            }
        }
        return pokemonDataList;

    }
}
