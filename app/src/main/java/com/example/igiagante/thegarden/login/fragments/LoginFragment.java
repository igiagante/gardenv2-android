package com.example.igiagante.thegarden.login.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.igiagante.thegarden.R;
import com.example.igiagante.thegarden.core.Session;
import com.example.igiagante.thegarden.core.domain.entity.User;
import com.example.igiagante.thegarden.core.presentation.BaseFragment;
import com.example.igiagante.thegarden.home.MainActivity;
import com.example.igiagante.thegarden.login.LoginActivity;
import com.example.igiagante.thegarden.login.RegisterActivity;
import com.example.igiagante.thegarden.login.di.LoginComponent;
import com.example.igiagante.thegarden.login.presenters.LoginPresenter;
import com.example.igiagante.thegarden.login.view.LoginView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Ignacio Giagante, on 2/8/16.
 */
public class LoginFragment extends BaseFragment implements LoginView {

    public static final String TOKEN_PREFS_NAME = "token";
    public static final String USERNAME_PREFS_NAME = "username";

    @Inject
    Session session;

    @Inject
    LoginPresenter loginPresenter;

    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.login_email_id)
    EditText mUserEmail;

    @BindView(R.id.login_password_id)
    EditText mPassword;

    @BindView(R.id.btn_login_id)
    Button mButtonLogin;

    @BindView(R.id.signup_id)
    TextView mButtonSignUp;

    private InterstitialAd mInterstitialAd;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Get component in order to inject the presenter
         */
        this.getComponent(LoginComponent.class).inject(this);

        final View fragmentView = inflater.inflate(R.layout.login_fragment, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);


        // Restore Token from preferences
        String token = sharedPreferences.getString(TOKEN_PREFS_NAME, "");
        String username = sharedPreferences.getString(USERNAME_PREFS_NAME, "");

        mButtonLogin.setOnClickListener(v -> {
            if (!checkInternet()) {
                showToastMessage(getString(R.string.there_is_not_internet_connection));
            } else {
                loginUser();
            }
        });

        mButtonSignUp.setOnClickListener(v -> {
            if (!checkInternet()) {
                showToastMessage(getString(R.string.there_is_not_internet_connection));
            } else {
                getActivity().startActivity(new Intent(getContext(), RegisterActivity.class));
            }
        });

        if (!TextUtils.isEmpty(token)) {
            session.setToken(token);
            session.getUser().setUserName(username);
            if (session.checkIfTokenIsExpired()) {
                this.loginPresenter.refreshToken();
            }
            notifyUserLogin(getString(R.string.login_ok));
        }

        mUserEmail.requestFocus();

        return fragmentView;
    }

    private void loginUser() {

        if (!checkInternet()) {
            showToastMessage(getString(R.string.there_is_not_internet_connection));
            return;
        }

        if (!validate()) {
            onLoginFailed();
            return;
        }

        String username = mUserEmail.getText().toString();
        String password = mPassword.getText().toString();

        User user = new User();
        user.setUserName(username);
        user.setPassword(password);

        loginPresenter.loginUser(user);
    }

    private void onLoginFailed() {
        showToastMessage(getString(R.string.login_failed));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loginPresenter.setView(new WeakReference<>(this));
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_PREFS_NAME, session.getToken());
        editor.putString(USERNAME_PREFS_NAME, session.getUser().getUserName());
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.loginPresenter.destroy();
    }

    /**
     * First, try to login
     */
    @Override
    public void notifyUserLogin(String result) {
        if (!result.equals(getString(R.string.login_ok))) {
            showToastMessage(result);
        } else {
            cleanFields();
            this.loginPresenter.existsUser(session.getUser().getId());
        }
    }

    private void cleanFields() {
        mUserEmail.setText("");
        mPassword.setText("");
    }

    /**
     * Second, lets check if the user exists in DB
     */
    @Override
    public void userExists(Boolean exists) {
        if (!exists) {
            this.loginPresenter.saveUser(session.getUser());
        } else {
            goToMainActivity();
        }
    }

    /**
     * Third, after saving the user, lets move to the main activity
     */
    @Override
    public void notifyUserWasPersisted() {
        goToMainActivity();
    }

    @Override
    public void sendNewToken(String token) {
        this.session.setToken(token);
        goToMainActivity();
    }

    private void goToMainActivity() {

        // tracking
        LoginActivity activity = (LoginActivity) getActivity();
        activity.getTracker().send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.category_login))
                .setAction(getString(R.string.action_user_logged))
                .build());

        getActivity().finish();
        getActivity().startActivity(new Intent(getContext(), MainActivity.class));
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context context() {
        return this.getActivity().getApplicationContext();
    }

    private boolean validate() {
        boolean valid = true;

        String email = mUserEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mUserEmail.setError(getString(R.string.email_not_valid));
            valid = false;
        } else {
            mUserEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPassword.setError(getString(R.string.check_email_characters));
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }
}
