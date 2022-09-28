package com.example.moviebuzz.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviebuzz.R;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.databinding.AutosearchBinding;
import com.example.moviebuzz.databinding.FragmentSearchBinding;
import com.example.moviebuzz.ui.search.SearchFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class AutoSearchAdapter extends RecyclerView.Adapter<AutoSearchAdapter.AutoSearchAdapterHolder> {

    AutosearchBinding autosearchBinding;
    SearchView searchView;
    SearchViewModel searchViewModel;
    FragmentSearchBinding fragmentSearchBinding;
    SearchFragment searchFragment;
    List<String> searchList = new ArrayList<>();


    public AutoSearchAdapter(List<String> searchList,
                             SearchView searchView, SearchViewModel searchViewModel,
                             FragmentSearchBinding fragmentSearchBinding,
                             SearchFragment searchFragment) {

        this.searchList = searchList;
        this.searchView = searchView;
        this.searchViewModel = searchViewModel;
        this.fragmentSearchBinding = fragmentSearchBinding;
        this.searchFragment = searchFragment;

    }

    @NonNull
    @Override
    public AutoSearchAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        autosearchBinding = DataBindingUtil.inflate(inflater, R.layout.autosearch, parent, false);
        return new AutoSearchAdapterHolder(autosearchBinding,fragmentSearchBinding,searchFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull AutoSearchAdapterHolder holder, int position) {
        holder.bindData(searchList.get(position),searchView,searchList,searchViewModel);
    }


    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public void clearSearchList()
    {
        this.searchList.clear();
    }

    public void  updateSearchList(List<String> searchList)
    {
        this.searchList = searchList;
    }

    static class AutoSearchAdapterHolder extends RecyclerView.ViewHolder
    {
        AutosearchBinding autosearchBinding;
        FragmentSearchBinding searchBinding;
        SearchFragment searchFragment;
        String jwtToken;
        public AutoSearchAdapterHolder(@NonNull AutosearchBinding itemView,FragmentSearchBinding searchBinding,SearchFragment searchFragment) {
            super(itemView.getRoot());
            this.autosearchBinding = itemView;
            this.searchBinding = searchBinding;
            this.searchFragment = searchFragment;
            this.jwtToken = this.searchFragment.getToken();

        }

        public void bindData(String s,SearchView searchView,List<String> searchList,SearchViewModel searchViewModel)
        {
            autosearchBinding.itemNameTextView1.setText(s);
            autosearchBinding.autoSearchConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConstraintLayout constraintLayout = (ConstraintLayout) v.findViewById(v.getId());
                    for(int i=0; i<constraintLayout.getChildCount();i++)
                    {
                        View v1 = constraintLayout.getChildAt(i);
                        if(v1.getId() == autosearchBinding.itemNameTextView1.getId())
                        {
                            searchView.setQuery(autosearchBinding.itemNameTextView1.getText(),true);
                            moviesSearchAPIRequests(searchViewModel,autosearchBinding.itemNameTextView1.getText().toString());
                            searchBinding.topAppBar.collapseActionView();
                        }
                    }
                }
            });
            autosearchBinding.executePendingBindings();
        }

        public void clearPreviousResultsAndScroll()
        {
            searchFragment.resetScrollPosition();
            searchFragment.clearCurrentSearchResult();
        }

        public void moviesSearchAPIRequests(SearchViewModel searchViewModel,String query)
        {
            double rd = 1;
            switch (query) {
                case "Top Popular Movies":
                    clearPreviousResultsAndScroll();
                    searchFragment.setApiValuesDefault(SearchApiEnum.Popular_Movies_Search);
                    searchFragment.callApiService();
                    break;
                case "Top Rated Movies":
                    clearPreviousResultsAndScroll();
                    searchFragment.setApiValuesDefault(SearchApiEnum.Rated_Movies_Search);
                    searchFragment.callApiService();
                    break;
                case "Top Movies in Comedy":
                    clearPreviousResultsAndScroll();
                    searchFragment.setApiValuesGenreBasedSearch(SearchApiEnum.Genre_Movies_Search,"Comedy");
                    searchFragment.callApiService();
                    break;
                case "Top Movies in Action":
                    clearPreviousResultsAndScroll();
                    searchFragment.setApiValuesGenreBasedSearch(SearchApiEnum.Genre_Movies_Search,"Action");
                    searchFragment.callApiService();
                    break;
            }
        }
    }
}

