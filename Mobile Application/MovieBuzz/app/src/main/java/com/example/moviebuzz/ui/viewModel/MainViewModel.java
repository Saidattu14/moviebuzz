package com.example.moviebuzz.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.enums.LocationRequestEnum;
import com.example.moviebuzz.data.enums.SearchApiEnum;

import java.io.Closeable;
import java.util.UUID;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<MainClass> mainClassMutableLiveData = new MutableLiveData<>();
    private final MainClass mainClass = new MainClass();
    public LiveData<MainClass> getMainClass()
    {
        return mainClassMutableLiveData;
    }

    public SearchApiEnum getApiEnum()
    {
        return mainClass.getCurrentAPI();
    }

    public String  getCurrentGenerType()
    {
        return mainClass.getGenerType();
    }

    public void setCurrentGenerType(String generType)
    {
        mainClass.setGenerType(generType);
    }

    public  void setApiEnum(SearchApiEnum searchApiEnum)
    {
        mainClass.setCurrentAPI(searchApiEnum);
    }

    public void setJwtToken(String jwtToken)
    {
        mainClass.setJwtToken(jwtToken);
    }

    public String getToken()
    {
       return mainClass.getJwtToken();
    }

    public String getSearchText()
    {
        return mainClass.getSearchText();
    }

    public void setSearchText(String query)
    {
        mainClass.setSearchText(query);
    }

    public void setUserEmail(String userEmail)
    {
        mainClass.setUserEmail(userEmail);
    }
    public String getUserEmail()
    {
        return  mainClass.getUserEmail();
    }


    public void setUserId(UUID userId)
    {
        mainClass.setUserId(userId);
    }
    public UUID getUserId()
    {
        return  mainClass.getUserId();
    }

    public LocationRequestEnum getIsLocationAccepted() {
        return mainClass.getIsLocationAccepted();
    }

    public void setIsLocationAccepted(LocationRequestEnum loc) {
        mainClass.setIsLocationAccepted(loc);
    }

    public CompositeDisposable getDisposables() {
        return disposables;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
