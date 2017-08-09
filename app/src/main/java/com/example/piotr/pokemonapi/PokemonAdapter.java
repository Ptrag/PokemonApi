package com.example.piotr.pokemonapi;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.example.piotr.pokemonapi.pokeAPI.PokemonData;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

    private ArrayList<PokemonData> pokemonData;
    private Context context;

    //constructor
    public PokemonAdapter(Context context) {
        pokemonData = new ArrayList<>();
        this.context = context;
    }



    @Override
    public PokemonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PokemonAdapter.ViewHolder holder, int position) {
        PokemonData pos = pokemonData.get(position);
        holder.pokeTextView.setText(pos.getName());


        /*
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).setIndicatorsEnabled(true);
        Picasso.with(context).load("http://pokeapi.co/media/sprites/pokemon/" + pos.getNumber() + ".png")
                .into(holder.pokeImageView);
        */

        //Downloading a URL into an ImageView
        Glide.with(context).load("http://pokeapi.co/media/sprites/pokemon/" + pos.getNumber() + ".png")
                .centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pokeImageView);

    }

    @Override
    public int getItemCount() {
        return pokemonData.size();
    }

    public void addingPokemonToList(ArrayList<PokemonData> pokeList) {
        pokemonData.addAll(pokeList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pokeImageView;
        private TextView pokeTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            pokeImageView = (ImageView)itemView.findViewById(R.id.pokemonIMG);
            pokeTextView = (TextView)itemView.findViewById(R.id.pokemonTXT);
        }
    }


}
