package com.example.moviebuzz.ui.addReview;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.StringRes;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.model.AddReviewRequestBody;
import com.example.moviebuzz.databinding.FragmentAddReviewBinding;
import com.example.moviebuzz.databinding.FragmentMovieBinding;
import com.example.moviebuzz.ui.search.SearchFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.google.android.material.slider.Slider;

import java.text.DecimalFormat;

public class AddReview extends Fragment {

    private AddReviewViewModel addReviewViewModel;
    private FragmentAddReviewBinding binding;
    private SearchViewModel searchViewModel;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddReviewBinding.inflate(inflater, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        addReviewViewModel = new ViewModelProvider(this).get(AddReviewViewModel.class);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        backButtonPressed();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                String s = String.format("%.02f", value);
                binding.ratingValue.setText(s);
            }
        });
        binding.reviewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                String movieID = searchViewModel.getSearchedMovieData().getValue().getCurrentSearchedMovieData().get_source().getImdbID();
                String review =  binding.addComment.getEditText().getText().toString();
                float rating =  binding.slider.getValue();
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                float twoDigitsF = Float.parseFloat(decimalFormat.format(rating));
                if(review.length() != 0 && rating != 2)
                {
                    String[] userName = mainViewModel.getUserEmail().split("@");
                    AddReviewRequestBody addReviewRequestBody = new AddReviewRequestBody(movieID, review,twoDigitsF,userName[0]);
                    addReviewViewModel.addReview(addReviewRequestBody,mainViewModel.getToken());
                }
            }
        });

        addReviewViewModel.getReviewResult().observe(getViewLifecycleOwner(), addReviewResult -> {
            if(addReviewResult.getError() == null)
            {
                binding.reviewResultValue.setText("Review will be added soon");
                binding.reviewResultValue.setVisibility(View.VISIBLE);
                showReviewAdded();
            }
            else
            {
                binding.reviewResultValue.setText("Sorry Try again Later");
                binding.reviewResultValue.setVisibility(View.VISIBLE);
                showReviewAddedError();
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
    private void showReviewAdded() {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(),"Review Will Be Added", Toast.LENGTH_LONG).show();
        }
    }

    private void showReviewAddedError() {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(),"Sorry Try again Later", Toast.LENGTH_LONG).show();
        }
    }

    private void backButtonPressed()
    {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(AddReview.this)
                        .popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        addReviewViewModel.clearDisposables();
    }
}