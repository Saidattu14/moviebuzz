package com.example.moviebuzz.ui.movieAvalablity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviebuzz.R;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.example.moviebuzz.webSockets.WebSocketClass;
import com.example.moviebuzz.webSockets.WebSocketEcho;
import com.example.moviebuzz.adapters.CityListAdapter;
import com.example.moviebuzz.adapters.TheatersAdapter;
import com.example.moviebuzz.data.model.CitiesData;
import com.example.moviebuzz.data.model.SearchMoviesResponse;
import com.example.moviebuzz.data.model.TheaterRequestModel;
import com.example.moviebuzz.databinding.FragmentMovieAvailabilityBinding;
import com.example.moviebuzz.ui.search.SearchViewModel;
import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.WebSocket;

@AndroidEntryPoint
public class MovieAvailabilityFragment extends Fragment {

    FragmentMovieAvailabilityBinding binding;
    MovieAvailabilityViewModel movieAvailabilityViewModel;
    private SearchViewModel searchViewModel;
    @Inject
    WebSocketClass webSocketClass;
    WebSocket webSocket;
    WebSocketEcho webSocketEcho;
    private List<CitiesData> citiesData;
    private MainViewModel mainViewModel;
    private SearchMoviesResponse searchMoviesResponse;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMovieAvailabilityBinding.inflate(inflater,container,false);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        webSocket = webSocketClass.getWebSocket();
        webSocketEcho = webSocketClass.getWebSocketEcho();
        movieAvailabilityViewModel = new ViewModelProvider(requireActivity()).get(MovieAvailabilityViewModel.class);
        webSocketEcho.setMovieAvailabilityViewModel(movieAvailabilityViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMovieData();
        binding.cityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                binding.theatersBtn.setVisibility(View.INVISIBLE);
               List<CitiesData> citiesData2 = citiesData
                       .stream()
                       .filter(citiesData1 -> citiesData1.getName().contains(s.toString()))
                       .collect(Collectors.toList());
               CityListAdapter adapter = (CityListAdapter) binding.countryList.getAdapter();
               if(adapter == null)
               {
                   adapter = new CityListAdapter(citiesData2, MovieAvailabilityFragment.this);
               }
               adapter.update(citiesData2);
               adapter.notifyDataSetChanged();
            }
        });

        binding.theatersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               requestTheatersData();
               binding.loading.setVisibility(View.VISIBLE);
            }
        });

        movieAvailabilityViewModel.getCurrentMovieAvailability().observe(getViewLifecycleOwner(), new Observer<MovieAvailabilityResult>() {
            @Override
            public void onChanged(MovieAvailabilityResult movieAvailabilityResult) {
                binding.loading.setVisibility(View.INVISIBLE);
                TheatersAdapter theatersAdapter =(TheatersAdapter) binding.theatersData.getAdapter();
                if(theatersAdapter == null && movieAvailabilityResult != null && movieAvailabilityResult.getTheatersAndTicketsModelList().size() != 0)
                {
                    theatersAdapter = new TheatersAdapter(movieAvailabilityResult.getTheatersAndTicketsModelList(),movieAvailabilityViewModel,getViewLifecycleOwner(), MovieAvailabilityFragment.this);
                    binding.theatersData.setAdapter(theatersAdapter);
                }
                else if(movieAvailabilityResult != null && movieAvailabilityResult.getTheatersAndTicketsModelList().size() != 0)
                {
                    theatersAdapter.update(movieAvailabilityResult.getTheatersAndTicketsModelList());
                    theatersAdapter.notifyDataSetChanged();
                }
                else if(movieAvailabilityResult != null)
                {
                    if(movieAvailabilityResult.getTheatersAndTicketsModelList().size() == 0 && !movieAvailabilityResult.isShowData())
                    {
                        binding.theatersResult.setText("Sorry No Theaters Found For This Movie");
                        binding.theatersResult.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void requestTheatersData()
    {
        TheaterRequestModel theaterRequestModel = new TheaterRequestModel(
                UUID.randomUUID(),
                "GetTheaters",binding.cityInput.getText().toString(),
                searchMoviesResponse.get_source().getTitle(),
                mainViewModel.getToken(),
                mainViewModel.getUserId());
        Gson gson = new Gson();
        String json = gson.toJson(theaterRequestModel,TheaterRequestModel.class);
        webSocket.send(json.toString());
    }

    public void getMovieData()
    {
        searchViewModel.getSearchedMovieData().observe(getViewLifecycleOwner(), searchedMovieData -> {
            searchMoviesResponse = searchedMovieData.getCurrentSearchedMovieData();
            citiesData = searchMoviesResponse.get_source().getCities();
            CityListAdapter cityListAdapter = new CityListAdapter(citiesData,this);
            binding.countryList.setAdapter(cityListAdapter);
        });
    }

    public void updateTextView (String s)
    {
        binding.cityInput.setText(s);
        CityListAdapter adapter = (CityListAdapter) binding.countryList.getAdapter();
        adapter.clear();
        adapter.notifyDataSetChanged();
        binding.theatersBtn.setVisibility(View.VISIBLE);
    }

    public void navigate()
    {
        NavHostFragment.findNavController(MovieAvailabilityFragment.this)
                .navigate(R.id.action_movieAvailability_to_movieTicketsFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//      this.webSocket.close(1000,"Good Bye");
        TheatersAdapter theatersAdapter = (TheatersAdapter) binding.theatersData.getAdapter();
        if(theatersAdapter != null)
        {
            theatersAdapter.clearList();
            theatersAdapter.notifyDataSetChanged();
        }
        movieAvailabilityViewModel.clear();
        binding = null;
    }
}