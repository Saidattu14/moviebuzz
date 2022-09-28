package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.TicketsDetailsModel;
import com.example.moviebuzz.databinding.DatesRecycleviewLayoutBinding;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityViewModel;

import java.util.List;

public class MovieAvailableDates extends RecyclerView.Adapter<MovieAvailableDates.MovieAvailableDateHolder> {

    DatesRecycleviewLayoutBinding datesRecycleviewLayoutBinding;
    List<TicketsDetailsModel> ticketsDetailsModelList;
    MovieAvailabilityViewModel movieAvailabilityViewModel;
    int updatePosition=-1;
    public MovieAvailableDates(List<TicketsDetailsModel> ticketsDetailsModelList, MovieAvailabilityViewModel movieAvailabilityViewModel)
    {
        this.ticketsDetailsModelList = ticketsDetailsModelList;
        this.movieAvailabilityViewModel = movieAvailabilityViewModel;

    }

    @NonNull
    @Override
    public MovieAvailableDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        datesRecycleviewLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.dates_recycleview_layout,parent,false);
        return new MovieAvailableDateHolder(datesRecycleviewLayoutBinding,movieAvailabilityViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAvailableDateHolder holder, int position) {

        holder.datesRecycleviewLayoutBinding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = holder.datesRecycleviewLayoutBinding.date.getText().toString();
                movieAvailabilityViewModel.updateMovieDate(date);
                updatePosition = position;
                notifyDataSetChanged();
            }
        });
        holder.bind(this.ticketsDetailsModelList.get(position).getDate(),position,updatePosition);
    }


    @Override
    public int getItemCount() {
        return this.ticketsDetailsModelList.size();
    }

    public static class MovieAvailableDateHolder extends RecyclerView.ViewHolder {

        DatesRecycleviewLayoutBinding datesRecycleviewLayoutBinding;
        MovieAvailabilityViewModel movieAvailabilityViewModel;
        public MovieAvailableDateHolder(@NonNull DatesRecycleviewLayoutBinding datesRecycleviewLayoutBinding,MovieAvailabilityViewModel movieAvailabilityViewModel) {
            super(datesRecycleviewLayoutBinding.getRoot());
            this.datesRecycleviewLayoutBinding = datesRecycleviewLayoutBinding;
            this.movieAvailabilityViewModel  = movieAvailabilityViewModel;
        }

        public void bind(String s, int postion,int updatePostion)
        {
            if(postion == updatePostion)
            {
                datesRecycleviewLayoutBinding.date.setBackgroundResource(R.drawable.date_border);
            }
            else
            {
                datesRecycleviewLayoutBinding.date.setBackgroundResource(R.drawable.date_change_border);
            }
            datesRecycleviewLayoutBinding.date.setText(s);
        }
    }
}
