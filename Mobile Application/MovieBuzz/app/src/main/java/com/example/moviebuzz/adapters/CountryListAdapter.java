package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.data.model.CountryData;
import com.example.moviebuzz.data.model.MainData;
import com.example.moviebuzz.databinding.CastListAdapterBinding;
import com.example.moviebuzz.databinding.CountryListAdapterBinding;
import com.example.moviebuzz.ui.movie.MovieFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryListAdapterHolder> {
    CountryListAdapterBinding countryListAdapterBinding;
    List<CountryData> countryList = new ArrayList<>();
    SearchViewModel searchViewModel;
    MovieFragment movieFragment;
    public CountryListAdapter(List<CountryData> countryList, MovieFragment movieFragment, SearchViewModel searchViewModel) {
        this.countryList = countryList;
        this.searchViewModel = searchViewModel;
        this.movieFragment= movieFragment;
    }

    @NonNull
    @Override
    public  CountryListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        countryListAdapterBinding = DataBindingUtil.inflate(inflater, R.layout.country_list_adapter,parent,false);
        return new CountryListAdapterHolder(countryListAdapterBinding,movieFragment,searchViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull  CountryListAdapterHolder holder, int position) {
            holder.bind(this.countryList.get(position),position,this.countryList.size());
    }

    @Override
    public int getItemCount() {
        return this.countryList.size();
    }

    public static class CountryListAdapterHolder extends RecyclerView.ViewHolder {

        CountryListAdapterBinding countryListAdapterBinding;
        SearchViewModel searchViewModel;
        MovieFragment movieFragment;
        public CountryListAdapterHolder(@NonNull CountryListAdapterBinding itemView,MovieFragment movieFragment,SearchViewModel searchViewModel) {
            super(itemView.getRoot());
            this.countryListAdapterBinding = itemView;
            this.searchViewModel = searchViewModel;
            this.movieFragment= movieFragment;
        }

        public void bind(CountryData mainData, int pos, int size)
        {
            if(size-1 == pos) {
                countryListAdapterBinding.countryName.setText(mainData.getName()+".");
            }
            else {
                countryListAdapterBinding.countryName.setText(mainData.getName()+",");
            }
            countryListAdapterBinding.countryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String countryName = mainData.getName();
                    movieFragment.callApi1(SearchApiEnum.Country_Based_Search,countryName);
                    movieFragment.navigateToSearch();
                }
            });
        }
    }
}
