package com.noza.zplitter.app.android.rest.response;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class ErrorResponse extends DivisionResponse {
    private final String message;
    private final int responseCode;

    public ErrorResponse(String message){
        this(message, -1);
    }
    public ErrorResponse(String message, int responseCode){
        super(false);
        this.responseCode = responseCode;
        this.message = message;
    }

    public int getResponseCode(){
        return this.responseCode;
    }
    public String getMessage(){
        return this.message;
    }
}
