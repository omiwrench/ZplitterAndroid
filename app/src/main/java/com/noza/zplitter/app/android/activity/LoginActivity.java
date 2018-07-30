package com.noza.zplitter.app.android.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.adapter.LoginViewFragmentPageAdapter;
import com.noza.zplitter.app.android.handler.ValidationErrorHandler;
import com.noza.zplitter.app.android.helper.InternetHelper;
import com.noza.zplitter.app.android.helper.SnackbarHelper;
import com.noza.zplitter.app.android.helper.view.KeyboardDismissHelper;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.listener.auth.AuthCompletedListener;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.FacebookAuthFailedResponse;
import com.noza.zplitter.app.android.service.FacebookAuthService;
import com.noza.zplitter.app.android.view.dialog.FacebookLoginProgressDialog;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements ValidationErrorHandler{

    private Button facebookButton;
    private TabLayout tabLayout;
    private Dialog facebookDialog;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Logger.init();
        setContentView(R.layout.activity_login);

        bindViews();
        setupTabLayout();
        setupFacebookLogin();
        bindListeners();

        if(InternetHelper.isConnectedToInternet(this)){
            setStateNoInternet();
        }
    }
    @Override
    protected void onResume(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        super.onResume();
    }
    @Override
    protected void onPause(){
        unregisterReceiver(receiver);
        super.onPause();
    }
    private void bindViews(){
        facebookButton = (Button) findViewById(R.id.button_facebook);
        tabLayout = (TabLayout) findViewById(R.id.login_tabs);
    }
    private void bindListeners(){
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> permissions = Arrays.asList(getResources().getStringArray(R.array.facebook_permissions));
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissions);
                showFacebookProgressDialog();
            }
        });
    }
    private void setupTabLayout(){
        ViewPager pager = (ViewPager) findViewById(R.id.login_view_pager);
        pager.setAdapter(new LoginViewFragmentPageAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(pager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                KeyboardDismissHelper.hideSoftKeyboard(LoginActivity.this);
            }
        });
    }
    private void setupFacebookLogin(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                finishFacebookAuth(loginResult);
            }

            @Override
            public void onCancel() {
                facebookDialog.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                Logger.e(error.getMessage());
                facebookDialog.dismiss();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void finishFacebookAuth(LoginResult result){
        String id = Profile.getCurrentProfile().getId();
        String token = result.getAccessToken().getToken();

        FacebookAuthService fbAuthService = new FacebookAuthService(LoginActivity.this);
        fbAuthService.authenticate(id, token, new RequestFinishedListener<AuthSuccessResponse, FacebookAuthFailedResponse>() {
            @Override
            public void onSuccess(AuthSuccessResponse response) {
                facebookDialog.dismiss();
                startMainActivity();
            }

            @Override
            public void onFail(FacebookAuthFailedResponse response) {
                Logger.e("Auth failed");
                Logger.e(response.getCauseString());
            }

            @Override
            public void onError(ErrorResponse response) {
                Logger.e("Auth error");
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(InternetHelper.isConnectedToInternet(LoginActivity.this)){
                setStateInternet();
            }
            else{
                setStateNoInternet();
            }
        }
    };

    public void showValidationError(int messageId){
        SnackbarHelper.showErrorSnackbar(this, findViewById(R.id.target_snackbar), messageId);
    }
    private void setStateNoInternet(){
        SnackbarHelper.showPersistantSnackbar(this, findViewById(R.id.target_snackbar), R.string.error_general_no_internet);
        facebookButton.setAlpha(0.56f);
    }
    private void setStateInternet(){
        SnackbarHelper.dismissPersistantSnackbar();
        facebookButton.setAlpha(1.00f);
    }
    private void showFacebookProgressDialog(){
        facebookDialog = new FacebookLoginProgressDialog(this);
        facebookDialog.show();
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
