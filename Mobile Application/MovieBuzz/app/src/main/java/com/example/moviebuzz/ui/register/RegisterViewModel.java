package com.example.moviebuzz.ui.register;

import android.graphics.Region;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.LoginRepository1;
import com.example.moviebuzz.data.RegisterRepository;
import com.example.moviebuzz.data.model.LoggedInUserRequestBody;
import com.example.moviebuzz.data.model.RegisterResponse;
import com.example.moviebuzz.data.model.RegisterUserRequestBody;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private final MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final RegisterFormState registerFormStateObj = new RegisterFormState();

    LiveData<RegisterFormState> getRegisterFormState () {
        return registerFormState;
    }
    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String email, String password, String password1)
    {
      registerAPIRequest(email,password,password1);
    }

    public RegisterFormState setUsernameError()
    {
        registerFormStateObj.setUsernameError(R.string.invalid_username);
        return registerFormStateObj;
    }

    public RegisterFormState setPasswordError()
    {
        registerFormStateObj.setPasswordError(R.string.invalid_password);
        return registerFormStateObj;
    }

    public RegisterFormState setPasswordUnMatchError()
    {
        registerFormStateObj.setPasswordUnMatchError(R.string.password_unmatch);
        return registerFormStateObj;
    }

    public RegisterFormState setClearErrors()
    {
        registerFormStateObj.clearErrors();
        return registerFormStateObj;
    }

    public void registerDataChanged(String username, String password, String password1) {

        if (!isUserNameValid(username)) {
            System.out.println(isUserNameValid(username));
            registerFormState.setValue(setUsernameError());
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(setPasswordError());
        }
        else if (!password.equals(password1))
        {
            registerFormStateObj.setPasswordError(null);
            registerFormState.setValue(setPasswordUnMatchError());
        }
        else {

            registerFormState.setValue(setClearErrors());
        }

    }

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

    private void registerAPIRequest(String username, String password, String password1)
    {
        disposables.add(getRegisterResponse(username,password,password1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RegisterResponse>(){
                    RegisterResponse registerResponse;
                    String error;
                    @Override
                    public void onNext( RegisterResponse registerResponse) {
                        this.registerResponse = registerResponse;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.getMessage();
                        registerResult.setValue(new RegisterResult(this.error,this.registerResponse));
                    }
                    @Override
                    public void onComplete() {
                        registerResult.setValue(new RegisterResult(this.error,this.registerResponse));
                    }
                }
                ));
    }

    static Observable<RegisterResponse> getRegisterResponse(String username, String password, String password1)
    {
        return Observable.defer(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/192.168.43.99:8005/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RegisterRepository registerRepository = retrofit.create(RegisterRepository.class);
            Call<RegisterResponse> call = registerRepository.registerUser(new RegisterUserRequestBody(username,password,password1));
            retrofit2.Response<RegisterResponse>stringResponse = call.execute();
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
        disposables.clear();
        registerResult.postValue(null);
        registerFormState.postValue(null);
    }
}