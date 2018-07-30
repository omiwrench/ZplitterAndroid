package com.noza.zplitter.app.android.rest.response.auth;

import com.noza.zplitter.app.android.model.auth.User;
import com.noza.zplitter.app.android.rest.response.user.UserSuccessResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class AuthSuccessResponse extends UserSuccessResponse {
    private final String token;
    private final boolean created;

    public AuthSuccessResponse(JSONObject response) throws JSONException{
        super(response);

        token = response.getString("token");
        created = response.getBoolean("created");
    }
    public AuthSuccessResponse(User user, String token, boolean created){
        super(user);
        this.token = token;
        this.created = created;
    }

    public String getToken(){
        return token;
    }
    public boolean wasCreated(){
        return created;
    }
}
