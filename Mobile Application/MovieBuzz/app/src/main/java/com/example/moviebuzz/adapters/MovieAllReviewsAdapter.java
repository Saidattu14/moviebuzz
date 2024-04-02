package com.example.moviebuzz.adapters;

import android.annotation.SuppressLint;
import android.graphics.ColorFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.MovieReviewsModel;
import com.example.moviebuzz.data.model.ReviewDeleteRequestBody;
import com.example.moviebuzz.data.model.ReviewLikedDisLikedRequestBody;
import com.example.moviebuzz.databinding.AllReviewsRecycleviewBinding;
import com.example.moviebuzz.ui.reviews.FragmentAllReviewsViewModel;

import java.util.List;

public class MovieAllReviewsAdapter extends RecyclerView.Adapter<MovieAllReviewsAdapter.MovieAllReviewsHolder> {

   AllReviewsRecycleviewBinding allReviewsRecycleviewBinding;
   List<MovieReviewsModel> movieReviewsModelList;
   FragmentAllReviewsViewModel allReviewsViewModel;
   String userId;
   String token;
   String userName;
   public MovieAllReviewsAdapter(List<MovieReviewsModel> movieReviewsModelList,FragmentAllReviewsViewModel allReviewsViewModel,String userId,String token,String userName)
   {
       this.movieReviewsModelList = movieReviewsModelList;
       this.allReviewsViewModel = allReviewsViewModel;
       this.userId = userId;
       this.token = token;
       this.userName = userName;
   }


    @NonNull
    @Override
    public MovieAllReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        allReviewsRecycleviewBinding = DataBindingUtil.inflate(inflater,R.layout.all_reviews_recycleview,parent,false);
       return new MovieAllReviewsHolder(allReviewsRecycleviewBinding,allReviewsViewModel,userId,token,userName);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAllReviewsHolder holder, int position) {
        holder.bind(this.movieReviewsModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.movieReviewsModelList.size();
    }

    static class MovieAllReviewsHolder extends RecyclerView.ViewHolder {
        AllReviewsRecycleviewBinding allReviewsRecycleviewBinding;
        FragmentAllReviewsViewModel allReviewsViewModel;
        String userId;
        String token;
        String userName;
        public MovieAllReviewsHolder(@NonNull AllReviewsRecycleviewBinding allReviewsRecycleviewBinding,FragmentAllReviewsViewModel allReviewsViewModel,String userId,String token,String userName)
        {
          super(allReviewsRecycleviewBinding.getRoot());
          this.allReviewsRecycleviewBinding = allReviewsRecycleviewBinding;
          this.allReviewsViewModel = allReviewsViewModel;
          this.userId = userId;
          this.token = token;
          this.userName = userName;
        }

        public void bind(MovieReviewsModel movieReviewsModel) {

             allReviewsRecycleviewBinding.username.setText(movieReviewsModel.getReviewer_name());
             allReviewsRecycleviewBinding.comment.setText(movieReviewsModel.getFull_review());
             allReviewsRecycleviewBinding.likedPercent.setText(movieReviewsModel.getRating_value()+"/10");
             allReviewsRecycleviewBinding.likedCount.setText(movieReviewsModel.getLikesCount().toString());
             allReviewsRecycleviewBinding.dislikedCount.setText(movieReviewsModel.getDislikesCount().toString());

             if(userName.equals(movieReviewsModel.getReviewer_name())) {
                 allReviewsRecycleviewBinding.deleteIcon.setVisibility(View.VISIBLE);
             } else{
                 allReviewsRecycleviewBinding.deleteIcon.setVisibility(View.INVISIBLE);
             }

             allReviewsRecycleviewBinding.deleteIcon.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     allReviewsViewModel.reviewDeleteApiRequest(new ReviewDeleteRequestBody(movieReviewsModel.getReviewId(),allReviewsViewModel.getLiveMovieReviews().getValue().getMovieId()),token);
                 }
             });

            if(movieReviewsModel.getFull_review().length() > 0) {
                allReviewsRecycleviewBinding.comment.setText(movieReviewsModel.getFull_review());
            } else{
                allReviewsRecycleviewBinding.comment.setText(movieReviewsModel.getShort_review());
            }
            allReviewsRecycleviewBinding.comment.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    allReviewsRecycleviewBinding.getRoot().getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            allReviewsRecycleviewBinding.likedIcon.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {

                    ColorFilter colorFilter = allReviewsRecycleviewBinding.likedIcon.getColorFilter();
                    if(colorFilter == null) {
                        ColorFilter colorFilter1 = allReviewsRecycleviewBinding.dislikedIcon.getColorFilter();
                        if(colorFilter1 != null) {
                            Integer ct = movieReviewsModel.getDislikesCount();
                            allReviewsRecycleviewBinding.dislikedCount.setText(ct.toString());
                            allReviewsRecycleviewBinding.dislikedIcon.setColorFilter(null);
                        }
                        Integer ct = movieReviewsModel.getLikesCount() + 1;
                        allReviewsRecycleviewBinding.likedCount.setText(ct.toString());
                        allReviewsViewModel.reviewLikedDisLikedApiRequest(new ReviewLikedDisLikedRequestBody(true,false,movieReviewsModel.getReviewId(),allReviewsViewModel.getLiveMovieReviews().getValue().getMovieId(),userId),token);
                        allReviewsRecycleviewBinding.likedIcon.setColorFilter(allReviewsRecycleviewBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor));
                    } else{
                        allReviewsRecycleviewBinding.likedCount.setText(movieReviewsModel.getLikesCount().toString());
                        allReviewsRecycleviewBinding.likedIcon.setColorFilter(null);
                    }
                }
            });
            allReviewsRecycleviewBinding.dislikedIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ColorFilter colorFilter = allReviewsRecycleviewBinding.dislikedIcon.getColorFilter();
                    if(colorFilter == null) {
                        ColorFilter colorFilter1 = allReviewsRecycleviewBinding.likedIcon.getColorFilter();
                        if(colorFilter1 != null) {
                            Integer ct = movieReviewsModel.getLikesCount();
                            allReviewsRecycleviewBinding.likedCount.setText(ct.toString());
                            allReviewsRecycleviewBinding.likedIcon.setColorFilter(null);
                        }
                        Integer ct = movieReviewsModel.getDislikesCount() + 1;
                        allReviewsRecycleviewBinding.dislikedCount.setText(ct.toString());
                        allReviewsViewModel.reviewLikedDisLikedApiRequest(new ReviewLikedDisLikedRequestBody(false,true,movieReviewsModel.getReviewId(),allReviewsViewModel.getLiveMovieReviews().getValue().getMovieId(),userId),token);
                        allReviewsRecycleviewBinding.dislikedIcon.setColorFilter(allReviewsRecycleviewBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor));
                    } else{
                        Integer ct = movieReviewsModel.getDislikesCount();
                        allReviewsRecycleviewBinding.dislikedCount.setText(ct.toString());
                        allReviewsRecycleviewBinding.dislikedIcon.setColorFilter(null);
                    }
                }
            });

        }
    }
}

