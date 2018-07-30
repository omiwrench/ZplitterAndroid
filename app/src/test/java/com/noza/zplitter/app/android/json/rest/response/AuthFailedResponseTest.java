package com.noza.zplitter.app.android.json.rest.response;

import com.noza.zplitter.app.android.rest.response.auth.RegistrationFailedResponse;

import org.junit.Test;

import static com.noza.zplitter.app.android.rest.response.auth.RegistrationFailedResponse.Cause;
import static org.junit.Assert.assertEquals;

/**
 * Created by omiwrench on 2016-02-17.
 */
public class AuthFailedResponseTest {

    @Test
    public void authFailedResponse_CorrectErrors_Returns(){
        RegistrationFailedResponse response = new RegistrationFailedResponse("INVALID_EMAIL");
        assertEquals("works for MISSING_FACEBOOK_ID", response.getCause(), Cause.INVALID_EMAIL);

        response = new RegistrationFailedResponse("USER_MISSING");
        assertEquals("works for USER_MISSING", response.getCause(), Cause.USER_MISSING);

        response = new RegistrationFailedResponse("EMAIL_MISSING");
        assertEquals("works for EMAIL_MISSING", response.getCause(), Cause.EMAIL_MISSING);

        response = new RegistrationFailedResponse("EMAIL_MISSING");
        assertEquals("works for EMAIL_MISSING", response.getCause(), Cause.EMAIL_MISSING);

        response = new RegistrationFailedResponse("EMAIL_TAKEN");
        assertEquals("works for EMAIL_TAKEN", response.getCause(), Cause.EMAIL_TAKEN);

        response = new RegistrationFailedResponse("PASSWORD_MISSING");
        assertEquals("works for PASSWORD_MISSING", response.getCause(), Cause.PASSWORD_MISSING);

        response = new RegistrationFailedResponse("PASSWORD_TOO_SHORT");
        assertEquals("works for PASSWORD_TOO_SHORT", response.getCause(), Cause.PASSWORD_TOO_SHORT);
    }
    @Test
    public void authFailedResponse_UnknownError_ReturnsUnknown(){
        RegistrationFailedResponse response = new RegistrationFailedResponse("TEAPOT_TOO_COLD");
        assertEquals("returns UNKNOWN on unknown error", response.getCause(), Cause.UNKNOWN);
    }
}
