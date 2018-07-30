package com.noza.zplitter.app.android.rest.response.auth;

import com.noza.zplitter.app.android.rest.response.FailResponse;
import com.noza.zplitter.app.android.rest.response.failcause.Cause;
import com.noza.zplitter.app.android.rest.response.failcause.GeneralFailCause;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class RegistrationFailedResponse extends FailResponse<RegistrationFailedResponse.RegistrationFailCause> {

    public enum RegistrationFailCause implements Cause{
        MISSING_USER,
        MISSING_EMAIL,
        INVALID_EMAIL,
        EMAIL_TAKEN,
        MISSING_PASSWORD,
        PASSWORD_TOO_SHORT;
    }

    public RegistrationFailedResponse(String causeString){
        super(RegistrationFailCause.valueOf(causeString));
    }
    public RegistrationFailedResponse(Cause cause){
        super(cause);
    }
}
