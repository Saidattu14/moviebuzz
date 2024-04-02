package com.example.moviebuzz.adapters;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Icon;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.MovieReviewsModel;
import com.example.moviebuzz.data.model.ReviewLikedDisLikedRequestBody;
import com.example.moviebuzz.databinding.MoviereviewsBinding;
import com.example.moviebuzz.ui.movie.MovieViewModel;

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
    MovieViewModel movieViewModel;
    String jwtToken;
    public MovieReviewsAdapter(List<MovieReviewsModel> movieReviewsModelList, MovieViewModel movieViewModel,String jwtToken)
    {
        this.movieReviewsModelList = movieReviewsModelList;
        this.movieViewModel = movieViewModel;
        this.jwtToken = jwtToken;
    }

    @NonNull
    @Override
    public MovieReviewsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MoviereviewsBinding moviereviewsBinding =  DataBindingUtil.inflate(inflater, R.layout.moviereviews,parent, false);
        return new MovieReviewsAdapterHolder(moviereviewsBinding,this.movieViewModel,this.jwtToken);
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
        private final MovieViewModel movieViewModel;
        private final String jwtToken;
        public MovieReviewsAdapterHolder(@NonNull MoviereviewsBinding moviereviewsBinding,MovieViewModel movieViewModel,String jwtToken) {
            super(moviereviewsBinding.getRoot());
            this.moviereviewsBinding = moviereviewsBinding;
            this.movieViewModel = movieViewModel;
            this.jwtToken = jwtToken;
        }

        public void bind(MovieReviewsModel movieReviewsModel)
        {
           if(movieReviewsModel.getFull_review().length() > 0) {
               moviereviewsBinding.comment.setText(movieReviewsModel.getFull_review());
           } else{
               moviereviewsBinding.comment.setText(movieReviewsModel.getShort_review());
           }
           moviereviewsBinding.username.setText(movieReviewsModel.getReviewer_name());
           moviereviewsBinding.likedPercent.setText(movieReviewsModel.getRating_value()+"/10");
           moviereviewsBinding.likedCount.setText(movieReviewsModel.getLikesCount().toString());
           moviereviewsBinding.dislikedCount.setText(movieReviewsModel.getDislikesCount().toString());

            moviereviewsBinding.likedIcon.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {

                    ColorFilter colorFilter = moviereviewsBinding.likedIcon.getColorFilter();
                    if(colorFilter == null) {
                        ColorFilter colorFilter1 = moviereviewsBinding.dislikedIcon.getColorFilter();
                        if(colorFilter1 != null) {
                            Integer ct = movieReviewsModel.getDislikesCount();
                            moviereviewsBinding.dislikedCount.setText(ct.toString());
                            moviereviewsBinding.dislikedIcon.setColorFilter(null);
                        }
                        Integer ct = movieReviewsModel.getLikesCount() + 1;
                        moviereviewsBinding.likedCount.setText(ct.toString());
                       // allReviewsViewModel.reviewLikedDisLikedApiRequest(new ReviewLikedDisLikedRequestBody(true,false,movieReviewsModel.getReviewId(),allReviewsViewModel.getLiveMovieReviews().getValue().getMovieId(),userId),token);
                        moviereviewsBinding.likedIcon.setColorFilter(moviereviewsBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor));
                    } else{
                        moviereviewsBinding.likedCount.setText(movieReviewsModel.getLikesCount().toString());
                        moviereviewsBinding.likedIcon.setColorFilter(null);
                    }
                }
            });
            moviereviewsBinding.dislikedIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ColorFilter colorFilter = moviereviewsBinding.dislikedIcon.getColorFilter();
                    if(colorFilter == null) {
                        ColorFilter colorFilter1 = moviereviewsBinding.likedIcon.getColorFilter();
                        if(colorFilter1 != null) {
                            Integer ct = movieReviewsModel.getLikesCount();
                            moviereviewsBinding.likedCount.setText(ct.toString());
                            moviereviewsBinding.likedIcon.setColorFilter(null);
                        }
                        Integer ct = movieReviewsModel.getDislikesCount() + 1;
                        moviereviewsBinding.dislikedCount.setText(ct.toString());
                      //  allReviewsViewModel.reviewLikedDisLikedApiRequest(new ReviewLikedDisLikedRequestBody(false,true,movieReviewsModel.getReviewId(),allReviewsViewModel.getLiveMovieReviews().getValue().getMovieId(),userId),token);
                        moviereviewsBinding.dislikedIcon.setColorFilter(moviereviewsBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor));
                    } else{
                        Integer ct = movieReviewsModel.getDislikesCount();
                        moviereviewsBinding.dislikedCount.setText(ct.toString());
                        moviereviewsBinding.dislikedIcon.setColorFilter(null);
                    }
                }
            });

        }
    }
}
