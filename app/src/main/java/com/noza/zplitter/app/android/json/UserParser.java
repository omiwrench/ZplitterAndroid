package com.noza.zplitter.app.android.json;

import com.noza.zplitter.app.android.model.auth.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class UserParser {
    public static User toObject(String json) throws JSONException{
        JSONObject object = new JSONObject(json);
        return toObject(object);
    }
    public static User toObject(JSONObject json) throws JSONException{

        User user = new User.Builder()
                .email(json.getString("email"))
                .id(json.getInt("id"))
                .build();
        return user;
    }
}
