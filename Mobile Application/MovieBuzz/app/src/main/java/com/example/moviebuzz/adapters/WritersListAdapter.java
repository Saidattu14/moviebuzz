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
import com.example.moviebuzz.databinding.WritersListAdapterBinding;
import com.example.moviebuzz.ui.movie.MovieFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;

import java.util.List;

public class WritersListAdapter extends RecyclerView.Adapter<WritersListAdapter.WritersListAdapterHolder> {

    WritersListAdapterBinding writersListAdapterBinding;
    List<MainData> writersList;
    SearchViewModel searchViewModel;
    MovieFragment movieFragment;
    public WritersListAdapter(List<MainData> writersList,MovieFragment movieFragment, SearchViewModel searchViewModel)
    {
        this.writersList = writersList;
        this.searchViewModel = searchViewModel;
        this.movieFragment= movieFragment;
    }


    @NonNull
    @Override
    public WritersListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        writersListAdapterBinding = DataBindingUtil.inflate(inflater, R.layout.writers_list_adapter,parent,false);
        return new WritersListAdapterHolder(writersListAdapterBinding,movieFragment,searchViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull WritersListAdapterHolder holder, int position) {
        holder.bind(this.writersList.get(position),position,this.writersList.size());
    }

    @Override
    public int getItemCount() {
        return this.writersList.size();
    }

    public static class WritersListAdapterHolder extends RecyclerView.ViewHolder {
        WritersListAdapterBinding writersListAdapterBinding;
        SearchViewModel searchViewModel;
        MovieFragment movieFragment;
        public WritersListAdapterHolder(@NonNull WritersListAdapterBinding writersListAdapterBinding, MovieFragment movieFragment, SearchViewModel searchViewModel)
        {
            super(writersListAdapterBinding.getRoot());
            this.writersListAdapterBinding = writersListAdapterBinding;
            this.searchViewModel = searchViewModel;
            this.movieFragment= movieFragment;
        }

        public void bind(MainData mainData,int pos,int size)
        {
            if(size-1 == pos)
            {

                writersListAdapterBinding.writerName.setText(mainData.getName()+".");
            }
            else
            {
                writersListAdapterBinding.writerName.setText(mainData.getName()+",");

            }
            writersListAdapterBinding.writerName.setOnClickListener(new View.OnClickListener() {
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
