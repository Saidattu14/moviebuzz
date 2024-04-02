package com.example.moviebuzz.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.ReviewRepository;
import com.example.moviebuzz.data.model.MovieReviewsResponseBody;
import com.example.moviebuzz.data.model.ReviewDeleteRequestBody;
import com.example.moviebuzz.data.model.ReviewLikedDisLikedRequestBody;


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



    public void reviewLikedDisLikedApiRequest(ReviewLikedDisLikedRequestBody reviewLikedDisLikedRequestBody,String token)
    {
        disposables.add(getLikedOrDisLikedApiResponse(reviewLikedDisLikedRequestBody,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    static Observable<String> getLikedOrDisLikedApiResponse(ReviewLikedDisLikedRequestBody reviewLikedDisLikedRequestBody,String token)
    {
        return Observable.defer(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/192.168.43.99:8005/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ReviewRepository reviewRepository = retrofit.create(ReviewRepository.class);
            Call<String> call = reviewRepository.likedOrDisLikedReviewApiCall(token, reviewLikedDisLikedRequestBody);
            retrofit2.Response<String> stringResponse = call.execute();
            if(stringResponse.code() != 200)
            {
                Throwable throwable = new Error(stringResponse.errorBody().string());
                return Observable.error(throwable);
            }
            return Observable.just(stringResponse.body());
        });
    }



    public void reviewDeleteApiRequest(ReviewDeleteRequestBody reviewDeleteRequestBody, String token)
    {
        disposables.add(getReviewDeleteResponse(reviewDeleteRequestBody,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                      System.out.println(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    static Observable<String> getReviewDeleteResponse(ReviewDeleteRequestBody reviewDeleteRequestBody,String token)
    {
        return Observable.defer(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/192.168.43.99:8005/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ReviewRepository reviewRepository = retrofit.create(ReviewRepository.class);
            Call<String> call = reviewRepository.deleteReviewApiCall(token, reviewDeleteRequestBody);
            retrofit2.Response<String> stringResponse = call.execute();
            if(stringResponse.code() != 200)
            {
                Throwable throwable = new Error(stringResponse.errorBody().string());
                return Observable.error(throwable);
            }
            return Observable.just(stringResponse.body());
        });
    }


    public void clearDisposables()
    {
        this.disposables.clear();
        movieReviewsResultDataLiveData.postValue(null);
    }
}