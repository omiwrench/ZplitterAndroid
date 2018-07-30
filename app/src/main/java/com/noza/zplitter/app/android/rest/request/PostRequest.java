package com.noza.zplitter.app.android.rest.request;

import com.android.volley.Response;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;

import org.json.JSONObject;

/**
 * Created by omiwrench on 2016-02-01.
 */
public abstract class PostRequest extends DivisionRequest {

    protected JSONObject body = new JSONObject();

    public PostRequest(String url, Response.ErrorListener errorListener){
        super(Method.POST, url, errorListener);
    }
    public PostRequest(String url, Response.Listener<DivisionResponse> listener, Response.ErrorListener errorListener){
        super(Method.POST, url, listener, errorListener);
    }

    @Override
    public byte[] getBody(){
        return this.body.toString().getBytes();
    }
}
