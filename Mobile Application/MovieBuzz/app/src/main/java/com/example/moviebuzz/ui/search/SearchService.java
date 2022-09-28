package com.example.moviebuzz.ui.search;

import android.content.Context;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.Navigation;

import com.example.moviebuzz.R;
import com.example.moviebuzz.adapters.AutoSearchAdapter;
import com.example.moviebuzz.adapters.SearchDataMoviesAdapter;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.data.model.SearchMoviesResponse;
import com.example.moviebuzz.databinding.FragmentSearchBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchService {
    SearchViewModel searchViewModel;
    SearchFragment searchFragment;
    FragmentSearchBinding binding;


    public SearchService(SearchViewModel searchViewModel, SearchFragment searchFragment, FragmentSearchBinding binding) {
        this.searchViewModel = searchViewModel;
        this.searchFragment = searchFragment;
        this.binding = binding;
    }

    public String getCountry(Location location) {
        try {
            Geocoder gd = new Geocoder(binding.getRoot().getContext(), Locale.getDefault());
            List<Address> addressList = gd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            return addressList.get(0).getCountryName();
        } catch (IOException ioException) {
            System.out.println(ioException);
            return null;
        }
    }

    public void setUpSearchViewData() {
        Menu menu = (Menu) binding.topAppBar.getMenu();
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        String search_hint = searchFragment.getString(R.string.search_hint);
        searchView.setQueryHint(search_hint);
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchText = searchView.findViewById(id);
        searchText.setTextSize(13);
        searchText.setTextColor(Color.BLACK);
        searchText.setHintTextColor(Color.BLACK);
    }


    public SearchView.OnCloseListener getOnCloseListener() {
        SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                AutoSearchAdapter autoSearchAdapter = (AutoSearchAdapter) binding.autoComplete.getAdapter();
                if (autoSearchAdapter != null) {
                    autoSearchAdapter.clearSearchList();
                }
                return false;
            }
        };
        return onCloseListener;
    }


    public SearchView.OnQueryTextListener getOnQueryTextListener() {
        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFragment.setApiValuesWildSearch(query);
                binding.topAppBar.collapseActionView();
                View view = (View) binding.getRoot().getFocusedChild();
                if (view != null) {
                    InputMethodManager manager = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
        return onQueryTextListener;
    }


    public SearchView.OnAttachStateChangeListener getOnAttachStateChangeListener() {
        SearchView.OnAttachStateChangeListener onAttachStateChangeListener = new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                AutoSearchAdapter autoSearchAdapter = (AutoSearchAdapter) binding.autoComplete.getAdapter();
                autoSearchAdapter.clearSearchList();
                autoSearchAdapter.notifyDataSetChanged();
            }
        };
        return onAttachStateChangeListener;
    }

    public void setAutoSearchAdapter(SearchView searchView) {
        List<String> initialSearchList = new ArrayList<>();
        initialSearchList.add("Top Popular Movies");
        initialSearchList.add("Top Rated Movies");
        initialSearchList.add("Top Movies in Comedy");
        initialSearchList.add("Top Movies in Action");
        AutoSearchAdapter autoSearchAdapter = (AutoSearchAdapter) binding.autoComplete.getAdapter();
        if (autoSearchAdapter == null) {
            autoSearchAdapter = new AutoSearchAdapter(initialSearchList, searchView, searchViewModel, binding, searchFragment);
            binding.autoComplete.setAdapter(autoSearchAdapter);
        } else {
            autoSearchAdapter.updateSearchList(initialSearchList);
            autoSearchAdapter.notifyDataSetChanged();
        }
    }

    public void setSearchedMoviesListAdapter(List<SearchMoviesResponse> searchMoviesResponseList) {
        SearchDataMoviesAdapter searchDataMoviesAdapter = (SearchDataMoviesAdapter) binding.moviesList.getAdapter();
        if (searchDataMoviesAdapter == null) {
            searchDataMoviesAdapter = new SearchDataMoviesAdapter(searchMoviesResponseList, searchViewModel, searchFragment);
            binding.moviesList.setAdapter(searchDataMoviesAdapter);
            //binding.moviesList.addOnScrollListener(onScrollListenerFunction());
        } else {
            searchDataMoviesAdapter.update(searchMoviesResponseList);
            searchDataMoviesAdapter.notifyDataSetChanged();
        }
    }

    public void setNavigationDrawer() {
        int count = binding.navigationView.getHeaderCount();
        System.out.println(count);
        for(int i=0; i<count; i++) {
            View view = (View) binding.navigationView.getHeaderView(i);
            String userEmail = searchFragment.getEmail();
            String[] userName = userEmail.split("@");
            TextView textView1 = (TextView) view.findViewById(R.id.header_title1);
            TextView textView2 = (TextView) view.findViewById(R.id.header_title2);
            if(textView1 != null)
            {
                textView1.setText(userName[0]);
            }
            if(textView2 != null)
            {
                textView2.setText(userEmail);
            }
        }
    }

    public NavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener()
    {
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        searchFragment.sendRequestHistoryDetailsMessage();
                        searchFragment.navigationToTransactionHistory();
                        return true;
                    case R.id.item2:
                        return true;
                    case R.id.item3:
                        return true;
                    case R.id.item4:
                        return true;
                    default:
                        return false;
                }
            }
        };
        return  navigationItemSelectedListener;
    }


    public Toolbar.OnMenuItemClickListener getOnMenuItemClickListener()
    {
        Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                        searchView.onActionViewExpanded();
                        setAutoSearchAdapter(searchView);
                        searchView.addOnAttachStateChangeListener(getOnAttachStateChangeListener());
                        searchView.setOnQueryTextListener(getOnQueryTextListener());
                        searchView.setOnCloseListener(getOnCloseListener());
                        return true;
                    default:
                        return false;
                }
            }
        };
        return  onMenuItemClickListener;
    }
}
