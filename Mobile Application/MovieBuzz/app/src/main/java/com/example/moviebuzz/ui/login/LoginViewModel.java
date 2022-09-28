package com.example.moviebuzz.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.moviebuzz.data.LoginRepository1;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.LoggedInUserRequestBody;
import com.example.moviebuzz.data.model.LoginResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        loginApiRequest(username,password);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void loginApiRequest(String username, String password)
    {
        disposables.add(getLoginResponse(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginResponse>(){
                    LoginResponse loginResponse;
                    String error;
                    @Override
                    public void onNext(@NonNull  LoginResponse loginResponse) {
                        this.loginResponse = loginResponse;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.getMessage();
                        //System.out.println(this.error);
                        loginResult.setValue(new LoginResult(this.error, this.loginResponse));
                    }

                    @Override
                    public void onComplete() {
                        loginResult.setValue(new LoginResult(this.error, this.loginResponse));
                    }
                }
                ));
    }

    static Observable<LoginResponse> getLoginResponse(String username, String password)
    {
        return Observable.defer(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/192.168.43.99:8005/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            LoginRepository1 loginRepository1 = retrofit.create(LoginRepository1.class);
            Call<LoginResponse> call = loginRepository1.loginUser(new LoggedInUserRequestBody(username,password));
           // String obj = call.execute().body();
            retrofit2.Response<LoginResponse> stringResponse = call.execute();
            if(stringResponse.code() != 200)
            {
                Throwable throwable = new Error(stringResponse.errorBody().string());
                return Observable.error(throwable);
            }
            return Observable.just(stringResponse.body());
        });
    }

    public void clear()
    {
        loginFormState.postValue(null);
        loginResult.postValue(null);
        disposables.clear();
    }
}