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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.activity.CreateProfileActivity;
import com.noza.zplitter.app.android.handler.ValidationErrorHandler;
import com.noza.zplitter.app.android.helper.InternetHelper;
import com.noza.zplitter.app.android.helper.SnackbarHelper;
import com.noza.zplitter.app.android.helper.view.KeyboardAnimationHelper;
import com.noza.zplitter.app.android.helper.view.KeyboardDismissHelper;
import com.noza.zplitter.app.android.listener.auth.AuthCompletedListener;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.NoResponseErrorResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.RegistrationFailedResponse;
import com.noza.zplitter.app.android.rest.response.failcause.Cause;
import com.noza.zplitter.app.android.rest.response.user.ProfileCreationFailedResponse;
import com.noza.zplitter.app.android.service.AuthService;

/**
 * Created by omiwrench on 2016-01-28.
 */
public class RegisterFragment extends Fragment{
    private static final String TAG = RegisterFragment.class.getName();

    private static final float inputsTranslateDeltaYDip = 125;

    private ViewGroup container;
    private EditText emailInput;
    private EditText passwordInput;
    private CheckBox termsCheckbox;
    private Button registerButton;

    private KeyboardAnimationHelper keyboardAnimationHelper;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(InternetHelper.isConnectedToInternet(getContext())){
                enableRegister();
            }
            else{
                disableRegister();
            }
        }
    };

    public static RegisterFragment newInstance(){
        return new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        bindViews(view);
        bindListeners(view);
        bindHelpers(view);

        return view;
    }

    private void bindViews(final View view){
        container = (ViewGroup) view.findViewById(R.id.container);
        emailInput = (EditText) view.findViewById(R.id.input_email);
        passwordInput = (EditText) view.findViewById(R.id.input_password);
        termsCheckbox = (CheckBox) view.findViewById(R.id.checkbox_terms);
        registerButton = (Button) view.findViewById(R.id.button_register);
    }

    private void bindListeners(final View view){
        termsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                endEditing();
            }
        });
        view.findViewById(R.id.text_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsCheckbox.toggle();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endEditing();
                registerUser();
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

    private void registerUser(){
        if(!termsCheckbox.isChecked()){
            showValidationError(R.string.error_register_accept_terms);
            return;
        }
        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        AuthService.registerUser(email, password, new AuthCompletedListener<RegistrationFailedResponse>() {
            @Override
            public void onSuccess(AuthSuccessResponse response) {
                startCreateProfileActivity();
            }

            @Override
            public void onFail(RegistrationFailedResponse response) {
                Cause cause = response.getCause();
                if(cause instanceof RegistrationFailedResponse.RegistrationFailCause){
                    switch ((RegistrationFailedResponse.RegistrationFailCause)response.getCause()) {
                        case EMAIL_TAKEN:
                            showValidationError(R.string.error_register_email_taken);
                            break;
                        case INVALID_EMAIL:
                            showValidationError(R.string.error_register_email_invalid);
                            break;
                        case MISSING_EMAIL:
                            showValidationError(R.string.error_register_email_missing);
                            break;
                        case MISSING_PASSWORD:
                            showValidationError(R.string.error_register_password_missing);
                            break;
                        case PASSWORD_TOO_SHORT:
                            showValidationError(R.string.error_register_password_too_short);
                            break;
                        default:
                            showValidationError(R.string.error_general_unknown);
                    }
                }
                else{
                    SnackbarHelper.showUnknownErrorSnackbar(getContext(), getActivity().findViewById(android.R.id.content));
                }
            }

            @Override
            public void onError(ErrorResponse response) {
                if(response instanceof NoResponseErrorResponse){
                    showValidationError(R.string.error_general_no_network_response);
                }
            }
        }, this.getContext());
    }

    private void endEditing(){
        KeyboardDismissHelper.hideSoftKeyboard(getActivity());
        keyboardAnimationHelper.moveViewDown();
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
    private void disableRegister(){
        registerButton.setEnabled(false);
        registerButton.setAlpha(0.50f);
    }
    private void enableRegister(){
        registerButton.setEnabled(true);
        registerButton.setAlpha(1.00f);
    }
}
