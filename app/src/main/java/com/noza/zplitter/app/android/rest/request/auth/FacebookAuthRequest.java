package com.noza.zplitter.app.android.rest.request.auth;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.constant.HttpStatus;
import com.noza.zplitter.app.android.exception.auth.RequestFailedError;
import com.noza.zplitter.app.android.rest.request.PostRequest;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;
import com.noza.zplitter.app.android.rest.response.auth.FacebookAuthFailedResponse;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by omiwrench on 2016-02-17.
 */
public class FacebookAuthRequest extends PostRequest{

    public FacebookAuthRequest(Context context,
                               String facebookId,
                               String facebookToken,
                               com.android.volley.Response.Listener<DivisionResponse> listener,
                               com.android.volley.Response.ErrorListener errorListener){
        super(context.getString(R.string.url_register_facebook), listener, errorListener);

        try{
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("id", facebookId);
            jsonUser.put("token", facebookToken);
            this.body.put("user", jsonUser);
        }
        catch(JSONException e){
            Logger.e("Could not parse user into json body");
            throw new IllegalArgumentException("Invalid auth");
        }
    }

    @Override
    public void deliverError(VolleyError error){
        if(error instanceof RequestFailedError){
            RequestFailedError failedError = (RequestFailedError) error;
            listener.onResponse(new FacebookAuthFailedResponse(failedError.getFailCause()));
        }
        else{
            Logger.e(error.getMessage());
            super.deliverError(error);
        }
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse res){
        try{
            if(res.statusCode == HttpStatus.CREATED ||
               res.statusCode == HttpStatus.OK){
                AuthSuccessResponse response = new AuthSuccessResponse(getResponseJson(res));
                return com.android.volley.Response.success((DivisionResponse) response, HttpHeaderParser.parseCacheHeaders(res));
            }
            else{
                return com.android.volley.Response.error(new VolleyError("Unexpected response code: " + res.statusCode));
            }
        }
        catch(UnsupportedEncodingException e){
            Logger.e("Facebook auth request returned response with unknown charset");
            return com.android.volley.Response.error(new ParseError(e));
        }
        catch(JSONException e){
            Logger.e("Malformed json in facebook auth response");
            Logger.e(e.getMessage());
            return com.android.volley.Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError error){
        switch (error.networkResponse.statusCode){
            case HttpStatus.CONFLICT:
            case HttpStatus.BAD_REQUEST:
                try{
                    String cause = getErrorCause(error);
                    return new RequestFailedError(cause);
                }
                catch(IllegalArgumentException e){
                    Logger.e("Division returned unknown reason");
                    return new ServerError(error.networkResponse);
                }
            case HttpStatus.SERVER_ERROR:
                return new ServerError();
            default:
                return error;
        }
    }
}
