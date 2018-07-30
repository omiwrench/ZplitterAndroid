package com.noza.zplitter.app.android.json.rest.response;

import com.noza.zplitter.app.android.rest.response.auth.FacebookAuthFailedResponse;

import org.junit.Test;

import static com.noza.zplitter.app.android.rest.response.auth.FacebookAuthFailedResponse.Cause;
import static org.junit.Assert.assertEquals;

/**
 * Created by omiwrench on 2016-02-17.
 */
public class FacebookAuthFailedResponseTest {

    @Test
    public void facebookAuthFailedResponse_CorrectErrors_Returns(){
        FacebookAuthFailedResponse response = new FacebookAuthFailedResponse("MISSING_FACEBOOK_ID");
        assertEquals("works for MISSING_FACEBOOK_ID", response.getCause(), Cause.MISSING_FACEBOOK_ID);

        response = new FacebookAuthFailedResponse("MISSING_FACEBOOK_TOKEN");
        assertEquals("works for MISSING_FACEBOOK_TOKEN", response.getCause(), Cause.MISSING_FACEBOOK_TOKEN);

        response = new FacebookAuthFailedResponse("INVALID_FACEBOOK_ID");
        assertEquals("works for INVALID_FACEBOOK_ID", response.getCause(), Cause.INVALID_FACEBOOK_ID);

        response = new FacebookAuthFailedResponse("INVALID_FACEBOOK_TOKEN");
        assertEquals("works for INVALID_FACEBOOK_TOKEN", response.getCause(), Cause.INVALID_FACEBOOK_TOKEN);
    }
    @Test
    public void facebookAuthFailedResponse_UnknownError_ReturnsUnknown(){
        FacebookAuthFailedResponse response = new FacebookAuthFailedResponse("NOT_CEO_BITCH");
        assertEquals("returns UNKNOWN on unknown error", response.getCause(), Cause.UNKNOWN);
    }
}
