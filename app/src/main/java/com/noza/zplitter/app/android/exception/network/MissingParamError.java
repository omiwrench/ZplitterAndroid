package com.noza.zplitter.app.android.exception.network;

import com.android.volley.VolleyError;

/**
 * Created by omiwrench on 2016-01-31.
 */
public class MissingParamError extends VolleyError{

    private final String param;

    public MissingParamError(String param){
        super("Missing " + param + " parameter");
        this.param = param;
    }

    public String getMissingParam(){
        return param;
    }
}
