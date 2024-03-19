package com.example.moviebuzz.adapters;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.SearchMoviesResponse;
import com.example.moviebuzz.databinding.SearchmoviesRecycleviewLayoutBinding;
import com.example.moviebuzz.ui.search.SearchFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchDataMoviesAdapter extends RecyclerView.Adapter<SearchDataMoviesAdapter.SearchDataMoviesViewHolder>{

    List<SearchMoviesResponse> searchMoviesResponseList = new ArrayList<>();
    SearchViewModel searchViewModel;
    SearchFragment searchFragment;


    public SearchDataMoviesAdapter(List<SearchMoviesResponse> searchMoviesResponses, SearchViewModel searchViewModel,SearchFragment searchFragment) {
        this.searchMoviesResponseList = searchMoviesResponses;
        this.searchViewModel = searchViewModel;
        this.searchFragment = searchFragment;
    }

    @NonNull
    @Override
    public SearchDataMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
         SearchmoviesRecycleviewLayoutBinding itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.searchmovies_recycleview_layout, parent, false);
        return new SearchDataMoviesViewHolder(itemLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchDataMoviesViewHolder holder, int position) {
          holder.bindData(this.searchMoviesResponseList.get(position),this.searchViewModel,this.searchFragment);
    }

    @Override
    public int getItemCount() {
        return this.searchMoviesResponseList.size();
    }

    public void update(List<SearchMoviesResponse> searchMoviesResponses)
    {
        this.searchMoviesResponseList = searchMoviesResponses;
    }

    public List<SearchMoviesResponse> getSearchMoviesResponseList() {
        return searchMoviesResponseList;
    }

    public void clearList()
    {
        this.searchMoviesResponseList.clear();
    }


    static class SearchDataMoviesViewHolder extends RecyclerView.ViewHolder {
        SearchmoviesRecycleviewLayoutBinding itemLayoutBinding;

        public SearchDataMoviesViewHolder(@NonNull SearchmoviesRecycleviewLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(SearchMoviesResponse searchMoviesResponse,SearchViewModel searchViewModel,SearchFragment searchFragment)
        {

                Integer i = searchMoviesResponse.get_source().getImdbVotes();
                itemLayoutBinding.itemNameTextView1.setText(i.toString() + " Votes");

            ImageView imageView = itemLayoutBinding.movieposter;
            Picasso.get().load(searchMoviesResponse.get_source().getPoster())
                    .into(imageView);
            itemLayoutBinding.movieposter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchViewModel.currentSearchedMovieData(searchMoviesResponse);
                    searchViewModel.setPreviousSearchResult();
                    searchFragment.navigationToMoviePage();
                }
            });
            itemLayoutBinding.itemNameTextView.setText(searchMoviesResponse.get_source().getTitle());
            itemLayoutBinding.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageViewCompat.setImageTintMode(itemLayoutBinding.favorite, PorterDuff.Mode.SRC_ATOP);
                    ImageViewCompat.setImageTintList(itemLayoutBinding.favorite,ColorStateList.valueOf(itemLayoutBinding.getRoot().getResources().getColor(R.color.textInputOutlineColor)));
                }
            });
            itemLayoutBinding.executePendingBindings();
        }
    }
}
