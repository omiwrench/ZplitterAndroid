package com.noza.zplitter.app.android.fragment;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.activity.CreateProfileActivity;
import com.noza.zplitter.app.android.activity.MainActivity;
import com.noza.zplitter.app.android.handler.ValidationErrorHandler;
import com.noza.zplitter.app.android.helper.InternetHelper;
import com.noza.zplitter.app.android.helper.SnackbarHelper;
import com.noza.zplitter.app.android.helper.view.KeyboardAnimationHelper;
import com.noza.zplitter.app.android.helper.view.KeyboardDismissHelper;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.NoResponseErrorResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthenticationFailedResponse;
import com.noza.zplitter.app.android.service.AuthService;
import com.orhanobut.logger.Logger;

/**
 * Created by omiwrench on 2016-01-28.
 */
public class LoginFragment extends Fragment{
    private static final String TAG = LoginFragment.class.getName();

    private static final float inputsTranslateDeltaYDip = 100;

    private ViewGroup container;
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;

    private KeyboardAnimationHelper keyboardAnimationHelper;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(InternetHelper.isConnectedToInternet(getContext())){
                enableLogin();
            }
            else{
                disableLogin();
            }
        }
    };

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(receiver, filter);
        super.onResume();
    }
    @Override
    public void onPause(){
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        bindViews(view);
        bindListeners(view);
        bindHelpers(view);

        return view;
    }

    private void bindViews(final View view){
        container = (ViewGroup) view.findViewById(R.id.container);
        emailInput = (EditText) view.findViewById(R.id.input_email);
        passwordInput = (EditText) view.findViewById(R.id.input_password);
        loginButton = (Button) view.findViewById(R.id.button_login);
    }

    private void bindListeners(final View view){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardDismissHelper.hideSoftKeyboard(getActivity());
                keyboardAnimationHelper.moveViewDown();
                sendAuthentication();
            }
        });
    }
    private void bindHelpers(final View view){
        float deltaYPix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, inputsTranslateDeltaYDip, getResources().getDisplayMetrics());
        keyboardAnimationHelper = new KeyboardAnimationHelper(container, deltaYPix);
        keyboardAnimationHelper.registerForInputsIn(container);

        KeyboardDismissHelper.setAutoHideKeyboard(view, this.getActivity(), keyboardAnimationHelper);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser && keyboardAnimationHelper != null){
            keyboardAnimationHelper.moveViewDown();
        }
    }

    private void sendAuthentication(){
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        AuthService.authenticate(email, password, new RequestFinishedListener<AuthSuccessResponse, AuthenticationFailedResponse>() {
            @Override
            public void onSuccess(AuthSuccessResponse response) {
                if(response.getUser().hasProfile()){
                    startMainActivity();
                }
                else{
                    startCreateProfileActivity();
                }
            }

            @Override
            public void onFail(AuthenticationFailedResponse response) {
                if(response.getCause() instanceof AuthenticationFailedResponse.AuthenticationFailedCause){
                    switch ((AuthenticationFailedResponse.AuthenticationFailedCause) response.getCause()) {
                        case MISSING_EMAIL:
                            showValidationError(R.string.error_auth_missing_email);
                            break;
                        case MISSING_PASSWORD:
                            showValidationError(R.string.error_auth_missing_password);
                            break;
                        case INVALID_CREDENTIALS:
                            showValidationError(R.string.error_auth_invalid);
                            clearPasswordField();
                            break;
                    }
                }
                else{
                    SnackbarHelper.showUnknownErrorSnackbar(getContext(), getActivity().findViewById(android.R.id.content));
                }
            }

            @Override
            public void onError(ErrorResponse response) {
                if (response instanceof NoResponseErrorResponse) {
                    showValidationError(R.string.error_general_no_network_response);
                }
                Logger.e("Error: " + response.getMessage());
            }
        }, getContext());
    }

    private void startMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startCreateProfileActivity(){
        Intent intent = new Intent(this.getContext(), CreateProfileActivity.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            this.getActivity().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this.getActivity()).toBundle());
        }
        else{
            startActivity(intent);
        }
    }
    private void showValidationError(int messageId){
        if(getActivity() instanceof ValidationErrorHandler){
            ((ValidationErrorHandler) getActivity()).showValidationError(messageId);
        }
    }
    private void disableLogin(){
        loginButton.setEnabled(false);
        loginButton.setAlpha(0.50f);
    }
    private void enableLogin(){
        loginButton.setEnabled(true);
        loginButton.setAlpha(1.00f);
    }
    private void clearPasswordField(){
        passwordInput.setText("");
    }
}
