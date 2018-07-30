package com.noza.zplitter.app.android.listener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.noza.zplitter.app.android.exception.network.NoResponseError;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.NoResponseErrorResponse;

/**
 * Created by omiwrench on 2016-03-03.
 */
public class DivisionErrorListener implements Response.ErrorListener{

    private RequestFinishedListener<?, ?> callback;

    public DivisionErrorListener(RequestFinishedListener<?, ?> callback){
        this.callback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ErrorResponse response;
        if(error instanceof NoResponseError){
            response = new NoResponseErrorResponse();
        }
        else{
            response = new ErrorResponse(error.getMessage(), error.networkResponse.statusCode);
        }
        callback.onError(response);
    }
}
