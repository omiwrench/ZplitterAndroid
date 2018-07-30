package com.noza.zplitter.app.android.exception.auth;

import com.android.volley.VolleyError;

/**
 * Created by omiwrench on 2016-02-17.
 */
public class RequestFailedError extends VolleyError{
    private final String cause;

    public RequestFailedError(String cause){
        this.cause = cause;
    }

    public String getFailCause(){
        return cause;
    }
}
