package com.example.moviebuzz.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebuzz.ui.paymentStatus.PaymentStatusFragment;
import com.example.moviebuzz.ui.viewModel.MainViewModel;
import com.example.moviebuzz.ui.viewModel.ViewModelFactory;
import com.example.moviebuzz.databinding.FragmentLoginBinding;
import com.example.moviebuzz.R;
public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private MainViewModel mainViewModel;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        loginViewModel = new ViewModelProvider(this, new ViewModelFactory())
                .get(LoginViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
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
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button SignUp = binding.signup;
        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if(loginResult == null)
                {
                    return;
                }
                else if (loginResult.getError() != null) {
                    System.out.println(loginResult.getError());
                    //showLoginFailed(loginResult.getError());
                    binding.responseData.setVisibility(View.VISIBLE);
                    binding.responseData.setText(loginResult.getError());
                }
                else if (loginResult.getLoginResponse() != null) {

                    mainViewModel.setJwtToken(loginResult.getLoginResponse().getJwtToken());
                    mainViewModel.setUserEmail(loginResult.getLoginResponse().getUserEmail());
                    mainViewModel.setUserId(loginResult.getLoginResponse().getUserId());
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment2_to_registerFragment);
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

    private void navigateToSearch()
    {
        NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment2_to_searchFragment);
    }



    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.username.getText().clear();
        binding.password.getText().clear();
        binding.passwordTextInput.clearOnEditTextAttachedListeners();
        binding.usernameTextInput.clearOnEditTextAttachedListeners();
        binding = null;
        loginViewModel.clear();
    }
}