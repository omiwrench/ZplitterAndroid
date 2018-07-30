package com.noza.zplitter.app.android.rest.response;

import com.noza.zplitter.app.android.rest.response.failcause.Cause;

/**
 * Created by omiwrench on 2016-02-17.
 */
public abstract class FailResponse<E extends Enum> extends DivisionResponse {

    protected Cause cause;

    public FailResponse(Cause cause){
        super(false);
        this.cause = cause;
    }

    public Cause getCause(){
        return cause;
    }
    public String getCauseString(){
        return cause.toString();
    }
}
