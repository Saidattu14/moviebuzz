package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.Seats;
import com.example.moviebuzz.data.model.Shows;
import com.example.moviebuzz.data.model.TheatersAndTicketsModel;
import com.example.moviebuzz.data.model.TicketsDetailsModel;
import com.example.moviebuzz.databinding.TheatersRecycleviewLayoutBinding;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityFragment;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityViewModel;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailablityTimeDate;

import java.util.ArrayList;
import java.util.List;

public class TheatersAdapter extends RecyclerView.Adapter<TheatersAdapter.TheatersAdapterHolder>
{

    List<TheatersAndTicketsModel> theatersAndTicketsModelList;
    TheatersRecycleviewLayoutBinding theatersRecycleviewLayoutBinding;
    MovieAvailabilityViewModel movieAvailabilityViewModel;
    LifecycleOwner lifecycleOwner;
    MovieAvailabilityFragment movieAvailabilityFragment;
    public TheatersAdapter(List<TheatersAndTicketsModel> theatersAndTicketsModelList, MovieAvailabilityViewModel movieAvailabilityViewModel, LifecycleOwner lifecycleOwner, MovieAvailabilityFragment movieAvailabilityFragment)
    {
        this.theatersAndTicketsModelList = theatersAndTicketsModelList;
        this.movieAvailabilityViewModel = movieAvailabilityViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.movieAvailabilityFragment = movieAvailabilityFragment;
    }

    @NonNull
    @Override
    public TheatersAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        theatersRecycleviewLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.theaters_recycleview_layout,parent,false);
        return new TheatersAdapterHolder(theatersRecycleviewLayoutBinding,movieAvailabilityViewModel,lifecycleOwner, movieAvailabilityFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TheatersAdapterHolder holder, int position) {
        holder.bind(this.theatersAndTicketsModelList.get(position));
    }

    public void update(List<TheatersAndTicketsModel> theatersAndTicketsModelList)
    {
        this.theatersAndTicketsModelList = theatersAndTicketsModelList;
    }

    public void clearList()
    {
        this.theatersAndTicketsModelList.clear();
    }

    @Override
    public int getItemCount() {
        return this.theatersAndTicketsModelList.size();
    }

    public static class TheatersAdapterHolder extends RecyclerView.ViewHolder
   {
       TheatersRecycleviewLayoutBinding theatersRecycleviewLayoutBinding;
       MovieAvailabilityViewModel movieAvailabilityViewModel;
       LifecycleOwner lifecycleOwner;
       String selectedDate;
       String selectedTime;
       MovieAvailabilityFragment movieAvailabilityFragment;
       List<String> timingsList = new ArrayList<>();
       public TheatersAdapterHolder(@NonNull TheatersRecycleviewLayoutBinding theatersRecycleviewLayoutBinding, MovieAvailabilityViewModel movieAvailabilityViewModel, LifecycleOwner lifecycleOwner, MovieAvailabilityFragment movieAvailabilityFragment)
       {
           super(theatersRecycleviewLayoutBinding.getRoot());
           this.movieAvailabilityViewModel = movieAvailabilityViewModel;
           this.theatersRecycleviewLayoutBinding = theatersRecycleviewLayoutBinding;
           this.timingsList.add("10:00 A.M");
           this.timingsList.add("01:00 P.M");
           this.timingsList.add("03:30 P.M");
           this.timingsList.add("06:00 P.M");
           this.lifecycleOwner = lifecycleOwner;
           this.movieAvailabilityFragment = movieAvailabilityFragment;
           filterTickets();
       }

       private void bind(TheatersAndTicketsModel theatersAndTicketsModel)
       {

           //System.out.println(this.theatersRecycleviewLayoutBinding.getLifecycleOwner());
           theatersRecycleviewLayoutBinding.theaterName1.setText(theatersAndTicketsModel.getTheater_name());
           theatersRecycleviewLayoutBinding.cityName1.setText(theatersAndTicketsModel.getCity());
           MovieAvailableDates movieAvailableDates = (MovieAvailableDates) theatersRecycleviewLayoutBinding.dateDetails.getAdapter();
           if(movieAvailableDates == null)
           {
               movieAvailableDates = new MovieAvailableDates(theatersAndTicketsModel.getAvailable_booking_data(),movieAvailabilityViewModel);
               theatersRecycleviewLayoutBinding.dateDetails.setAdapter(movieAvailableDates);
           }
           MovieTimingsAdapter movieTimingsAdapter = (MovieTimingsAdapter) theatersRecycleviewLayoutBinding.timingsDetails.getAdapter();
           if(movieTimingsAdapter == null)
           {
               movieTimingsAdapter = new MovieTimingsAdapter(timingsList,movieAvailabilityViewModel);
               theatersRecycleviewLayoutBinding.timingsDetails.setAdapter(movieTimingsAdapter);
           }

           theatersRecycleviewLayoutBinding.next.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  if(selectedDate != null && selectedTime != null)
                  {
                      //System.out.println(selectedDate);
                      //System.out.println(selectedTime);
                      List<Seats> seats = getSeatsData(theatersAndTicketsModel.getAvailable_booking_data());
                      System.out.println(seats);
                      movieAvailabilityViewModel.updateSeatsData(seats);
                      movieAvailabilityViewModel.setSelectedTheaterData(theatersAndTicketsModel);
                      movieAvailabilityFragment.navigate();
                  }
               }
           });
       }

       private void filterTickets()
       {
         movieAvailabilityViewModel.getTimeDate().observe(this.lifecycleOwner, new Observer<MovieAvailablityTimeDate>() {
             @Override
             public void onChanged(MovieAvailablityTimeDate movieAvailablityTimeDate) {
                 if(movieAvailablityTimeDate != null)
                 {
                     if(movieAvailablityTimeDate.getError() == null)
                     {
                         selectedDate = movieAvailablityTimeDate.getDate();
                         selectedTime = movieAvailablityTimeDate.getTime();
                     }
                 }
             }
         });
       }

       private List<Seats> getSeatsData(List<TicketsDetailsModel> ticketsDetailsModelList)
       {
           for(int i=0;i<ticketsDetailsModelList.size();i++){

               if(ticketsDetailsModelList.get(i).getDate().equals(selectedDate))
               {
                   List<Shows> shows = ticketsDetailsModelList.get(i).getShows_details();
                   for(int j=0; j<shows.size();j++)
                   {
                       StringBuffer resString = new StringBuffer(shows.get(j).getTimings());
                       if(selectedTime.equals(resString.toString()))
                       {
                           return shows.get(j).getAvailable_seats();
                       }
                   }
               }
           }
           return null;
       }
   }
}
