package com.noza.zplitter.app.android.rest.request;

import com.android.volley.Response;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

/**
 * Created by omiwrench on 2016-03-02.
 */
public abstract class AuthenticatedPostRequest extends PostRequest{

    private final String token;

    public AuthenticatedPostRequest(String url, String token, Response.ErrorListener errorListener){
        super(url, errorListener);
        this.token = token;
        init();
    }
    public AuthenticatedPostRequest(String url, String token, Response.Listener<DivisionResponse> listener, Response.ErrorListener errorListener){
        super(url, listener, errorListener);
        this.token = token;
        init();
    }

    private void init(){
        try{
            this.body.put("token", token);
        }
        catch(JSONException e){
            Logger.e("Invalid token, cannot make json");
            throw new IllegalArgumentException("Invalid token argument");
        }
    }
}
