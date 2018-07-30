package com.noza.zplitter.app.android.rest.response.user;

import com.noza.zplitter.app.android.rest.response.FailResponse;
import com.noza.zplitter.app.android.rest.response.failcause.Cause;
import com.noza.zplitter.app.android.rest.response.failcause.GeneralFailCause;

/**
 * Created by omiwrench on 2016-03-02.
 */
public class ProfileCreationFailedResponse extends FailResponse<ProfileCreationFailedResponse.ProfileCreationFailCause>{
    public enum ProfileCreationFailCause implements Cause{
        MISSING_TOKEN,
        INVALID_TOKEN,
        MISSING_FIRST_NAME,
        MISSING_LAST_NAME,
        MISSING_GENDER,
        INVALID_GENDER,
        MISSING_AGE,
        AGE_TOO_YOUNG,
        AGE_TOO_OLD,
        UNKNOWN
    }

    public ProfileCreationFailedResponse(String causeString){
        super(ProfileCreationFailCause.valueOf(causeString));
    }
    public ProfileCreationFailedResponse(Cause cause){
        super(cause);
    }
}
