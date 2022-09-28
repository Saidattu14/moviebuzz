package com.example.moviebuzz.ui.tickets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.moviebuzz.data.model.BookingRequestModel;
import com.example.moviebuzz.data.model.BookingResponseModel;
import com.example.moviebuzz.data.model.TheatersAndTicketsModel;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityResult;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieTicketsViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<MovieBookingResult> movieBookingResultData = new MutableLiveData<>();
    private final MutableLiveData<MovieBookingRequestResult> movieBookingRequestResultData = new MutableLiveData<>();
    public LiveData<MovieBookingResult> getBookingResult()
    {
        return movieBookingResultData;
    }

    public LiveData<MovieBookingRequestResult> getBookingRequestData ()
    {
        return movieBookingRequestResultData;
    }

    public void setBookingRequest(BookingRequestModel bookingRequestModel)
    {
        disposables.add(updateBookingRequest(bookingRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BookingRequestModel>(){
                    BookingRequestModel bookingRequestModel;
                    String error;
                    @Override
                    public void onNext(BookingRequestModel bookingRequestModel) {
                        this.bookingRequestModel = bookingRequestModel;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.toString();
                    }
                    @Override
                    public void onComplete() {
                        movieBookingRequestResultData.setValue(new MovieBookingRequestResult(bookingRequestModel,error));
                    }
                }));
    }

    static Observable<BookingRequestModel> updateBookingRequest(BookingRequestModel bookingRequestModel)
    {
        return Observable.defer(() -> {
            return Observable.just(bookingRequestModel);
        });
    }



    public void setBookingStatus(BookingResponseModel bookingResponseModel)
    {
        disposables.add(updateBookingStatus(bookingResponseModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BookingResponseModel>(){
                    BookingResponseModel bookingResponseModel;
                    String error;
                    @Override
                    public void onNext(BookingResponseModel bookingResponseModel) {
                        this.bookingResponseModel = bookingResponseModel;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.toString();
                    }
                    @Override
                    public void onComplete() {
                        movieBookingResultData.setValue(new MovieBookingResult(bookingResponseModel,error,false));
                    }
                }));
    }
    static Observable<BookingResponseModel> updateBookingStatus(BookingResponseModel bookingResponseModel)
    {
        return Observable.defer(() -> {
            return Observable.just(bookingResponseModel);
        });
    }


    public void clearDisposables()
    {
        this.disposables.clear();
        MovieBookingResult movieBookingResult = getBookingResult().getValue();
        if(movieBookingResult != null)
        {
            movieBookingResult.setBackPressed(true);
            movieBookingResultData.setValue(movieBookingResult);
        }
    }

}