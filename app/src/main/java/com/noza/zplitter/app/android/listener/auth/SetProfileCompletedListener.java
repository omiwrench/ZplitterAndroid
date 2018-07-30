package com.noza.zplitter.app.android.listener.auth;

import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.FailResponse;
import com.noza.zplitter.app.android.rest.response.user.UserSuccessResponse;

/**
 * Created by omiwrench on 2016-03-02.
 */
public interface SetProfileCompletedListener<E extends FailResponse> {
    void onSuccess(UserSuccessResponse response);
    void onFail(E response);
    void onError(ErrorResponse response);
}
