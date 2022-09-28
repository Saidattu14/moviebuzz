package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.databinding.TimingsRecycleviewBinding;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityViewModel;

import java.util.List;

public class MovieTimingsAdapter extends RecyclerView.Adapter<MovieTimingsAdapter.MovieTimingsAdapterHolder>{

    TimingsRecycleviewBinding timingsRecycleviewBinding;
    MovieAvailabilityViewModel movieAvailabilityViewModel;
    List<String> timingList;
    int updatePosition=-1;
    public MovieTimingsAdapter(List<String> timingList,MovieAvailabilityViewModel movieAvailabilityViewModel)
    {
        this.timingList = timingList;
        this.movieAvailabilityViewModel = movieAvailabilityViewModel;
    }

    @NonNull
    @Override
    public MovieTimingsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        timingsRecycleviewBinding = DataBindingUtil.inflate(inflater, R.layout.timings_recycleview,parent,false);
        return new MovieTimingsAdapter.MovieTimingsAdapterHolder(timingsRecycleviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTimingsAdapterHolder holder, int position) {
        holder.timingsRecycleviewBinding.timings.setOnClickListener(v -> {
            String time = holder.timingsRecycleviewBinding.timings.getText().toString();
            movieAvailabilityViewModel.updateMovieTime(time);
            updatePosition = position;
            notifyDataSetChanged();
        });
        holder.bind(this.timingList.get(position),position,updatePosition,this.movieAvailabilityViewModel);
    }

    @Override
    public int getItemCount() {
        return this.timingList.size();
    }

    public static  class MovieTimingsAdapterHolder extends RecyclerView.ViewHolder{

        TimingsRecycleviewBinding timingsRecycleviewBinding;
        public MovieTimingsAdapterHolder(@NonNull TimingsRecycleviewBinding timingsRecycleviewBinding)
        {
            super(timingsRecycleviewBinding.getRoot());
            this.timingsRecycleviewBinding = timingsRecycleviewBinding;
        }

        public void bind(String s, int position,int updatePosition,MovieAvailabilityViewModel movieAvailabilityViewModel)
        {
            if(position == updatePosition)
            {
                timingsRecycleviewBinding.timings.setBackgroundResource(R.drawable.date_border);
            }
            else
            {
                timingsRecycleviewBinding.timings.setBackgroundResource(R.drawable.date_change_border);
            }
            timingsRecycleviewBinding.timings.setText(s);
        }
    }
}
