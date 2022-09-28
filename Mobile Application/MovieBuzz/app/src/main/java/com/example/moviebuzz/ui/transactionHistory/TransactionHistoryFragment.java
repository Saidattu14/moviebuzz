package com.example.moviebuzz.ui.transactionHistory;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviebuzz.R;
import com.example.moviebuzz.adapters.BookingHistoryAdapter;
import com.example.moviebuzz.data.model.BookingHistoryResponseModel;
import com.example.moviebuzz.databinding.FragmentMovieTicketsBinding;
import com.example.moviebuzz.databinding.FragmentTransactionHistoryBinding;

public class TransactionHistoryFragment extends Fragment {

    private TransactionHistoryViewModel transactionHistoryViewModel;
    private FragmentTransactionHistoryBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionHistoryBinding.inflate(inflater, container, false);
        transactionHistoryViewModel = new ViewModelProvider(requireActivity()).get(TransactionHistoryViewModel.class);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transactionHistoryViewModel.getBookingHistoryResult().observe(getViewLifecycleOwner(), new Observer<BookingHistoryResult>() {
            @Override
            public void onChanged(BookingHistoryResult bookingHistoryResult) {
             if(bookingHistoryResult != null) {
                 if (bookingHistoryResult.getError() == null) {
                     BookingHistoryAdapter adapter = (BookingHistoryAdapter) binding.transactionHistoryRecycleView.getAdapter();
                     if (adapter == null) {
                         //System.out.println(bookingHistoryResult.bookingHistoryResponseModel.getMovieBookingHistoryDetails());
                         adapter = new BookingHistoryAdapter(bookingHistoryResult.getBookingHistoryResponseModel().getMovieBookingHistoryDetails());
                         binding.transactionHistoryRecycleView.setAdapter(adapter);
                         binding.loading.setVisibility(View.INVISIBLE);
                     }
                 }
             }
            }
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        transactionHistoryViewModel.clear();
        binding = null;
    }
}