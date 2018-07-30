package com.noza.zplitter.app.android.rest.request.user;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.constant.HttpStatus;
import com.noza.zplitter.app.android.exception.auth.RequestFailedError;
import com.noza.zplitter.app.android.model.Profile;
import com.noza.zplitter.app.android.rest.request.AuthenticatedPostRequest;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.user.ProfileCreationFailedResponse;
import com.noza.zplitter.app.android.rest.response.user.UserSuccessResponse;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by omiwrench on 2016-03-02.
 */
public class SetProfileRequest extends AuthenticatedPostRequest{

    public SetProfileRequest(Context context,
                             Profile profile,
                             String token,
                             Response.Listener<DivisionResponse> listener,
                             Response.ErrorListener errorListener){
        super(context.getString(R.string.url_set_profile), token, listener, errorListener);
        try{
            JSONObject profileJSON = new JSONObject();
            profileJSON.put("firstName", profile.getFirstName());
            profileJSON.put("lastName", profile.getLastName());
            profileJSON.put("gender", profile.getGender().getAsString());
            profileJSON.put("age", profile.getAge());
            this.body.put("profile", profileJSON);
        }
        catch(JSONException e){
            Logger.e("Could not parse profile into json body.");
            throw new IllegalArgumentException("Invalid profile");
        }
    }

    @Override
    public void deliverError(VolleyError error){
        if(error instanceof RequestFailedError){
            RequestFailedError failedError = (RequestFailedError) error;
            listener.onResponse(new ProfileCreationFailedResponse(failedError.getFailCause()));
        }
        else{
            super.deliverError(error);
        }
    }
    @Override
    protected Response<DivisionResponse> parseNetworkResponse(NetworkResponse rawResponse){
        try{
            if(rawResponse.statusCode == HttpStatus.CREATED){
                UserSuccessResponse response = new UserSuccessResponse(getResponseJson(rawResponse));
                return Response.success((DivisionResponse) response, HttpHeaderParser.parseCacheHeaders(rawResponse));
            }
            return super.parseNetworkResponse(rawResponse);
        }
        catch(Exception e){
            Logger.e(e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError error){
        if(error.networkResponse.statusCode == HttpStatus.BAD_REQUEST){
            return super.requestFailed(error);
        }
        return super.parseNetworkError(error);
    }
}
