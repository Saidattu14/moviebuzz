package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.data.model.MainData;
import com.example.moviebuzz.databinding.DirectorListAdapterBinding;
import com.example.moviebuzz.ui.movie.MovieFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;

import java.util.List;

public class DirectorsListAdapter extends RecyclerView.Adapter<DirectorsListAdapter.DirectorsListAdapterHolder> {

    DirectorListAdapterBinding directorListAdapterBinding;
    List<MainData> directorsList;
    SearchViewModel searchViewModel;
    MovieFragment movieFragment;
    public DirectorsListAdapter(List<MainData> directorsList,MovieFragment movieFragment, SearchViewModel searchViewModel)
    {
        this.directorsList = directorsList;
        this.searchViewModel = searchViewModel;
        this.movieFragment= movieFragment;
    }

    @NonNull
    @Override
    public DirectorsListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        directorListAdapterBinding = DataBindingUtil.inflate(inflater, R.layout.director_list_adapter,parent,false);
        return new DirectorsListAdapterHolder(directorListAdapterBinding,movieFragment,searchViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectorsListAdapterHolder holder, int position) {
          holder.bind(this.directorsList.get(position),position,directorsList.size());
    }

    @Override
    public int getItemCount() {
        return this.directorsList.size();
    }

    public static class DirectorsListAdapterHolder extends RecyclerView.ViewHolder{

        DirectorListAdapterBinding directorListAdapterBinding;
        SearchViewModel searchViewModel;
        MovieFragment movieFragment;
        public DirectorsListAdapterHolder(@NonNull DirectorListAdapterBinding directorListAdapterBinding,MovieFragment movieFragment,SearchViewModel searchViewModel)
        {
            super(directorListAdapterBinding.getRoot());
            this.directorListAdapterBinding = directorListAdapterBinding;
            this.searchViewModel = searchViewModel;
            this.movieFragment= movieFragment;
        }

        public void bind(MainData mainData,int pos,int size)
        {
            if(size-1 == pos)
            {
                directorListAdapterBinding.directorName.setText(mainData.getName()+".");
            }
            else
            {
                directorListAdapterBinding.directorName.setText(mainData.getName()+",");

            }
            directorListAdapterBinding.directorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = mainData.getName();
                    movieFragment.callApi(SearchApiEnum.WildCard_SearchOnActorsWritersDirectors,name);
                    movieFragment.navigateToSearch();
                }
            });
        }
    }
}
