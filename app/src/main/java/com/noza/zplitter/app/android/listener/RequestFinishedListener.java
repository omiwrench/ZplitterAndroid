package com.noza.zplitter.app.android.listener;

import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;

/**
 * Created by omiwrench on 2016-03-03.
 */
public interface RequestFinishedListener<S extends DivisionResponse, F extends DivisionResponse> {
    void onSuccess(S response);
    void onFail(F response);
    void onError(ErrorResponse response);
}
