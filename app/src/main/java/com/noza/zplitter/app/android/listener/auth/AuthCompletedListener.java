package com.noza.zplitter.app.android.listener.auth;

import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.FailResponse;
import com.noza.zplitter.app.android.rest.response.auth.AuthSuccessResponse;

/**
 * Created by omiwrench on 2016-01-30.
 */
public interface AuthCompletedListener<E extends FailResponse>{
    void onSuccess(AuthSuccessResponse response);
    void onFail(E response);
    void onError(ErrorResponse response);
}
