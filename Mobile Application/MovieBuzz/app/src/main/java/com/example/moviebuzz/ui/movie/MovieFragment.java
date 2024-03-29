package com.example.moviebuzz.ui.movie;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.adapters.CastListAdapter;
import com.example.moviebuzz.adapters.CountryListAdapter;
import com.example.moviebuzz.adapters.DirectorsListAdapter;
import com.example.moviebuzz.adapters.GenreListAdapter;
import com.example.moviebuzz.adapters.MovieReviewsAdapter;
import com.example.moviebuzz.adapters.WritersListAdapter;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.data.model.CountryData;
import com.example.moviebuzz.data.model.GenreMain;
import com.example.moviebuzz.data.model.MainData;
import com.example.moviebuzz.data.model.MovieReviewsModel;
import com.example.moviebuzz.data.model.SearchMoviesResponse;
import com.example.moviebuzz.databinding.FragmentMovieBinding;
import com.example.moviebuzz.ui.search.SearchViewModel;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieFragment extends Fragment {
    private MovieViewModel movieViewModel;
    private FragmentMovieBinding binding;
    private SearchViewModel searchViewModel;
    private MainViewModel mainViewModel;
    private String jwtToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        jwtToken = mainViewModel.getToken();
        backButtonPressed();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchViewModel.getSearchedMovieData().observe(getViewLifecycleOwner(), searchedMovieData -> {
            System.out.println(searchedMovieData.getCurrentSearchedMovieData().get_source());
            SearchMoviesResponse searchMoviesResponse = searchedMovieData.getCurrentSearchedMovieData();
            binding.movieTitle.setText(searchMoviesResponse.get_source().getTitle());
            binding.moviePlot1.setText(searchMoviesResponse.get_source().getPlot());
            binding.movieRuntime2.setText(searchMoviesResponse.get_source().getRuntime());
            binding.movieReleaseDate2.setText(searchMoviesResponse.get_source().getReleased());
            binding.movieLanguage2.setText(searchMoviesResponse.get_source().getLanguage());
            setUpCastRecycleView(searchMoviesResponse.get_source().getActors());
            setUpDirectorsRecycleView(searchMoviesResponse.get_source().getDirector());
            setUpWritersRecycleView(searchMoviesResponse.get_source().getWriter());
            setUpGenresRecycleView(searchMoviesResponse.get_source().getGenre());
            setUpCountryRecycleView(searchMoviesResponse.get_source().getCountry());
            setPoster(searchMoviesResponse.get_source().getPoster());
            setReviewsRecycleView(searchMoviesResponse.get_source().getReviews());
        });

        binding.movieReviewsSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MovieFragment.this).navigate(R.id.action_movieFragment_to_fragment_all_reviews);
            }
        });

        binding.textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               navigateToMovieAvailability();
            }
        });
    }

    private void setUpCastRecycleView(List<MainData> castList)
    {
       CastListAdapter adapter = (CastListAdapter) binding.movieCastRecyclerView.getAdapter();
       if(adapter == null)
       {
           adapter = new CastListAdapter(castList, this,searchViewModel);
           binding.movieCastRecyclerView.setAdapter(adapter);
       }
    }

    private void setUpDirectorsRecycleView(List<MainData> directorsList)
    {
        DirectorsListAdapter adapter = (DirectorsListAdapter) binding.movieDirectorsRecyclerView.getAdapter();
        if(adapter == null)
        {
            adapter = new DirectorsListAdapter(directorsList, this,searchViewModel);
            binding.movieDirectorsRecyclerView.setAdapter(adapter);
        }
    }

    private void setUpWritersRecycleView(List<MainData> writersList)
    {
        WritersListAdapter adapter = (WritersListAdapter) binding.movieWritersRecyclerView.getAdapter();
        if(adapter == null)
        {
            adapter = new WritersListAdapter(writersList,this,searchViewModel);
            binding.movieWritersRecyclerView.setAdapter(adapter);
        }
    }

    private void setUpGenresRecycleView(List<GenreMain> genreList)
    {
        GenreListAdapter adapter = (GenreListAdapter) binding.movieGenreRecyclerView.getAdapter();
        if(adapter == null)
        {
            adapter = new GenreListAdapter(genreList,this,searchViewModel);
            binding.movieGenreRecyclerView.setAdapter(adapter);
        }
    }

    private void setUpCountryRecycleView(List<CountryData> list)
    {
        CountryListAdapter adapter = (CountryListAdapter) binding.movieCountry2.getAdapter();
        if(adapter == null)
        {
            adapter = new CountryListAdapter(list,this,searchViewModel);
            binding.movieCountry2.setAdapter(adapter);
        }
    }

    private void navigateToMovieAvailability()
    {
        NavHostFragment.findNavController(MovieFragment.this)
                .navigate(R.id.action_movieFragment_to_movieAvailability);
    }

    private void clearPreviousData()
    {
        searchViewModel.clearPreviousSearchData();
        searchViewModel.setCurrentResult();
    }

    public void callApi(SearchApiEnum searchApiEnum,String name)
    {
        clearPreviousData();
        mainViewModel.setApiEnum(searchApiEnum);
        searchViewModel.actorsOtherMoviesSearch(jwtToken,name,0);
    }

    public void callApi1(SearchApiEnum searchApiEnum,String countryName)
    {
        double dummyValue = 1.1;
        clearPreviousData();
        mainViewModel.setSearchText(countryName);
        mainViewModel.setApiEnum(searchApiEnum);
        searchViewModel.moviesSearchAPIRequest(jwtToken,searchApiEnum,countryName,dummyValue,dummyValue,null,0);
    }

    public void callApi2(SearchApiEnum searchApiEnum,String genreType)
    {
        double dummyValue = 1.1;
        clearPreviousData();
        mainViewModel.setApiEnum(searchApiEnum);
        mainViewModel.setCurrentGenerType(genreType);
        searchViewModel.moviesSearchAPIRequest(jwtToken,searchApiEnum,null,dummyValue,dummyValue,genreType,0);
    }

    public void navigateToSearch()
    {
        NavHostFragment.findNavController(MovieFragment.this)
                .navigate(R.id.action_movieFragment_to_searchFragment);
    }

    private void backButtonPressed()
    {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                searchViewModel.updateSearchResultBackPress1();
                NavHostFragment.findNavController(MovieFragment.this)
                        .popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }


    private void setPoster(String poster)
    {
        ImageView imageView = binding.movieposter;
        Picasso.get().load(poster)
                .into(imageView);
    }

    private void setReviewsRecycleView( List<MovieReviewsModel> movieReviewsModelList)
    {
        if(movieReviewsModelList != null)
        {

            MovieReviewsAdapter movieReviewsAdapter =  new MovieReviewsAdapter(movieReviewsModelList,movieViewModel,jwtToken);
            binding.movieReviewsRecyclerView.setAdapter(movieReviewsAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieViewModel.clearDisposables();
        binding = null;
    }
}