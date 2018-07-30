package com.noza.zplitter.app.android.rest.request.auth;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.constant.HttpStatus;
import com.noza.zplitter.app.android.exception.auth.RequestFailedError;
import com.noza.zplitter.app.android.exception.network.NoResponseError;
import com.noza.zplitter.app.android.rest.request.PostRequest;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthenticationFailedResponse;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;

/**
 * Created by omiwrench on 2016-03-03.
 */
public class AuthenticationRequest extends PostRequest{

    public AuthenticationRequest(Context context,
                                 String email,
                                 String password,
                                 Response.Listener<DivisionResponse> listener,
                                 Response.ErrorListener errorListener){
        super(context.getString(R.string.url_authenticate), listener, errorListener);

        try{
            JSONObject authJson = new JSONObject();
            authJson.put("email", email);
            authJson.put("password", password);
            this.body.put("user", authJson);
        }
        catch(JSONException e){
            Logger.e("Could not parse auth into json");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deliverError(VolleyError error){
        if(error instanceof RequestFailedError){
            RequestFailedError failedError = (RequestFailedError) error;
            Logger.d(((RequestFailedError) error).getFailCause());
            listener.onResponse(new AuthenticationFailedResponse(failedError.getFailCause()));
        }
        else{
            super.deliverError(error);
        }
    }

    @Override
    protected Response<DivisionResponse> parseNetworkResponse(NetworkResponse rawResponse){
        try{
            if(rawResponse.statusCode == HttpStatus.OK){
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
        if((error != null && error.networkResponse != null) &&
          (error.networkResponse.statusCode == HttpStatus.UNAUTHORIZED ||
           error.networkResponse.statusCode == HttpStatus.BAD_REQUEST)){
            return super.requestFailed(error);
        }
        return super.parseNetworkError(error);
    }
}
