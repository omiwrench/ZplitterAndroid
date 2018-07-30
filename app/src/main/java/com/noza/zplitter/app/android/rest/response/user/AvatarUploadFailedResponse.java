package com.noza.zplitter.app.android.rest.response.user;

import com.noza.zplitter.app.android.rest.response.FailResponse;
import com.noza.zplitter.app.android.rest.response.failcause.Cause;

/**
 * Created by omiwrench on 2016-03-08.
 */
public class AvatarUploadFailedResponse extends FailResponse<AvatarUploadFailedResponse.AvatarUploadFailedCause>{
    public enum AvatarUploadFailedCause implements Cause {
        AVATAR_MISSING,
        FILE_TOO_LARGE;
    }

    public AvatarUploadFailedResponse(String causeString){
        super(AvatarUploadFailedCause.valueOf(causeString));
    }
    public AvatarUploadFailedResponse(Cause cause){
        super(cause);
    }
}
