package com.example.moviebuzz.ui.movieAvalablity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.model.Seats;
import com.example.moviebuzz.data.model.TheatersAndTicketsModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieAvailabilityViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MovieAvailablityTimeDate objMovieAvailablityTimeDate = new MovieAvailablityTimeDate();
    private final MutableLiveData<MovieAvailabilityResult> movieAvailabilityResultData = new MutableLiveData<>();
    private final MutableLiveData<MovieTicketsResult> movieTicketsResultData = new MutableLiveData<>();
    private final MutableLiveData<MovieAvailablityTimeDate> movieAvailabilityTimeData = new MutableLiveData<>();

   public LiveData<MovieAvailabilityResult> getCurrentMovieAvailability()
    {
        return movieAvailabilityResultData;
    }

  public LiveData<MovieTicketsResult> getTickets()
  {
        return movieTicketsResultData;
  }



    public LiveData<MovieAvailablityTimeDate> getTimeDate()
    {
        return movieAvailabilityTimeData;
    }

    public void updateMovieAvailabilityResult(List<TheatersAndTicketsModel> theatersAndTicketsModelList)
    {

        disposables.add(updateData(theatersAndTicketsModelList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<TheatersAndTicketsModel>>(){
                    List<TheatersAndTicketsModel> theatersAndTicketsModelList;
                    String error;
                    boolean showData = true;
                    @Override
                    public void onNext(List<TheatersAndTicketsModel> theatersAndTicketsModelList) {
                        this.theatersAndTicketsModelList = theatersAndTicketsModelList;
                        if(theatersAndTicketsModelList.size() == 0)
                        {
                            this.showData = false;
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.toString();
                    }
                    @Override
                    public void onComplete() {
                        movieAvailabilityResultData.setValue(new MovieAvailabilityResult(theatersAndTicketsModelList,null,showData));
                    }
                }));
    }

    static Observable<List<TheatersAndTicketsModel>> updateData(List<TheatersAndTicketsModel> list)
    {
        return Observable.defer(() -> {
            return Observable.just(list);
        });
    }

    public void updateSeatsData(List<Seats> seats)
    {
        disposables.add(updateSeats(seats)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Seats>>(){
                    List<Seats> seats;
                    String error;
                    @Override
                    public void onNext(List<Seats> seats) {
                        this.seats = seats;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.toString();
                    }
                    @Override
                    public void onComplete() {
                     movieTicketsResultData.setValue(new MovieTicketsResult(this.seats,this.error));
                    }
                }));
    }

    static Observable<List<Seats>> updateSeats(List<Seats> seats)
    {
        return Observable.defer(() -> {
            return Observable.just(seats);
        });
    }

    public void updateMovieDate(String date)
    {
        disposables.add(updateDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    String date;
                    String error;
                    @Override
                    public void onNext(String date) {
                        objMovieAvailablityTimeDate.setDate(date);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        objMovieAvailablityTimeDate.setError(e.toString());
                    }
                    @Override
                    public void onComplete() {
                        movieAvailabilityTimeData.setValue(objMovieAvailablityTimeDate);
                    }
                }));
    }

    static Observable<String> updateDate(String date)
    {
        return Observable.defer(() -> {
            return Observable.just(date);
        });
    }

    public void updateMovieTime(String time)
    {
        disposables.add(updateTime(time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>(){
                    String error;
                    @Override
                    public void onNext(String time) {
                        objMovieAvailablityTimeDate.setTime(time);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        objMovieAvailablityTimeDate.setError(e.toString());
                    }
                    @Override
                    public void onComplete() {
                        movieAvailabilityTimeData.setValue(objMovieAvailablityTimeDate);
                    }
                }));
    }

    static Observable<String> updateTime(String time)
    {
        return Observable.defer(() -> {
            return Observable.just(time);
        });
    }

    public void setSelectedTheaterData(TheatersAndTicketsModel theatersAndTicketsModel)
    {
        MovieAvailabilityResult movieAvailabilityResult = movieAvailabilityResultData.getValue();
        movieAvailabilityResult.setSelectedTheater(theatersAndTicketsModel);
        movieAvailabilityResultData.setValue(movieAvailabilityResult);
    }

    public void clear()
    {
        disposables.clear();
        MovieAvailabilityResult movieAvailabilityResult = movieAvailabilityResultData.getValue();
        if(movieAvailabilityResult != null) {
            movieAvailabilityResult.setShowData(true);
            movieAvailabilityResultData.setValue(movieAvailabilityResult);
        }
    }
}