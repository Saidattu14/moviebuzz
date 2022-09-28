package com.example.moviebuzz.ui.tickets;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.SearchMoviesResponse;
import com.example.moviebuzz.ui.movie.MovieViewModel;
import com.example.moviebuzz.ui.search.SearchViewModel;
import com.example.moviebuzz.ui.search.SearchedMovieData;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.example.moviebuzz.webSockets.WebSocketClass;
import com.example.moviebuzz.webSockets.WebSocketEcho;
import com.example.moviebuzz.adapters.MovieTicketsAdapter;
import com.example.moviebuzz.data.model.BookingRequestModel;
import com.example.moviebuzz.data.model.BookingResponseModel;
import com.example.moviebuzz.data.model.SeatsData;
import com.example.moviebuzz.data.model.TheatersAndTicketsModel;
import com.example.moviebuzz.databinding.FragmentMovieTicketsBinding;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityResult;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityViewModel;
import com.example.moviebuzz.ui.movieAvalablity.MovieTicketsResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.WebSocket;

@AndroidEntryPoint
public class MovieTicketsFragment extends Fragment {

    @Inject
    WebSocketClass webSocketClass;
    WebSocket webSocket;
    WebSocketEcho webSocketEcho;
    private MovieTicketsViewModel movieTicketsViewModel;
    private SearchViewModel searchViewModel;
    private FragmentMovieTicketsBinding binding;
    private MovieAvailabilityViewModel movieAvailabilityViewModel;
    private MainViewModel mainViewModel;
    private Seating seating = new Seating();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        webSocket = webSocketClass.getWebSocket();
        webSocketEcho = webSocketClass.getWebSocketEcho();
        movieTicketsViewModel = new ViewModelProvider(requireActivity()).get(MovieTicketsViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        movieAvailabilityViewModel = new ViewModelProvider(requireActivity()).get(MovieAvailabilityViewModel.class);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        webSocketEcho.setMovieTicketsViewModel(movieTicketsViewModel);
        binding = FragmentMovieTicketsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.loading.getVisibility() == View.INVISIBLE)
                {
                    binding.loading.setVisibility(View.VISIBLE);
                    binding.ticketsResult.setVisibility(View.INVISIBLE);
                    sendBookingRequestMessage();
                }
            }
        });
        movieAvailabilityViewModel.getTickets().observe(getViewLifecycleOwner(), new Observer<MovieTicketsResult>() {
            @Override
            public void onChanged(MovieTicketsResult movieTicketsResult) {
                List<SeatsData> seatsDataList = new ArrayList<>();
                movieTicketsResult.getSeats().forEach(seats -> seatsDataList.addAll(seats.getSeats().stream().collect(Collectors.toList())));
                movieTicketsResult.getSeats().forEach(seats -> seating.addRows(seats.getRow_id()));
                MovieTicketsAdapter movieTicketsAdapter = (MovieTicketsAdapter) binding.ticketsRecycleview.getAdapter();
                if(movieTicketsAdapter == null)
                {
                    movieTicketsAdapter = new MovieTicketsAdapter(seatsDataList, seating);
                    binding.ticketsRecycleview.setAdapter(movieTicketsAdapter);
                }
            }
        });
        movieTicketsViewModel.getBookingResult().observe(getViewLifecycleOwner(), new Observer<MovieBookingResult>() {
            @Override
            public void onChanged(MovieBookingResult movieBookingResult) {
                BookingResponseModel bookingResponseModel = movieBookingResult.getBookingResponseModel();
                //System.out.println(bookingResponseModel);
                String success = "Success";
                System.out.println(bookingResponseModel);
                System.out.println(movieBookingResult);
                if(bookingResponseModel != null  && movieBookingResult != null) {
                    try {
                        if (bookingResponseModel.getStatus().equals(success) && !movieBookingResult.isBackPressed()) {
                            navigateToPayment();
                        } else if (!movieBookingResult.isBackPressed()) {
                            binding.ticketsResult.setVisibility(View.VISIBLE);
                            binding.loading.setVisibility(View.INVISIBLE);
                            binding.ticketsResult.setText("Sorry Selected Tickets are Unavailable.");
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println(binding);
                    }

                }
            }
        });
    }

    private void sendBookingRequestMessage()
    {
        String show_id = getShow_id();
        MovieAvailabilityResult movieAvailabilityResult = movieAvailabilityViewModel.getCurrentMovieAvailability().getValue();
        TheatersAndTicketsModel theatersAndTicketsModel = movieAvailabilityResult.getSelectedTheater();
        SearchMoviesResponse currentSearchedMovieData = searchViewModel.getSearchedMovieData().getValue().getCurrentSearchedMovieData();
        BookingRequestModel bookingRequestModel = new BookingRequestModel(UUID.randomUUID(),
                "ValidateSelectedTickets",theatersAndTicketsModel.getCity(),theatersAndTicketsModel.getMovie_name(),
                theatersAndTicketsModel.getTheater_id(),show_id,seating.getList(),
                theatersAndTicketsModel.getState(),theatersAndTicketsModel.getCountry(),theatersAndTicketsModel.getTheater_name(),
                movieAvailabilityViewModel.getTimeDate().getValue().getDate(),
                UUID.randomUUID().toString(),theatersAndTicketsModel.getMovie_id(),currentSearchedMovieData.get_source().getPoster(),
                mainViewModel.getToken(),
                mainViewModel.getUserId());
        Gson gson = new Gson();
        String json = gson.toJson(bookingRequestModel, BookingRequestModel.class);
        movieTicketsViewModel.setBookingRequest(bookingRequestModel);
        webSocket.send(json.toString());
    }

    private String getShow_id()
    {
        String time = movieAvailabilityViewModel.getTimeDate().getValue().getTime();
        if(time.equals("10:00 A.M"))
        {
            return "0";
        }
        else if(time.equals("01:00 P.M"))
        {
            return "1";
        }
        else if(time.equals("03:30 P.M"))
        {
            return "2";
        }
        else if(time.equals("06:00 P.M"))
        {
            return "3";
        }
        return "0";
    }
    private void navigateToPayment()
    {
        NavHostFragment.findNavController(MovieTicketsFragment.this)
                .navigate(R.id.action_movieTicketsFragment_to_paymentFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //movieAvailabilityViewModel.clear();
        movieTicketsViewModel.clearDisposables();
        binding = null;
    }
}