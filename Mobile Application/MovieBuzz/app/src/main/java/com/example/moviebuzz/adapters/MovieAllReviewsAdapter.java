package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
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
             this.allReviewsRecycleviewBinding.username.setText(movieReviewsModel.getUsername());
             this.allReviewsRecycleviewBinding.comment.setText(movieReviewsModel.getReview());
             this.allReviewsRecycleviewBinding.likedPercent.setText(Float.toString(movieReviewsModel.getRating()*10)+"%");
        }
    }
}

