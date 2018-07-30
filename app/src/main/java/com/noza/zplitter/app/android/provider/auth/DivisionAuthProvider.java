package com.noza.zplitter.app.android.provider.auth;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.noza.zplitter.app.android.listener.DivisionErrorListener;
import com.noza.zplitter.app.android.listener.DivisionResponseListener;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.rest.request.auth.AuthenticationRequest;
import com.noza.zplitter.app.android.rest.request.auth.RegisterRequest;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthenticationFailedResponse;
import com.noza.zplitter.app.android.rest.response.auth.RegistrationFailedResponse;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class DivisionAuthProvider {
    private static final String TAG = DivisionAuthProvider.class.getName();

    private final Context context;

    public DivisionAuthProvider(Context context){
        this.context = context;
    }

    public void authenticate(String email, String password, final RequestFinishedListener<AuthSuccessResponse, AuthenticationFailedResponse> callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        AuthenticationRequest request = new AuthenticationRequest(context, email, password,
                new DivisionResponseListener<>(callback),
                new DivisionErrorListener(callback));

        request.setShouldCache(false);
        queue.add(request);
    }

    public void register(String email, String password, final RequestFinishedListener<AuthSuccessResponse, RegistrationFailedResponse> callback){
        RequestQueue queue = Volley.newRequestQueue(context);
                RegisterRequest request = new RegisterRequest(context, email, password,
                        new DivisionResponseListener<>(callback),
                        new DivisionErrorListener(callback));

        request.setShouldCache(false);
        queue.add(request);
    }
}
