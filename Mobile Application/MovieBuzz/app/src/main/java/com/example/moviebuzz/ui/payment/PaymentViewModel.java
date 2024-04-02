package com.example.moviebuzz.ui.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.PaymentResponseModel;

import java.text.SimpleDateFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PaymentViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<PaymentFormState> paymentFormState = new MutableLiveData<>();
    private final MutableLiveData<PaymentResponeResult> paymentResponeResultData = new MutableLiveData<>();

    LiveData<PaymentFormState> getPaymentFormState() {
        return paymentFormState;
    }

   public LiveData<PaymentResponeResult> getPaymentResponseResult() {
        return paymentResponeResultData;
    }

    public void setPaymentResponseResult(PaymentResponseModel paymentResponseModel)
    {
        disposables.add(paymentResponeResultObservable(paymentResponseModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PaymentResponseModel>(){
                    PaymentResponseModel paymentResponseModel;
                    String error;
                    @Override
                    public void onNext(PaymentResponseModel paymentResponseModel) {
                        this.paymentResponseModel = paymentResponseModel;
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        this.error = e.getMessage();
                        paymentResponeResultData.setValue(new PaymentResponeResult(this.error,this.paymentResponseModel));
                    }
                    @Override
                    public void onComplete() {
                        paymentResponeResultData.setValue(new PaymentResponeResult(this.error,this.paymentResponseModel));
                    }
                }));
    }
    static Observable<PaymentResponseModel> paymentResponeResultObservable(PaymentResponseModel paymentResponseModel)
    {
        return Observable.defer(() -> {
            return Observable.just(paymentResponseModel);
        });
    }

    public void paymentDataChanged(String cardNumber, String cardHolderName,
                                   String cvv, String postalCode,
                                   String date) {
       if(!isCardNumberValid(cardNumber))
       {
           paymentFormState.setValue(new PaymentFormState(R.string.invalid_card_number,null,null,null,null));
       }
      else if(isCardHolderNameValid(cardHolderName))
       {
           paymentFormState.setValue(new PaymentFormState(null,null,null,null,R.string.invalid_card_name));
       }
      else if(!isExpiryDate(date))
       {
           paymentFormState.setValue(new PaymentFormState(null,null,R.string.invalid_expiry_date,null,null));
       }
       else if(!isCvvVaild(cvv))
       {
           paymentFormState.setValue(new PaymentFormState(null,R.string.invalid_cvv,null,null,null));
       }
      else if(!isPostalCodeValid(postalCode))
       {
           paymentFormState.setValue(new PaymentFormState(null,null,null,R.string.invalid_postal_code,null));
       }
       else
       {
           paymentFormState.setValue(new PaymentFormState(true));
       }
    }

    private boolean isCvvVaild(String cvv)
    {
        return cvv.length() == 3;
    }

    private boolean isCardHolderNameValid(String cardHolderName)
    {
        return cardHolderName.length() <= 5;
    }

    private boolean isCardNumberValid(String cardNumber)
    {
        return cardNumber.length() == 16;
    }
    private boolean isPostalCodeValid(String postalCode)
    {
      return postalCode.length() == 6;
    }

    private  boolean isExpiryDate(String date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/MM/YYYY");
        try {
            simpleDateFormat.parse(date);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}