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
import com.example.moviebuzz.databinding.CastListAdapterBinding;
import com.example.moviebuzz.ui.movie.MovieFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;

import java.util.List;

public class CastListAdapter extends RecyclerView.Adapter<CastListAdapter.CastListAdapterHolder> {

    CastListAdapterBinding castListAdapterBinding;
    List<MainData> castList;
    SearchViewModel searchViewModel;
    MovieFragment movieFragment;
    public CastListAdapter(List<MainData> castList,MovieFragment movieFragment,SearchViewModel searchViewModel)
    {
        this.castList = castList;
        this.searchViewModel = searchViewModel;
        this.movieFragment= movieFragment;
    }

    @NonNull
    @Override
    public CastListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        castListAdapterBinding = DataBindingUtil.inflate(inflater, R.layout.cast_list_adapter,parent,false);
        return new CastListAdapterHolder(castListAdapterBinding,movieFragment,searchViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull CastListAdapterHolder holder, int position) {
             holder.bind(castList.get(position),position,castList.size());
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public static class CastListAdapterHolder extends RecyclerView.ViewHolder{

        CastListAdapterBinding castListAdapterBinding;
        SearchViewModel searchViewModel;
        MovieFragment movieFragment;
        public CastListAdapterHolder(@NonNull CastListAdapterBinding castListAdapterBinding,MovieFragment movieFragment,SearchViewModel searchViewModel)
        {
            super(castListAdapterBinding.getRoot());
            this.castListAdapterBinding = castListAdapterBinding;
            this.searchViewModel = searchViewModel;
            this.movieFragment= movieFragment;
        }

        public void bind(MainData mainData,int pos,int size)
        {
            if(size-1 == pos)
            {

                castListAdapterBinding.castName.setText(mainData.getName()+".");
            }
            else
            {
                castListAdapterBinding.castName.setText(mainData.getName()+",");
            }
            castListAdapterBinding.castName.setOnClickListener(new View.OnClickListener() {
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
