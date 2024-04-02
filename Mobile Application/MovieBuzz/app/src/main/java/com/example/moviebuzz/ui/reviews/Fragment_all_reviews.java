package com.example.moviebuzz.ui.reviews;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.transition.Scene;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviebuzz.R;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.example.moviebuzz.webSockets.WebSocketClass;
import com.example.moviebuzz.webSockets.WebSocketEcho;
import com.example.moviebuzz.adapters.MovieAllReviewsAdapter;
import com.example.moviebuzz.data.model.MovieReviewsRequestModel;
import com.example.moviebuzz.data.model.SearchMoviesResponse;
import com.example.moviebuzz.databinding.FragmentAllReviewsBinding;
import com.example.moviebuzz.ui.movie.MovieViewModel;
import com.example.moviebuzz.ui.search.SearchViewModel;
import com.google.gson.Gson;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.WebSocket;

@AndroidEntryPoint
public class Fragment_all_reviews extends Fragment {

    private FragmentAllReviewsViewModel allReviewsViewModel;
    private FragmentAllReviewsBinding binding;
    private MovieViewModel movieViewModel;
    private SearchViewModel searchViewModel;
    private Scene scene;
    private Scene anotherScene;
    private ViewGroup rootView;
    private ConstraintLayout constraintLayout1;
    private AtomicBoolean enterTransitionStarted = new AtomicBoolean();
    @Inject
    WebSocketClass webSocketClass;
    private WebSocket webSocket;
    private WebSocketEcho webSocketEcho;
    private MainViewModel mainViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding =  FragmentAllReviewsBinding.inflate(inflater, container, false);
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mainViewModel =  new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        allReviewsViewModel =  new ViewModelProvider(requireActivity()).get(FragmentAllReviewsViewModel.class);
        webSocket = webSocketClass.getWebSocket();
        webSocketEcho = webSocketClass.getWebSocketEcho();
        webSocketEcho.setAllReviewsViewModel(allReviewsViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchViewModel.getSearchedMovieData().observe(getViewLifecycleOwner(), searchedMovieData -> {
            getReviewsData(searchedMovieData.getCurrentSearchedMovieData());
        });

        binding.topAppBar4.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.drawable.ic_baseline_arrow_back_ios_24) {
                    NavHostFragment.findNavController(Fragment_all_reviews.this)
                            .popBackStack();
                    return true;
                }
                return false;
            }
        });

        binding.topAppBar4.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Fragment_all_reviews.this)
                        .popBackStack();
            }
        });

        binding.floatingActionButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(Fragment_all_reviews.this).navigate(R.id.action_fragment_all_reviews_to_addReview);
        });

        allReviewsViewModel.getLiveMovieReviews().observe(getViewLifecycleOwner(), new Observer<MovieReviewsResultData>() {
            @Override
            public void onChanged(MovieReviewsResultData movieReviewsResultData) {
                if(movieReviewsResultData != null) {
                    if (movieReviewsResultData.getError() == null) {
                        String[] userName = mainViewModel.getUserEmail().split("@");
                        MovieAllReviewsAdapter movieAllReviewsAdapter = new MovieAllReviewsAdapter(movieReviewsResultData.getMovieReviewsData(),allReviewsViewModel,mainViewModel.getUserId().toString(),mainViewModel.getToken(),userName[0]);
                        binding.allReviewsRecycle.setAdapter(movieAllReviewsAdapter);
                        binding.loading.setVisibility(View.INVISIBLE);
                        binding.floatingActionButton.setVisibility(View.VISIBLE);
                    } else {
                        binding.reviewsResult.setText("Sorry Try again Later");
                        binding.loading.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void sendMsg(String movieId){
        MovieReviewsRequestModel movieReviewsRequestModel = new MovieReviewsRequestModel(
                UUID.randomUUID(),
                "GetReviews",
                movieId,
                mainViewModel.getToken());
        Gson gson = new Gson();
        String json = gson.toJson(movieReviewsRequestModel, MovieReviewsRequestModel.class);
        webSocket.send(json.toString());
    }

    private void getReviewsData(SearchMoviesResponse searchMoviesResponse)
    {
        try {
            String movieId = movieId = allReviewsViewModel.getLiveMovieReviews().getValue().getMovieId();
            if(!searchMoviesResponse.get_source().getImdbID().equals(movieId))
            {
                searchMoviesResponse.get_source().getImdbID();
                allReviewsViewModel.setLiveMovieReviews();
            }
        }
        catch (Exception e)
        {
            binding.loading.setVisibility(View.VISIBLE);
            sendMsg(searchMoviesResponse.get_source().getImdbID());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        allReviewsViewModel.clearDisposables();
    }
}