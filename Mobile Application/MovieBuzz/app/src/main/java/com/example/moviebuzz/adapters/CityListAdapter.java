package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.CitiesData;
import com.example.moviebuzz.databinding.CityListRecycleviewLayoutBinding;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityFragment;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListAdapterHolder>{



    CityListRecycleviewLayoutBinding cityListRecycleviewLayoutBinding;
    List<CitiesData> cityList;
    MovieAvailabilityFragment movieAvailabilityFragment;
    public CityListAdapter(List<CitiesData> cityList, MovieAvailabilityFragment movieAvailabilityFragment)
    {

        this.cityList = cityList;
        this.movieAvailabilityFragment = movieAvailabilityFragment;

    }

    @NonNull
    @Override
    public CityListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        cityListRecycleviewLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.city_list_recycleview_layout,parent,false);
        return new CityListAdapterHolder(cityListRecycleviewLayoutBinding);

    }

    public void update(List<CitiesData> cityList)
    {
        this.cityList = cityList;
    }

    public void clear()
    {
        this.cityList.clear();
    }


    @Override
    public void onBindViewHolder(@NonNull CityListAdapterHolder holder, int position) {

            holder.bind(this.cityList.get(position), movieAvailabilityFragment);
    }

    @Override
    public int getItemCount() {
        return this.cityList.size();
    }

    public static class CityListAdapterHolder extends RecyclerView.ViewHolder {

        CityListRecycleviewLayoutBinding cityListRecycleviewLayoutBinding;
        public CityListAdapterHolder(@NonNull CityListRecycleviewLayoutBinding cityListRecycleviewLayoutBinding)
        {
            super(cityListRecycleviewLayoutBinding.getRoot());
            this.cityListRecycleviewLayoutBinding = cityListRecycleviewLayoutBinding;
        }

        public void bind(CitiesData c, MovieAvailabilityFragment movieAvailabilityFragment)
        {

            cityListRecycleviewLayoutBinding.countryListLayout.setText(c.getName());
            cityListRecycleviewLayoutBinding.autoSearchConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConstraintLayout constraintLayout = (ConstraintLayout) v.findViewById(v.getId());
                    for(int i=0; i<constraintLayout.getChildCount();i++)
                    {
                        View v1 = constraintLayout.getChildAt(i);
                        if(v1.getId() == cityListRecycleviewLayoutBinding.countryListLayout.getId())
                        {
                           movieAvailabilityFragment.updateTextView(c.getName());
                        }
                    }
                }
            });
        }

   }
}
