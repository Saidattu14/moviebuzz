package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.data.model.GenreMain;
import com.example.moviebuzz.data.model.MainData;
import com.example.moviebuzz.databinding.CountryListAdapterBinding;
import com.example.moviebuzz.databinding.GenerListAdapterBinding;
import com.example.moviebuzz.ui.movie.MovieFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;

import java.util.List;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.GenreListAdapterHolder> {

    List<GenreMain> genreList;
    GenerListAdapterBinding generListAdapterBinding;
    SearchViewModel searchViewModel;
    MovieFragment movieFragment;
    public GenreListAdapter(List<GenreMain> genreList, MovieFragment movieFragment, SearchViewModel searchViewModel)
    {
        this.genreList = genreList;
        this.searchViewModel = searchViewModel;
        this.movieFragment= movieFragment;
    }

    @NonNull
    @Override
    public GenreListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        generListAdapterBinding = DataBindingUtil.inflate(inflater, R.layout.gener_list_adapter,parent,false);
        return new GenreListAdapterHolder(generListAdapterBinding,movieFragment,searchViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreListAdapterHolder holder, int position) {
        holder.bind(this.genreList.get(position),position,this.genreList.size());
    }

    @Override
    public int getItemCount() {
        return this.genreList.size();
    }

    public static class GenreListAdapterHolder extends RecyclerView.ViewHolder{

        GenerListAdapterBinding generListAdapterBinding;
        SearchViewModel searchViewModel;
        MovieFragment movieFragment;
         public GenreListAdapterHolder(@NonNull GenerListAdapterBinding generListAdapterBinding,MovieFragment movieFragment, SearchViewModel searchViewModel)
         {
             super(generListAdapterBinding.getRoot());
             this.generListAdapterBinding = generListAdapterBinding;
             this.searchViewModel = searchViewModel;
             this.movieFragment= movieFragment;
         }

        public void bind(GenreMain mainData,int pos,int size)
        {
            if(size-1 == pos)
            {
                generListAdapterBinding.genreType.setText(mainData.getType()+".");
            }
            else
            {
                generListAdapterBinding.genreType.setText(mainData.getType()+",");
            }
            generListAdapterBinding.genreType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = mainData.getType();
                    movieFragment.callApi2(SearchApiEnum.Genre_Movies_Search,name);
                    movieFragment.navigateToSearch();
                }
            });
        }
    }
}
