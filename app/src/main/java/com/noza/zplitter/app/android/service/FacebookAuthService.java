package com.noza.zplitter.app.android.service;

import android.content.Context;

import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.listener.auth.AuthCompletedListener;
import com.noza.zplitter.app.android.provider.auth.FacebookAuthProvider;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.FacebookAuthFailedResponse;
import com.orhanobut.logger.Logger;

/**
 * Created by omiwrench on 2016-02-17.
 */
public class FacebookAuthService {

    private final Context context;

    public FacebookAuthService(Context context){
        this.context = context;
    }

    public void authenticate(String id, String token, final RequestFinishedListener<AuthSuccessResponse, FacebookAuthFailedResponse> callback){
        FacebookAuthProvider provider = new FacebookAuthProvider(context);
        provider.authenticate(id, token, new RequestFinishedListener<AuthSuccessResponse, FacebookAuthFailedResponse>() {
            @Override
            public void onSuccess(AuthSuccessResponse response) {
                Logger.d(response.getUser().getFullName());
                AuthService.saveToken(response.getToken(), context);
                UserService.saveCurrentUser(response.getUser(), context);
                AvatarService.updateUserAvatar(context);
                callback.onSuccess(response);
            }
            @Override
            public void onFail(FacebookAuthFailedResponse response) {
                callback.onFail(response);
            }
            @Override
            public void onError(ErrorResponse response) {
                callback.onError(response);
            }
        });
    }
}
