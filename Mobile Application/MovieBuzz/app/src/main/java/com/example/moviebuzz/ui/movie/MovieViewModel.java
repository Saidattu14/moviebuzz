package com.example.moviebuzz.ui.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.MovieRepository;
import com.example.moviebuzz.data.model.MovieReviewsResponseBody;
import com.example.moviebuzz.data.model.SearchMoviesResponse;
import com.example.moviebuzz.ui.reviews.MovieReviewsResultData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    public void clearDisposables()
    {
        this.disposables.clear();
    }

    private final MutableLiveData<MovieReviewsResultData> movieReviewsResultDataLiveData= new MutableLiveData<>();

    public LiveData<MovieReviewsResultData> getLiveMovieReviews()
    {
        return movieReviewsResultDataLiveData;
    }
}