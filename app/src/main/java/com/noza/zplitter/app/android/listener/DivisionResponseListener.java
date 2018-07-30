package com.noza.zplitter.app.android.listener;

import com.android.volley.Response;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.FailResponse;

/**
 * Created by omiwrench on 2016-03-03.
 */
public class DivisionResponseListener<S extends DivisionResponse, F extends FailResponse<?>> implements Response.Listener<DivisionResponse>{

    private final RequestFinishedListener<S, F> callback;

    public DivisionResponseListener(RequestFinishedListener<S, F> callback){
        this.callback = callback;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onResponse(DivisionResponse response) {
        if (response.wasSuccess()) {
            callback.onSuccess((S) response);
        } else {
            callback.onFail((F) response);
        }
    }
}
