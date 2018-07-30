package com.noza.zplitter.app.android.provider.auth;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.listener.auth.AuthCompletedListener;
import com.noza.zplitter.app.android.rest.request.auth.FacebookAuthRequest;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.FacebookAuthFailedResponse;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class FacebookAuthProvider {

    private final Context context;

    public FacebookAuthProvider(Context context){
        this.context = context;
    }

    public void authenticate(String id, String token, final RequestFinishedListener<AuthSuccessResponse, FacebookAuthFailedResponse> callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        FacebookAuthRequest request = new FacebookAuthRequest(context, id, token,
            new Response.Listener<DivisionResponse>() {
                @Override
                public void onResponse(DivisionResponse response) {
                    if(response.wasSuccess()){
                        callback.onSuccess((AuthSuccessResponse) response);
                    }
                    else{
                        callback.onFail((FacebookAuthFailedResponse)response);
                    }
                }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(new ErrorResponse(error.getMessage(), error.networkResponse.statusCode));
                }
            });
        request.setShouldCache(false);
        queue.add(request);
    }
}
