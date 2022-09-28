package com.example.moviebuzz.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.MovieReviewsModel;
import com.example.moviebuzz.databinding.MoviereviewsBinding;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsAdapterHolder> {

    List<MovieReviewsModel> movieReviewsModelList;
    public MovieReviewsAdapter(List<MovieReviewsModel> movieReviewsModelList)
    {
        this.movieReviewsModelList = movieReviewsModelList;
    }

    @NonNull
    @Override
    public MovieReviewsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MoviereviewsBinding moviereviewsBinding =  DataBindingUtil.inflate(inflater, R.layout.moviereviews,parent, false);
        return new MovieReviewsAdapterHolder(moviereviewsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsAdapterHolder holder, int position) {
        holder.bind(this.movieReviewsModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.movieReviewsModelList.size();
    }

    static class MovieReviewsAdapterHolder extends RecyclerView.ViewHolder{

        private final MoviereviewsBinding moviereviewsBinding;
        public MovieReviewsAdapterHolder(@NonNull MoviereviewsBinding moviereviewsBinding) {
            super(moviereviewsBinding.getRoot());
            this.moviereviewsBinding = moviereviewsBinding;
        }

        public void bind(MovieReviewsModel movieReviewsModel)
        {
           moviereviewsBinding.username.setText(movieReviewsModel.getUsername());
           moviereviewsBinding.comment.setText(movieReviewsModel.getReview());
           moviereviewsBinding.likedPercent.setText(movieReviewsModel.getRating()+"%");
        }
    }
}
