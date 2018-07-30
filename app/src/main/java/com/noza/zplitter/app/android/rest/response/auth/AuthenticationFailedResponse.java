package com.noza.zplitter.app.android.rest.response.auth;

import com.noza.zplitter.app.android.rest.response.failcause.Cause;
import com.noza.zplitter.app.android.rest.response.FailResponse;
import com.noza.zplitter.app.android.rest.response.failcause.GeneralFailCause;

/**
 * Created by omiwrench on 2016-03-03.
 */
public class AuthenticationFailedResponse extends FailResponse<AuthenticationFailedResponse.AuthenticationFailedCause> {

    public enum AuthenticationFailedCause implements Cause{
        MISSING_EMAIL,
        MISSING_PASSWORD,
        INVALID_CREDENTIALS;
    }

    public AuthenticationFailedResponse(String causeString){
        super(AuthenticationFailedCause.valueOf(causeString));
    }
    public AuthenticationFailedResponse(Cause cause){
        super(cause);
    }
}
