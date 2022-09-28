package com.example.moviebuzz.ui.search;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Process;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.moviebuzz.MainActivity;
import com.example.moviebuzz.R;
import com.example.moviebuzz.adapters.AutoSearchAdapter;
import com.example.moviebuzz.adapters.SearchDataMoviesAdapter;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.data.model.BookingHistoryRequestModel;
import com.example.moviebuzz.data.model.PaymentRequestModel;
import com.example.moviebuzz.data.model.SearchMoviesResponse;
import com.example.moviebuzz.databinding.AutosearchBinding;
import com.example.moviebuzz.databinding.FragmentSearchBinding;
import com.example.moviebuzz.ui.movie.MovieFragment;
import com.example.moviebuzz.ui.register.RegisterFragment;
import com.example.moviebuzz.ui.transactionHistory.TransactionHistoryViewModel;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.example.moviebuzz.ui.viewModel.ViewModelFactory;
import com.example.moviebuzz.webSockets.WebSocketClass;
import com.example.moviebuzz.webSockets.WebSocketEcho;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.WebSocket;

@AndroidEntryPoint
public class SearchFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Inject
    WebSocketClass webSocketClass;
    private WebSocket webSocket;
    private WebSocketEcho webSocketEcho;
    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private AutosearchBinding autosearchBinding;
    private int scrollPosition = 0;
    private double latitude,longitude;
    private String country;
    private SearchService searchService;
    private SearchApiEnum searchApiEnumValue;
    private MainViewModel mainViewModel;
    private SearchScrollService searchScrollService;
    private boolean searchPrevResultsResponse = true;
    private SearchApiCallService searchApiCallService;
    private TransactionHistoryViewModel transactionHistoryViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        transactionHistoryViewModel = new ViewModelProvider(requireActivity()).get(TransactionHistoryViewModel.class);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        webSocket = webSocketClass.getWebSocket();
        webSocketEcho = webSocketClass.getWebSocketEcho();
        webSocketEcho.setTransactionHistoryViewModel(transactionHistoryViewModel);
        searchService = new SearchService(searchViewModel,this,binding);
        searchApiCallService = new SearchApiCallService(searchViewModel,this);
        searchScrollService = new SearchScrollService(searchViewModel,this,binding,searchApiCallService);
        searchApiEnumValue = mainViewModel.getApiEnum();
        checkLocationPermissions();
        searchService.setUpSearchViewData();
        searchService.setNavigationDrawer();
        requestLocationPermission();
        backButtonPressed();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.topAppBar.setOnMenuItemClickListener(searchService.getOnMenuItemClickListener());
        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerLayout.openDrawer(Gravity.START));
        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<SearchResult>() {
            @Override
            public void onChanged(SearchResult searchResult) {
                List<SearchMoviesResponse> searchMoviesResponseList = searchResult.getSearchMoviesResponseList();
                if(searchResult.getError() != null)
                {
                }
                else if(searchMoviesResponseList.size() == 0 || searchMoviesResponseList==null)
                {

                    searchPrevResultsResponse = false;
                }
                else
                {
                    searchPrevResultsResponse = true;
                  binding.loading.setVisibility(View.INVISIBLE);
                  searchService.setSearchedMoviesListAdapter(searchMoviesResponseList);
                  binding.moviesList.setOnScrollListener(searchScrollService.onScrollListenerFunction());
                }
            }
        });
        binding.navigationView.setNavigationItemSelectedListener(searchService.getOnNavigationItemSelectedListener());
    }

    private void checkLocationPermissions()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (getContext().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
            checkGpsIsOnorOff();
            try {
                fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancelLocationRequest())
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                try {
                                    Geocoder gd = new Geocoder(getContext(), Locale.getDefault());
                                    List<Address> addressList = gd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    country =  addressList.get(0).getCountryName();
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    if(searchApiEnumValue == null)
                                    {
                                        setSearchApiEnumValueToLocation();
                                        searchApiCallService.searchWithLocationBased();
                                    }
                                }
                                catch (Exception ignored)
                                {
                                    System.out.println(ignored);
                                }
                            }
                        });
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else
        {
            setApiValuesDefault(SearchApiEnum.Popular_Movies_Search);
            searchApiCallService.searchWithOutLocationBased();
        }
    }

    public void setSearchApiEnumValueToLocation()
    {
        mainViewModel.setApiEnum(SearchApiEnum.Location_Based_Search);
    }

    public String getToken()
    {
        return mainViewModel.getToken();
    }

    public int getScrollPosition()
    {
        return scrollPosition;
    }

    public boolean isSearchPrevResultsResponse() {
        return searchPrevResultsResponse;
    }

    public void incrementScrollCount()
    {
        scrollPosition = scrollPosition + 10;
    }

    public SearchApiEnum getSearchApiEnumValue()
    {
        return mainViewModel.getApiEnum();
    }

    public void resetScrollPosition()
    {
        scrollPosition = 0;
    }

    public String getGenerType()
    {
        return mainViewModel.getCurrentGenerType();
    }
    public String getSearchText()
    {
        return mainViewModel.getSearchText();
    }

    public String getCountry()
    {
        return this.country;
    }
    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getEmail()
    {
        return mainViewModel.getUserEmail();
    }

    public void clearCurrentSearchResult()
    {
        SearchDataMoviesAdapter searchDataMoviesAdapter = (SearchDataMoviesAdapter) binding.moviesList.getAdapter();
        if(searchDataMoviesAdapter != null)
        {
            searchDataMoviesAdapter.clearList();
            searchDataMoviesAdapter.notifyDataSetChanged();
            searchViewModel.clearPreviousSearchData();
        }
    }

    public void setApiValuesWildSearch(String query)
    {
       
        mainViewModel.setCurrentGenerType(null);
        mainViewModel.setApiEnum(SearchApiEnum.Other);
        mainViewModel.setSearchText(query);
        resetScrollPosition();
        clearCurrentSearchResult();
        searchApiCallService.searchWildCard();
    }

    public void callApiService()
    {
        searchApiCallService.searchWithOutLocationBased();
    }
    public void setApiValuesGenreBasedSearch(SearchApiEnum searchApiEnumValue, String generType)
    {
        mainViewModel.setCurrentGenerType(generType);
        mainViewModel.setApiEnum(searchApiEnumValue);
    }

    public void setApiValuesDefault(SearchApiEnum searchApiEnumValue)
    {
        mainViewModel.setApiEnum(searchApiEnumValue);
    }
    public void navigationToMoviePage()
    {
        NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.action_searchFragment_to_movieFragment);
    }

    public void navigationToTransactionHistory()
    {
        NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.action_searchFragment_to_transactionHistoryFragment);
    }

    public void sendRequestHistoryDetailsMessage()
    {
         BookingHistoryRequestModel bookingHistoryRequestModel = new BookingHistoryRequestModel(
                 UUID.randomUUID(),
                 "GetBookingHistoryDetails",
                 getToken(),
                 mainViewModel.getUserId());
        Gson gson = new Gson();
        String json = gson.toJson(bookingHistoryRequestModel, BookingHistoryRequestModel.class);
        webSocket.send(json.toString());
    }

    private void backButtonPressed()
    {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
              searchViewModel.updateSearchResultBackPress();
//                if(getParentFragmentManager().getBackStackEntryCount() == 1)
//                {
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                    System.exit(0);
//                }
                NavHostFragment.findNavController(SearchFragment.this)
                        .popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void requestLocationPermission()
    {
      getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},99);
    }

    private void checkGpsIsOnorOff()
    {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
    }

    private CancellationToken cancelLocationRequest()
    {
       CancellationToken cancellationToken = new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        };
        return cancellationToken;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        searchService = null;
        searchScrollService = null;
        searchApiCallService = null;
    }
}