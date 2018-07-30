package com.noza.zplitter.app.android.rest.response;

import com.noza.zplitter.app.android.exception.network.NoResponseError;

/**
 * Created by omiwrench on 2016-03-05.
 */
public class NoResponseErrorResponse extends ErrorResponse{

    public NoResponseErrorResponse(){
        super("No network response", -1);
    }
}
