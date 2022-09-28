package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.CitiesData;
import com.example.moviebuzz.data.model.MovieBookingHistoryDetails;
import com.example.moviebuzz.data.model.SeatsData;
import com.example.moviebuzz.databinding.TransactionHistoryRecycleviewBinding;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityFragment;
import com.example.moviebuzz.ui.tickets.Seating1;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryAdapterViewHolder>{


     TransactionHistoryRecycleviewBinding transactionHistoryRecycleviewBinding;
     List<MovieBookingHistoryDetails> movieBookingHistoryDetailsList;
    public BookingHistoryAdapter(List<MovieBookingHistoryDetails> movieBookingHistoryDetailsList)
    {
     this.movieBookingHistoryDetailsList = movieBookingHistoryDetailsList;
    }

    @NonNull
    @Override
    public BookingHistoryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        transactionHistoryRecycleviewBinding = DataBindingUtil.inflate(inflater, R.layout.transaction_history_recycleview,parent,false);
        return new BookingHistoryAdapter.BookingHistoryAdapterViewHolder(transactionHistoryRecycleviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryAdapterViewHolder holder, int position) {
        holder.bind(movieBookingHistoryDetailsList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.movieBookingHistoryDetailsList.size();
    }

    public static class BookingHistoryAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TransactionHistoryRecycleviewBinding transactionHistoryRecycleviewBinding;
        public BookingHistoryAdapterViewHolder(@NonNull TransactionHistoryRecycleviewBinding transactionHistoryRecycleviewBinding) {
            super(transactionHistoryRecycleviewBinding.getRoot());
            this.transactionHistoryRecycleviewBinding = transactionHistoryRecycleviewBinding;
        }
        public void bind(MovieBookingHistoryDetails movieBookingHistoryDetails)
        {

            transactionHistoryRecycleviewBinding.bookingDateValue.setText(movieBookingHistoryDetails.getDate());
            transactionHistoryRecycleviewBinding.cityNameValue.setText(movieBookingHistoryDetails.getCityname());
            transactionHistoryRecycleviewBinding.countryNameValue.setText(movieBookingHistoryDetails.getCountryname());
            transactionHistoryRecycleviewBinding.movieName.setText(movieBookingHistoryDetails.getMoviename());
            transactionHistoryRecycleviewBinding.theaterNameValue.setText(movieBookingHistoryDetails.getTheatername());
            transactionHistoryRecycleviewBinding.paymentIdValue.setText(movieBookingHistoryDetails.getPaymentid().toString());
            transactionHistoryRecycleviewBinding.bookingIdValue.setText(movieBookingHistoryDetails.getBookingid().toString());
            List<String> seatsDataList = new ArrayList<>();
            List<Seating1> seatingList = movieBookingHistoryDetails.getSeating();
            String st = "";
            seatingList.forEach(seating1 -> {
                if(seating1.getSeat_numbers() != null)
                {
                    seatsDataList.addAll(seating1.getSeat_numbers());
                }
            });
            for(int i=0; i<seatsDataList.size();i++)
            {
                st = st + seatsDataList.get(i);
            }
            transactionHistoryRecycleviewBinding.seatingValue.setText(st);
            ImageView imageView = transactionHistoryRecycleviewBinding.moviePoster;
            Picasso.get().load(movieBookingHistoryDetails.getMovieposter())
                    .into(imageView);
            String show_value = getShow_idThis(movieBookingHistoryDetails.getShow_id());
            transactionHistoryRecycleviewBinding.showTimingValue.setText(show_value);
            transactionHistoryRecycleviewBinding.transactionResultStatus.setText(movieBookingHistoryDetails.getPaymentStatus());
        }

        private String getShow_idThis(String time)
        {

            if(time.equals("0"))
            {
                return "10:00 A.M";
            }
            else if(time.equals("1"))
            {
                return "01:00 P.M";
            }
            else if(time.equals("2"))
            {
                return "03:30 P.M";
            }
            else if(time.equals("3"))
            {
                return "06:00 P.M";
            }
            return "10:00 A.M";
        }
    }
}
