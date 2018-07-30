package com.noza.zplitter.app.android.rest.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.noza.zplitter.app.android.constant.HttpStatus;
import com.noza.zplitter.app.android.exception.auth.RequestFailedError;
import com.noza.zplitter.app.android.exception.network.NoResponseError;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by omiwrench on 2016-02-01.
 */
abstract class DivisionRequest extends Request<DivisionResponse>{

    protected com.android.volley.Response.Listener<DivisionResponse> listener;

    public DivisionRequest(int method, String url, Response.ErrorListener errorListener){
        super(method, url, errorListener);
    }
    public DivisionRequest(int method, String url, Response.Listener<DivisionResponse> listener, Response.ErrorListener errorListener){
        super(method, url, errorListener);
        this.listener = listener;
    }

    protected JSONObject getResponseJson(NetworkResponse response) throws IllegalArgumentException, UnsupportedEncodingException{
        try{
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject json = new JSONObject(jsonString);
            return json;
        }
        catch(JSONException e){
            throw new IllegalArgumentException("Argument was not valid JSON");
        }
    }

    @Override
    public Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        return headers;
    }

    @Override
    public void deliverResponse(DivisionResponse response){
        listener.onResponse(response);
    }

    protected String getErrorCause(VolleyError error) throws IllegalArgumentException{
        try{
            String cause = new JSONObject(new String(error.networkResponse.data)).getString("cause");
            return cause;
        }
        catch(JSONException e){
            throw new IllegalArgumentException("Error did not contain a cause");
        }
    }

    @Override
    protected Response<DivisionResponse> parseNetworkResponse(NetworkResponse rawResponse){
        Logger.e("Unexpected response code: " + rawResponse.statusCode);
        return Response.error(new VolleyError("Unexpected response code: " + rawResponse.statusCode));
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError error){
        if(error == null || error.networkResponse == null){
            return new NoResponseError();
        }
        if(error.networkResponse.statusCode == HttpStatus.SERVER_ERROR){
            return new ServerError();
        }
        return error;
    }

    protected VolleyError requestFailed(VolleyError error){
        try{
            String cause = getErrorCause(error);
            return new RequestFailedError(cause);
        }
        catch(IllegalArgumentException e){
            Logger.e("Division returned unknown reason");
            Logger.e("Division response: " + new String(error.networkResponse.data));
            return new ServerError(error.networkResponse);
        }
    }
}
