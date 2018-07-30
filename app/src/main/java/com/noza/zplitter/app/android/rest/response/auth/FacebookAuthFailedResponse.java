package com.noza.zplitter.app.android.rest.response.auth;

import com.noza.zplitter.app.android.rest.response.FailResponse;
import com.noza.zplitter.app.android.rest.response.failcause.Cause;
import com.noza.zplitter.app.android.rest.response.failcause.GeneralFailCause;

/**
 * Created by omiwrench on 2016-02-17.
 */
public class FacebookAuthFailedResponse extends FailResponse<FacebookAuthFailedResponse.FacebookAuthFailCause> {

    public enum FacebookAuthFailCause implements Cause{
        EMAIL_TAKEN,
        MISSING_FACEBOOK_ID,
        INVALID_FACEBOOK_ID,
        MISSING_FACEBOOK_TOKEN,
        INVALID_FACEBOOK_TOKEN;
    }

    public FacebookAuthFailedResponse(String causeString){
        super(GeneralFailCause.UNKNOWN);
    }
    public FacebookAuthFailedResponse(Cause cause){
        super(cause);
    }

    public Cause getCause(){
        return cause;
    }
    public String getCauseString(){
        return cause.toString();
    }
}
