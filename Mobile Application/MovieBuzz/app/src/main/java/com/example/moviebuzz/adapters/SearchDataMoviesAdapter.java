package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

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

            itemLayoutBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConstraintLayout constraintLayout = v.findViewById(v.getId());
                    for(int i=0; i<constraintLayout.getChildCount();i++)
                    {
                        View v1 = constraintLayout.getChildAt(i);
                        if(v1.getId() == itemLayoutBinding.itemNameTextView.getId())
                        {
                            searchViewModel.currentSearchedMovieData(searchMoviesResponse);
                            searchViewModel.setPreviousSearchResult();
                            searchFragment.navigationToMoviePage();
                        }
                    }
                }
            });
            ImageView imageView = itemLayoutBinding.movieposter;
            Picasso.get().load(searchMoviesResponse.get_source().getPoster())
                    .into(imageView);
            itemLayoutBinding.itemNameTextView.setText(searchMoviesResponse.get_source().getTitle());
            itemLayoutBinding.executePendingBindings();
        }
    }
}
