package com.noza.zplitter.app.android.rest.response;

/**
 * Created by omiwrench on 2016-02-01.
 */
public abstract class DivisionResponse {
    protected final boolean success;

    protected DivisionResponse(boolean success){
        this.success = success;
    }

    public boolean wasSuccess(){
        return success;
    }
}
