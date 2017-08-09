package com.example.piotr.pokemonapi.pokeAPI;


import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.annotations.Expose;

public class PokemonData {

    @Expose
    private String url;

    @Expose
    private String name;

    private Bitmap photo;



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    //parse number form the url
    public int getNumber(){
        String[] adressLink = url.split("/");
        return Integer.parseInt(adressLink[adressLink.length - 1]);
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return photo;
    }


}

