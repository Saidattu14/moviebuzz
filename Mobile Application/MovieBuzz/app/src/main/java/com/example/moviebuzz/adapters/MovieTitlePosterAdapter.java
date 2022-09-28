package com.example.moviebuzz.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.databinding.MovieTitleAndPosterLayoutBinding;

public class MovieTitlePosterAdapter extends RecyclerView.Adapter<MovieTitlePosterAdapter.MovieTitlePosterViewHolder> {



    @NonNull
    @Override
    public MovieTitlePosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MovieTitleAndPosterLayoutBinding movieTitleAndPosterLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.movie_title_and_poster_layout,parent, false);
        return new MovieTitlePosterViewHolder(movieTitleAndPosterLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTitlePosterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MovieTitlePosterViewHolder extends RecyclerView.ViewHolder{

        MovieTitleAndPosterLayoutBinding movieTitleAndPosterLayoutBinding;
        public MovieTitlePosterViewHolder(@NonNull MovieTitleAndPosterLayoutBinding movieTitleAndPosterLayoutBinding)
        {
            super(movieTitleAndPosterLayoutBinding.getRoot());
            this.movieTitleAndPosterLayoutBinding = movieTitleAndPosterLayoutBinding;
        }
    }
}
