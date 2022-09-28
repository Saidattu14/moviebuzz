package com.example.moviebuzz.ui.transactionHistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.model.BookingHistoryResponseModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TransactionHistoryViewModel extends ViewModel {

    private final MutableLiveData<BookingHistoryResult> bookingHistoryResponseModelLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public LiveData<BookingHistoryResult> getBookingHistoryResult() {
        return bookingHistoryResponseModelLiveData;
    }

    public void setBookingHistoryResponseModelLiveDataResult(BookingHistoryResponseModel bookingHistoryResponseModel)
    {
        disposables.add(bookingHistoryResponseModelObservable(bookingHistoryResponseModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BookingHistoryResponseModel>(){
                    BookingHistoryResponseModel bookingHistoryResponseModel;
                    String error;
                    @Override
                    public void onNext(BookingHistoryResponseModel bookingHistoryResponseModel) {
                        this.bookingHistoryResponseModel = bookingHistoryResponseModel;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.getMessage();
                        bookingHistoryResponseModelLiveData.setValue(new BookingHistoryResult(this.error,this.bookingHistoryResponseModel));
                    }
                    @Override
                    public void onComplete() {
                        bookingHistoryResponseModelLiveData.setValue(new BookingHistoryResult(this.error,this.bookingHistoryResponseModel));
                    }
                }));
    }
    static Observable<BookingHistoryResponseModel> bookingHistoryResponseModelObservable(BookingHistoryResponseModel bookingHistoryResponseModel)
    {
        return Observable.defer(() -> {
            return Observable.just(bookingHistoryResponseModel);
        });
    }

    public void clear()
    {
        bookingHistoryResponseModelLiveData.postValue(null);
    }
}