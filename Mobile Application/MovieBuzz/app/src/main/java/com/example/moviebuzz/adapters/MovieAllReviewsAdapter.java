package com.example.moviebuzz.adapters;

import android.annotation.SuppressLint;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.MovieReviewsModel;
import com.example.moviebuzz.databinding.AllReviewsRecycleviewBinding;

import java.util.List;

public class MovieAllReviewsAdapter extends RecyclerView.Adapter<MovieAllReviewsAdapter.MovieAllReviewsHolder> {

   AllReviewsRecycleviewBinding allReviewsRecycleviewBinding;
   List<MovieReviewsModel> movieReviewsModelList;

   public MovieAllReviewsAdapter(List<MovieReviewsModel> movieReviewsModelList)
   {
       this.movieReviewsModelList = movieReviewsModelList;
   }


    @NonNull
    @Override
    public MovieAllReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        allReviewsRecycleviewBinding = DataBindingUtil.inflate(inflater,R.layout.all_reviews_recycleview,parent,false);
       return new MovieAllReviewsHolder(allReviewsRecycleviewBinding);
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
        public MovieAllReviewsHolder(@NonNull AllReviewsRecycleviewBinding allReviewsRecycleviewBinding)
        {
          super(allReviewsRecycleviewBinding.getRoot());
          this.allReviewsRecycleviewBinding = allReviewsRecycleviewBinding;
        }

        public void bind(MovieReviewsModel movieReviewsModel)
        {
             allReviewsRecycleviewBinding.username.setText(movieReviewsModel.getReviewer_name());
             allReviewsRecycleviewBinding.comment.setText(movieReviewsModel.getFull_review());
             allReviewsRecycleviewBinding.likedPercent.setText(movieReviewsModel.getRating_value()+"/10");
             allReviewsRecycleviewBinding.likedCount.setText(movieReviewsModel.getLikesCount().toString());
             allReviewsRecycleviewBinding.dislikedCount.setText(movieReviewsModel.getDislikesCount().toString());
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
                    System.out.println( allReviewsRecycleviewBinding.likedIcon.getResources().getColorStateList(allReviewsRecycleviewBinding.likedIcon.getId()));
                    ColorFilter colorFilter = allReviewsRecycleviewBinding.likedIcon.getColorFilter();
                    if(colorFilter != null) {

                        @SuppressLint("ResourceAsColor") PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(R.color.textInputOutlineColor, PorterDuff.Mode.DST_OUT);
//                        System.out.println(porterDuffColorFilter);
//                        System.out.println(colorFilter);
                    }

//                    System.out.println(allReviewsRecycleviewBinding.likedIcon.getImageTintMode());
//
//                   if(moviereviewsBinding.likedIcon.getColorFilter().equals(moviereviewsBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor))) {
//                       moviereviewsBinding.likedIcon.setColorFilter(moviereviewsBinding.getRoot().getResources().getColor(R.color.darkGrey));
//                   }
//                   if(moviereviewsBinding.likedIcon.getColorFilter().equals(moviereviewsBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor))) {
//                       moviereviewsBinding.likedIcon.setColorFilter(moviereviewsBinding.getRoot().getResources().getColor(R.color.darkGrey));
//                   } else {
                    //moviereviewsBinding.likedIcon.getColorFilter().equals(moviereviewsBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor));
                    allReviewsRecycleviewBinding.likedIcon.setColorFilter(allReviewsRecycleviewBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor));


//                   }
                }
            });
            allReviewsRecycleviewBinding.dislikedIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                   if(moviereviewsBinding.likedIcon.getColorFilter().equals(moviereviewsBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor))) {
//                       moviereviewsBinding.likedIcon.setColorFilter(moviereviewsBinding.getRoot().getResources().getColor(R.color.darkGrey));
//                   }
//                   if(moviereviewsBinding.dislikedIcon.getColorFilter().equals(moviereviewsBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor))) {
//                       moviereviewsBinding.dislikedIcon.setColorFilter(moviereviewsBinding.getRoot().getResources().getColor(R.color.darkGrey));
//                   } else{
                    allReviewsRecycleviewBinding.dislikedIcon.setColorFilter(allReviewsRecycleviewBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor));
//                   }
//
//                   System.out.println(movieReviewsModel.getReviewId());
                }
            });

        }
    }
}

