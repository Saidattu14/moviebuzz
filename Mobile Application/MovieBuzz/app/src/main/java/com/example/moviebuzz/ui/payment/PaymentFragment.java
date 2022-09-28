package com.example.moviebuzz.ui.payment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.BookingRequestModel;
import com.example.moviebuzz.data.model.BookingResponseModel;
import com.example.moviebuzz.data.model.PaymentBookingRequestModel;
import com.example.moviebuzz.ui.tickets.MovieBookingResult;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.example.moviebuzz.webSockets.WebSocketClass;
import com.example.moviebuzz.webSockets.WebSocketEcho;
import com.example.moviebuzz.data.model.PaymentData;
import com.example.moviebuzz.data.model.PaymentRequestModel;
import com.example.moviebuzz.databinding.FragmentPaymentBinding;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityViewModel;
import com.example.moviebuzz.ui.paymentStatus.PaymentStatusFragment;
import com.example.moviebuzz.ui.tickets.MovieTicketsViewModel;
import com.google.gson.Gson;

import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.WebSocket;

@AndroidEntryPoint
public class PaymentFragment extends Fragment {

    private PaymentViewModel paymentViewModel;
    @Inject
    WebSocketClass webSocketClass;
    private WebSocket webSocket;
    private WebSocketEcho webSocketEcho;
    private FragmentPaymentBinding binding;
    private MovieTicketsViewModel movieTicketsViewModel;
    private MovieAvailabilityViewModel movieAvailabilityViewModel;
    private PaymentStatusFragment paymentStatusFragment;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        webSocket = webSocketClass.getWebSocket();
        webSocketEcho = webSocketClass.getWebSocketEcho();
        paymentViewModel = new ViewModelProvider(requireActivity()).get(PaymentViewModel.class);
        movieTicketsViewModel = new ViewModelProvider(requireActivity()).get(MovieTicketsViewModel.class);
        mainViewModel =  new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        movieAvailabilityViewModel = new ViewModelProvider(requireActivity()).get(MovieAvailabilityViewModel.class);
        webSocketEcho.setPaymentViewModelViewModel(paymentViewModel);
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       final EditText cardHolderNameEdit = binding.cardHolderName;
       final EditText cardNumberEdit = binding.cardNumber;
       final EditText cvvEdit = binding.cvv;
       final EditText postalEdit = binding.postalCode;
       final EditText expiryDateEdit = binding.expiryDate;
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                paymentViewModel.paymentDataChanged(cardNumberEdit.getText().toString(),
                        cardHolderNameEdit.getText().toString(),cvvEdit.getText().toString(),
                postalEdit.getText().toString(),expiryDateEdit.getText().toString());
            }
        };
        cardHolderNameEdit.addTextChangedListener(afterTextChangedListener);
        cardNumberEdit.addTextChangedListener(afterTextChangedListener);
        cvvEdit.addTextChangedListener(afterTextChangedListener);
        postalEdit.addTextChangedListener(afterTextChangedListener);
        expiryDateEdit.addTextChangedListener(afterTextChangedListener);

        paymentViewModel.getPaymentFormState().observe(getViewLifecycleOwner(), new Observer<PaymentFormState>() {
            @Override
            public void onChanged(PaymentFormState paymentFormState) {
                if(paymentFormState.getCardNameError() != null)
                {
                    cardHolderNameEdit.setError(getString(paymentFormState.getCardNameError()));
                }
                else if(paymentFormState.getCardNumberError() != null)
                {
                    cardNumberEdit.setError(getString(paymentFormState.getCardNumberError()));
                }
                else if(paymentFormState.getCvvError() != null)
                {
                    cvvEdit.setError(getString(paymentFormState.getCvvError()));
                }
                else if(paymentFormState.getPostalCodeError() != null)
                {
                    postalEdit.setError(getString(paymentFormState.getPostalCodeError()));
                }
                else if(paymentFormState.getExpiryDateError() != null)
                {
                    expiryDateEdit.setError(getString(paymentFormState.getExpiryDateError()));
                }
            }
        });
        binding.buttonFirst1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paymentViewModel.getPaymentFormState().getValue().isDataValid() && binding.loading.getVisibility() == View.INVISIBLE)
                {
                    BookingRequestModel bookingRequestModel = movieTicketsViewModel.getBookingRequestData().getValue().getBookingRequestModel();
                    PaymentBookingRequestModel paymentBookingRequestModel = new PaymentBookingRequestModel(
                            bookingRequestModel.getCityName(), bookingRequestModel.getMovieName(), bookingRequestModel.getTheater_id(),
                            bookingRequestModel.getShow_id(), bookingRequestModel.getSeating(), bookingRequestModel.getState(),
                            bookingRequestModel.getCountryName(), bookingRequestModel.getTheater_name(), bookingRequestModel.getDate(), bookingRequestModel.getBookingId(),
                            bookingRequestModel.getMovieId(), bookingRequestModel.getMoviePoster()
                    );

                    PaymentData paymentData = new PaymentData(cardHolderNameEdit.getText().toString(),
                            cardNumberEdit.getText().toString(),expiryDateEdit.getText().toString(),
                            postalEdit.getText().toString(),cvvEdit.getText().toString(),
                            movieTicketsViewModel.getBookingResult().getValue().getBookingResponseModel().getPayment_id());
                    PaymentRequestModel paymentRequestModel = new PaymentRequestModel(
                            UUID.randomUUID(),
                            "ValidatePayment",
                            paymentData,
                            paymentBookingRequestModel,
                            mainViewModel.getToken(),mainViewModel.getUserId());
                    Gson gson = new Gson();
                    String json = gson.toJson(paymentRequestModel, PaymentRequestModel.class);
                    webSocket.send(json.toString());
                    binding.loading.setVisibility(View.VISIBLE);
                    closeKeyBoard();
                }
            }
        });
        paymentViewModel.getPaymentResponseResult().observe(getViewLifecycleOwner(), new Observer<PaymentResponeResult>() {
            @Override
            public void onChanged(PaymentResponeResult paymentResponeResult) {
                if(paymentResponeResult != null) {
                    if (paymentResponeResult.getError() == null && binding.loading.getVisibility() == View.VISIBLE) {
                        paymentStatusFragment = new PaymentStatusFragment();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.paymentFragment, paymentStatusFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });
    }


    private void closeKeyBoard()
    {
        View view = (View) binding.getRoot().getFocusedChild();
        if(view != null)
        {
            InputMethodManager manager = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}