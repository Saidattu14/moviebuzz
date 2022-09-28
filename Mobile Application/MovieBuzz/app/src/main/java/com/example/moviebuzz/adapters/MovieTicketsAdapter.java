package com.example.moviebuzz.adapters;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.SeatsData;
import com.example.moviebuzz.databinding.TicketDetailsRecycleviewBinding;
import com.example.moviebuzz.ui.tickets.Seating;

import java.util.List;


public class MovieTicketsAdapter extends RecyclerView.Adapter<MovieTicketsAdapter.MovieTicketsAdapterHolder> {

    TicketDetailsRecycleviewBinding ticketDetailsRecycleviewBinding;
    List<SeatsData> seatsData;
    Seating seating;
    public  MovieTicketsAdapter(List<SeatsData> seatsData, Seating seating)
    {
        this.seatsData = seatsData;
        this.seating = seating;
    }
    @NonNull
    @Override
    public MovieTicketsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ticketDetailsRecycleviewBinding =  DataBindingUtil.inflate(inflater, R.layout.ticket_details_recycleview,parent, false);
        return new MovieTicketsAdapter.MovieTicketsAdapterHolder(ticketDetailsRecycleviewBinding, seating);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTicketsAdapterHolder holder, int position) {
        holder.bind(this.seatsData.get(position));
    }

    @Override
    public int getItemCount() {
        return this.seatsData.size();
    }

    public static class MovieTicketsAdapterHolder extends RecyclerView.ViewHolder {

        TicketDetailsRecycleviewBinding ticketDetailsRecycleviewBinding;
        Seating seating;
        int greenColorId;
        public MovieTicketsAdapterHolder(@NonNull TicketDetailsRecycleviewBinding ticketDetailsRecycleviewBinding, Seating seating)
        {
            super(ticketDetailsRecycleviewBinding.getRoot());
            this.ticketDetailsRecycleviewBinding = ticketDetailsRecycleviewBinding;
            this.seating = seating;
            this.greenColorId = ticketDetailsRecycleviewBinding.getRoot().getResources().getColor(R.color.green);
        }

        public void bind(SeatsData seatsData)
        {

            if(seatsData.getStatus().equals("UNBOOKED") == true)
            {
                ticketDetailsRecycleviewBinding.rowData.setText(seatsData.getSeat_id());
                ticketDetailsRecycleviewBinding.rowData.setBackgroundResource(R.color.green);
                ticketDetailsRecycleviewBinding.rowData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ColorDrawable colorDrawable1 = (ColorDrawable) ticketDetailsRecycleviewBinding.rowData.getBackground();
                        String seat_id = ticketDetailsRecycleviewBinding.rowData.getText().toString();
                        if(greenColorId == colorDrawable1.getColor())
                        {
                            seating.addSeatDetails(seat_id.substring(0,1),seat_id);
                            ticketDetailsRecycleviewBinding.rowData.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                        else
                        {
                            seating.deleteSeatDetails(seat_id.substring(0,1),seat_id);
                            ticketDetailsRecycleviewBinding.rowData.setBackgroundResource(R.color.green);
                        }
                    }
                });
            }
            else
            {
                ticketDetailsRecycleviewBinding.rowData.setText(seatsData.getSeat_id());
                ticketDetailsRecycleviewBinding.rowData.setBackgroundResource(R.color.red);
            }
        }
    }
}
