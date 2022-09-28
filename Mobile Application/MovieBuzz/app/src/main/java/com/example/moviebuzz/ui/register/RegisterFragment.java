package com.example.moviebuzz.ui.register;

import androidx.annotation.StringRes;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebuzz.R;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.databinding.FragmentRegisterBinding;
import com.example.moviebuzz.ui.login.LoginFragment;
import com.example.moviebuzz.ui.search.SearchViewModel;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.example.moviebuzz.ui.viewModel.ViewModelFactory;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private SearchViewModel searchViewModel;
    private MainViewModel mainViewModel;
    private  RegisterViewModel registerViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        registerViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(RegisterViewModel.class);
        if(mainViewModel.getToken() != null)
        {
            getActivity().finishAffinity();
            binding = null;
            return null;
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final EditText emailEdit = binding.usernameRegister;
        final EditText passwordEdit = binding.editTextTextPassword;
        final EditText passwordConfirmEdit = binding.editTextTextPassword2;
        final Button submit = binding.signup;
        final ProgressBar progressBar = binding.loading;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              progressBar.setVisibility(View.VISIBLE);
              registerViewModel.register(emailEdit.getText().toString(),passwordEdit.getText().toString(),passwordConfirmEdit.getText().toString());

            }
        });

       registerViewModel.getRegisterFormState().observe(getViewLifecycleOwner(),new Observer<RegisterFormState>() {
           @Override
           public void onChanged(@Nullable RegisterFormState registerFormState) {
               if (registerFormState == null) {
                   return;
               }
               if (registerFormState.getUsernameError() != null) {
                   emailEdit.setError(getString(registerFormState.getUsernameError()));
               }
               if (registerFormState.getPasswordError() != null) {
                   passwordEdit.setError(getString(registerFormState.getPasswordError()));
               }
               if (registerFormState.getPasswordUnMatchError() != null)
               {
                  passwordConfirmEdit.setError(getString(registerFormState.getPasswordUnMatchError()));
               }
           }
       });

       registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<RegisterResult>() {
           @Override
           public void onChanged(RegisterResult registerResult) {
               progressBar.setVisibility(View.GONE);
               if(registerResult == null)
               {
                   return;
               }
               else if(registerResult.getError() != null)
               {
                   binding.responseData.setText(registerResult.getError());
               }
               else if(registerResult.getError() == null)
               {
                   mainViewModel.setJwtToken(registerResult.getRegisterResponse().getJwtToken());
                   mainViewModel.setUserEmail(registerResult.getRegisterResponse().getUserEmail());
                   mainViewModel.setUserId(registerResult.getRegisterResponse().getUserId());
                   navigateToSearch();
               }
           }
       });

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
                registerViewModel.registerDataChanged(emailEdit.getText().toString(),passwordEdit.getText().toString(),passwordConfirmEdit.getText().toString());
            }
        };

        emailEdit.addTextChangedListener(afterTextChangedListener);
        passwordConfirmEdit.addTextChangedListener(afterTextChangedListener);
        passwordEdit.addTextChangedListener(afterTextChangedListener);

        emailEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                registerViewModel.register(emailEdit.getText().toString(),passwordEdit.getText().toString(),passwordConfirmEdit.getText().toString());
            }
            return false;
        });
        passwordEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                registerViewModel.register(emailEdit.getText().toString(),passwordEdit.getText().toString(),passwordConfirmEdit.getText().toString());
            }
            return false;
        });

        passwordConfirmEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(emailEdit.getText().toString(),passwordEdit.getText().toString(),passwordConfirmEdit.getText().toString());
                }
                return false;
            }
        });
    }
    private void showRegisterFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToSearch()
    {
        NavHostFragment.findNavController(RegisterFragment.this)
                .navigate(R.id.action_registerFragment_to_searchFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        registerViewModel.clear();
    }
}