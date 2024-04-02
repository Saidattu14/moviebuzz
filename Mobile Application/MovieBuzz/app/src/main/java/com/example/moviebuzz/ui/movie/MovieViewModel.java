package com.example.moviebuzz.ui.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.ui.reviews.MovieReviewsResultData;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

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
//        return Observable.defer(() -> {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("http:/192.168.43.99:8005/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            ReviewRepository addReviewRepository = retrofit.create(ReviewRepository.class);
//
//            Call<String> call = addReviewRepository.like_or_dislike_ReviewApiCall("ks",new ReviewLikedDisLikedRequestBody(isLiked,isDisLiked,reviewId,""));
//
//            retrofit2.Response<String> stringResponse = call.execute();
//            if(stringResponse.code() != 200)
//            {
//                Throwable throwable = new Error(stringResponse.errorBody().string());
//                return Observable.error(throwable);
//            }
//            return Observable.just(stringResponse.body());
//        });

        return Observable.just("d");
    }


}