package com.example.moviebuzz.ui.paymentStatus;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.BookingResponseModel;
import com.example.moviebuzz.databinding.FragmentPaymentBinding;
import com.example.moviebuzz.databinding.FragmentPaymentStatusBinding;
import com.example.moviebuzz.ui.payment.PaymentResponeResult;
import com.example.moviebuzz.ui.payment.PaymentViewModel;
import com.example.moviebuzz.ui.search.SearchFragment;

public class PaymentStatusFragment extends Fragment {

    private PaymentStatusViewModel mViewModel;
    private FragmentPaymentStatusBinding binding;
    private PaymentViewModel paymentViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentStatusBinding.inflate(inflater, container, false);
        paymentViewModel = new ViewModelProvider(requireActivity()).get(PaymentViewModel.class);
        backButtonPressed();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        paymentViewModel.getPaymentResponseResult().observe(getViewLifecycleOwner(), new Observer<PaymentResponeResult>() {
            @Override
            public void onChanged(PaymentResponeResult paymentResponeResult) {
                System.out.println(paymentResponeResult);
                if(paymentResponeResult.getError() == null)
                {
                   String successTransaction = "TransactionDone";
                   String failedTransaction = "TransactionFailed";
                  if(paymentResponeResult.getPaymentResponseModel().getPaymentStatus().equals(successTransaction))
                  {
                      binding.transactionResult.setText("Transaction Successfully Completed");
                  }
                  else if(paymentResponeResult.getPaymentResponseModel().getPaymentStatus().equals(failedTransaction))
                  {
                        binding.transactionResult.setText("Sorry Transaction UnSuccessful");
                  }
                }
            }
        });
    }

    public void backButtonPressed()
    {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.paymentFragment);
                System.out.println(fragment);
                if(fragment != null)
                {

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .remove(fragment)
                            .commit();
                }
                NavHostFragment.findNavController(PaymentStatusFragment.this)
                        .popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}