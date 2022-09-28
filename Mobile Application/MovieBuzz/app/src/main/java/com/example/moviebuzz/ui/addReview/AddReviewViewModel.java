package com.example.moviebuzz.ui.addReview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.AddReviewRepository;
import com.example.moviebuzz.data.RegisterRepository;
import com.example.moviebuzz.data.model.AddReviewRequestBody;
import com.example.moviebuzz.data.model.MovieReviewsResponseBody;
import com.example.moviebuzz.data.model.RegisterUserRequestBody;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityResult;
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

public class AddReviewViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<AddReviewResult> addReviewResultMutableLiveData = new MutableLiveData<>();
    public void clearDisposables()
    {
        disposables.clear();
    }
    public LiveData<AddReviewResult> getReviewResult()
    {

        return addReviewResultMutableLiveData;
    }
    public void addReview(AddReviewRequestBody addReviewRequestBody, String jwtToken)
    {
        disposables.add(addReviewsObservable(addReviewRequestBody,jwtToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>(){
                    String response;
                    String error;
                    @Override
                    public void onNext(String response) {
                       this.response = response;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.getMessage();
                        addReviewResultMutableLiveData.setValue(new AddReviewResult(this.response,this.error));
                    }
                    @Override
                    public void onComplete() {
                        addReviewResultMutableLiveData.setValue(new AddReviewResult(this.response,this.error));
                    }
                }));
    }
    static Observable<String> addReviewsObservable(AddReviewRequestBody addReviewRequestBody,String jwtToken)
    {
        return Observable.defer(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/192.168.43.99:8005/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AddReviewRepository addReviewRepository = retrofit.create(AddReviewRepository.class);
            Call<String> call = addReviewRepository.addReviewApiCall(jwtToken,addReviewRequestBody);
            retrofit2.Response<String>stringResponse = call.execute();
            if(stringResponse.code() == 400 || stringResponse.code() == 500 || stringResponse.code() == 404)
            {
                Throwable throwable = new Error(stringResponse.errorBody().string());
                return Observable.error(throwable);
            }
            return Observable.just(stringResponse.body());
        });
    }
}