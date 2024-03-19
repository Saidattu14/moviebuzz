package com.example.moviebuzz.ui.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.AddReviewRepository;
import com.example.moviebuzz.data.LoginRepository1;
import com.example.moviebuzz.data.MovieRepository;
import com.example.moviebuzz.data.model.LoggedInUserRequestBody;
import com.example.moviebuzz.data.model.LoginResponse;
import com.example.moviebuzz.data.model.MovieReviewsResponseBody;
import com.example.moviebuzz.data.model.ReviewLikedRequestBody;
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



    private void loginApiRequest(String reviewId,boolean isLiked,boolean isDisLiked)
    {
//        disposables.add(getLoginResponse(username,password)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<LoginResponse>(){
//                                   LoginResponse loginResponse;
//                                   String error;
//                                   @Override
//                                   public void onNext(@NonNull  LoginResponse loginResponse) {
//                                       this.loginResponse = loginResponse;
//                                   }
//                                   @Override
//                                   public void onError(@NonNull Throwable e) {
//                                       this.error = e.getMessage();
//                                       //System.out.println(this.error);
//
//                                   }
//
//                                   @Override
//                                   public void onComplete() {
//
//                                   }
//                               }
//                ));
    }

    static Observable<String> getLoginResponse(String reviewId,boolean isLiked,boolean isDisLiked)
    {
        return Observable.defer(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/192.168.43.99:8005/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AddReviewRepository addReviewRepository = retrofit.create(AddReviewRepository.class);

            Call<String> call = addReviewRepository.like_or_dislike_ReviewApiCall("ks",new ReviewLikedRequestBody(isLiked,isDisLiked,reviewId));

            retrofit2.Response<String> stringResponse = call.execute();
            if(stringResponse.code() != 200)
            {
                Throwable throwable = new Error(stringResponse.errorBody().string());
                return Observable.error(throwable);
            }
            return Observable.just(stringResponse.body());
        });
    }


}