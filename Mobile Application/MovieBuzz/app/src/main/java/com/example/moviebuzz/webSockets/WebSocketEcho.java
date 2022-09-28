package com.example.moviebuzz.webSockets;

import androidx.annotation.Nullable;

import com.example.moviebuzz.data.model.BookingHistoryResponseModel;
import com.example.moviebuzz.data.model.BookingResponseModel;
import com.example.moviebuzz.data.model.MovieReviewsResponseBody;
import com.example.moviebuzz.data.model.PaymentResponseModel;
import com.example.moviebuzz.data.model.TheatersAndTicketsModel;
import com.example.moviebuzz.data.model.TheatersAndTicketsResponseModel;
import com.example.moviebuzz.ui.movieAvalablity.MovieAvailabilityViewModel;
import com.example.moviebuzz.ui.payment.PaymentViewModel;
import com.example.moviebuzz.ui.reviews.FragmentAllReviewsViewModel;
import com.example.moviebuzz.ui.tickets.MovieTicketsFragment;
import com.example.moviebuzz.ui.tickets.MovieTicketsViewModel;
import com.example.moviebuzz.ui.transactionHistory.TransactionHistoryViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketEcho extends WebSocketListener {

    MovieAvailabilityViewModel movieAvailabilityViewModel;
    MovieTicketsViewModel movieTicketsViewModel;
    FragmentAllReviewsViewModel allReviewsViewModel;
    PaymentViewModel paymentViewModel;
    TransactionHistoryViewModel transactionHistoryViewModel;
    public WebSocketEcho() {
        super();
    }

    public void setMovieAvailabilityViewModel(MovieAvailabilityViewModel movieAvailabilityViewModel) {
        this.movieAvailabilityViewModel = movieAvailabilityViewModel;
    }

    public void setMovieTicketsViewModel(MovieTicketsViewModel movieTicketsViewModel) {
        this.movieTicketsViewModel = movieTicketsViewModel;
    }

    public void setAllReviewsViewModel(FragmentAllReviewsViewModel allReviewsViewModel) {
        this.allReviewsViewModel = allReviewsViewModel;
    }

    public void setPaymentViewModelViewModel(PaymentViewModel paymentViewModel) {
        this.paymentViewModel = paymentViewModel;
    }

    public void setTransactionHistoryViewModel(TransactionHistoryViewModel transactionHistoryViewModel) {
        this.transactionHistoryViewModel = transactionHistoryViewModel;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Gson gson = new Gson();
        try {
                System.out.println(text.toString());
                Type listType = new TypeToken<TheatersAndTicketsResponseModel>(){}.getType();
                TheatersAndTicketsResponseModel theatersAndTicketsResponseModel = gson.fromJson(text,listType);
                this.movieAvailabilityViewModel.updateMovieAvailabilityResult(theatersAndTicketsResponseModel.getTheatersAndTicketsModelList());
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        try {
            Type listType = new TypeToken<BookingResponseModel>() {}.getType();
            BookingResponseModel bookingResponseModel = gson.fromJson(text, listType);
            System.out.println(bookingResponseModel.getCityName());
            this.movieTicketsViewModel.setBookingStatus(bookingResponseModel);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        try {
            Type listType = new TypeToken<MovieReviewsResponseBody>() {}.getType();
            MovieReviewsResponseBody movieReviewsResponseBody = gson.fromJson(text, listType);
            this.allReviewsViewModel.setMovieReviewsResultDataLiveData(movieReviewsResponseBody);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        try {
            Type listType = new TypeToken<PaymentResponseModel>() {}.getType();
            PaymentResponseModel paymentResponseModel = gson.fromJson(text, listType);
            this.paymentViewModel.setPaymentResponseResult(paymentResponseModel);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        try {
            Type listType = new TypeToken<BookingHistoryResponseModel>() {}.getType();
            BookingHistoryResponseModel bookingHistoryResponseModel = gson.fromJson(text, listType);
            this.transactionHistoryViewModel.setBookingHistoryResponseModelLiveDataResult(bookingHistoryResponseModel);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }
}
