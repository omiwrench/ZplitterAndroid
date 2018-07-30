package com.noza.zplitter.app.android.exception.auth;

import com.android.volley.VolleyError;

/**
 * Created by omiwrench on 2016-01-31.
 */
public class EmailTakenError extends VolleyError{

    public EmailTakenError(){
        super("Email address already taken");
    }
    public EmailTakenError(String email){
        super(email + " address already taken");
    }
}
