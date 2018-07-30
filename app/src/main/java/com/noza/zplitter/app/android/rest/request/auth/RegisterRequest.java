package com.noza.zplitter.app.android.rest.request.auth;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.constant.HttpStatus;
import com.noza.zplitter.app.android.exception.auth.RequestFailedError;
import com.noza.zplitter.app.android.rest.request.PostRequest;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.RegistrationFailedResponse;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class RegisterRequest extends PostRequest{
    private static final String TAG = RegisterRequest.class.getName();

    public RegisterRequest(Context context,
                           String email,
                           String password,
                           Response.Listener<DivisionResponse> listener,
                           Response.ErrorListener errorListener){
        super(context.getString(R.string.url_register), listener, errorListener);

        try{
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("email", email);
            jsonUser.put("password", password);
            this.body.put("user", jsonUser);
        }
        catch(JSONException e) {
            Log.e(TAG, "Could not parse user into json body.");
            throw new IllegalArgumentException("Invalid user.");
        }
    }

    @Override
    public void deliverError(VolleyError error){
        if(error instanceof RequestFailedError){
            RequestFailedError failedError = (RequestFailedError) error;
            listener.onResponse(new RegistrationFailedResponse(failedError.getFailCause()));
        }
        else{
            super.deliverError(error);
        }
    }

    @Override
    protected Response<DivisionResponse> parseNetworkResponse(NetworkResponse rawResponse){
        try{
            if(rawResponse.statusCode == HttpStatus.CREATED){
                AuthSuccessResponse response = new AuthSuccessResponse(getResponseJson(rawResponse));
                return Response.success((DivisionResponse) response, HttpHeaderParser.parseCacheHeaders(rawResponse));
            }
            return super.parseNetworkResponse(rawResponse);
        }
        catch(Exception e){
            Logger.e(e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError error){
        if(error.networkResponse != null &&
          (error.networkResponse.statusCode == HttpStatus.CONFLICT ||
           error.networkResponse.statusCode == HttpStatus.BAD_REQUEST)){
            return super.requestFailed(error);
        }
        return super.parseNetworkError(error);
    }
}
