package com.example.moviebuzz.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.MovieRepository;
import com.example.moviebuzz.data.model.MovieReviewsResponseBody;
import com.example.moviebuzz.data.model.SearchMoviesResponse;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentAllReviewsViewModel extends ViewModel {

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

    public void setLiveMovieReviews()
    {
        movieReviewsResultDataLiveData.setValue(new MovieReviewsResultData(null,new ArrayList<>(),null));
    }

    public void setMovieReviewsResultDataLiveData(MovieReviewsResponseBody movieReviewsResponseBody)
    {
        disposables.add(reviewsResponseBodyObservable(movieReviewsResponseBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MovieReviewsResponseBody>(){
                    MovieReviewsResponseBody movieReviewsResponseBody;
                    String error;
                    @Override
                    public void onNext(MovieReviewsResponseBody movieReviewsResponseBody) {
                        this.movieReviewsResponseBody = movieReviewsResponseBody;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.toString();
                    }
                    @Override
                    public void onComplete() {
                        movieReviewsResultDataLiveData.setValue(new MovieReviewsResultData(this.error,this.movieReviewsResponseBody.getMovieReviewsModelList(),this.movieReviewsResponseBody.getMovieId()));
                    }
                }));
    }
    static Observable<MovieReviewsResponseBody> reviewsResponseBodyObservable(MovieReviewsResponseBody movieReviewsResponseBody)
    {
        return Observable.defer(() -> {
            return Observable.just(movieReviewsResponseBody);
        });
    }
}