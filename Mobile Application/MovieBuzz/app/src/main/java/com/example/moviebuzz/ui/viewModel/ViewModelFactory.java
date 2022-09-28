package com.example.moviebuzz.ui.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviebuzz.ui.login.LoginViewModel;
import com.example.moviebuzz.ui.movie.MovieViewModel;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityViewModel;
import com.example.moviebuzz.ui.payment.PaymentViewModel;
import com.example.moviebuzz.ui.register.RegisterViewModel;
import com.example.moviebuzz.ui.search.SearchViewModel;
import com.example.moviebuzz.ui.transactionHistory.TransactionHistoryViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory{
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel();
        }
        else if(modelClass.isAssignableFrom(RegisterViewModel.class))
        {
            return (T) new RegisterViewModel();
        }
        else if(modelClass.isAssignableFrom(SearchViewModel.class))
        {
            return (T) new SearchViewModel();
        }
        else if(modelClass.isAssignableFrom(MovieViewModel.class))
        {
            return (T) new MovieViewModel();
        }
        else if(modelClass.isAssignableFrom(MovieAvailabilityViewModel.class))
        {
            return (T) new MovieAvailabilityViewModel();
        }
        else if(modelClass.isAssignableFrom(MainViewModel.class))
        {
            return (T) new MainViewModel();
        }
        else if(modelClass.isAssignableFrom(PaymentViewModel.class))
        {
            return (T) new PaymentViewModel();
        }
        else if(modelClass.isAssignableFrom(TransactionHistoryViewModel.class))
        {
            return (T) new TransactionHistoryViewModel();
        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
