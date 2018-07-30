package com.noza.zplitter.app.android.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.exception.auth.NoLoggedInUserException;
import com.noza.zplitter.app.android.listener.auth.AuthCompletedListener;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.provider.auth.DivisionAuthProvider;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthenticationFailedResponse;
import com.noza.zplitter.app.android.rest.response.auth.RegistrationFailedResponse;

/**
 * Created by omiwrench on 2016-01-30.
 */

public class AuthService {

    private AuthService(){}

    public static void authenticate(String email,
                                    String password,
                                    final RequestFinishedListener<AuthSuccessResponse, AuthenticationFailedResponse> callback,
                                    final Context context){
        DivisionAuthProvider provider = new DivisionAuthProvider(context);
        provider.authenticate(email, password, new RequestFinishedListener<AuthSuccessResponse, AuthenticationFailedResponse>() {
            @Override
            public void onSuccess(AuthSuccessResponse response) {
                AuthService.saveToken(response.getToken(), context);
                UserService.saveCurrentUser(response.getUser(), context);
                callback.onSuccess(response);
            }
            @Override
            public void onFail(AuthenticationFailedResponse response) {
                callback.onFail(response);
            }
            @Override
            public void onError(ErrorResponse response) {
                callback.onError(response);
            }
        });
    }

    public static void registerUser(String email, String password, final AuthCompletedListener<RegistrationFailedResponse> callback, final Context context){
        DivisionAuthProvider provider = new DivisionAuthProvider(context);
        provider.register(email, password, new RequestFinishedListener<AuthSuccessResponse, RegistrationFailedResponse>() {
            @Override
            public void onSuccess(AuthSuccessResponse response) {
                AuthService.saveToken(response.getToken(), context);
                callback.onSuccess(response);
            }

            @Override
            public void onFail(RegistrationFailedResponse response) {
                callback.onFail(response);
            }

            @Override
            public void onError(ErrorResponse response) {
                callback.onError(response);
            }
        });
    }

    public static void logout(Context context){
        clearAuth(context);
        UserService.removeCurrentUser(context);
        AvatarService.clearAvatar(context);
    }
    private static void clearAuth(Context context){
        SharedPreferences sharedPreferences = getSharedPrefs(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(context.getString(R.string.token_key));

        editor.apply();
    }

    public static boolean isUserLoggedIn(Context context){
        SharedPreferences sharedPreferences = getSharedPrefs(context);
        return sharedPreferences.getString(context.getString(R.string.token_key), null) != null;
    }

    public static void saveToken(String token, Context context){
        SharedPreferences sharedPreferences = getSharedPrefs(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(context.getString(R.string.token_key), token);

        editor.apply();
    }
    public static String getToken(Context context){
        SharedPreferences sharedPreferences = getSharedPrefs(context);
        String token = sharedPreferences.getString(context.getString(R.string.token_key), null);
        if(token == null){
            throw new NoLoggedInUserException();
        }
        return token;
    }

    private static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences(context.getString(R.string.auth_pref_file_key), Context.MODE_PRIVATE);
    }
}
