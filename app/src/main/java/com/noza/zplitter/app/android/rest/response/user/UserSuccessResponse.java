package com.noza.zplitter.app.android.rest.response.user;

import com.noza.zplitter.app.android.json.ProfileParser;
import com.noza.zplitter.app.android.json.UserParser;
import com.noza.zplitter.app.android.model.Profile;
import com.noza.zplitter.app.android.model.auth.User;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by omiwrench on 2016-03-02.
 */
public class UserSuccessResponse extends DivisionResponse {
    private final User user;

    public UserSuccessResponse(JSONObject response) throws JSONException {
        super(true);

        JSONObject userJSON = response.getJSONObject("user");
        this.user = UserParser.toObject(userJSON);

        if(userJSON.has("profile")){
            Profile profile = ProfileParser.toObject(userJSON.getJSONObject("profile"));
            user.setProfile(profile);
        }
    }
    public UserSuccessResponse(User user){
        super(true);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}