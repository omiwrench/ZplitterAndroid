package com.noza.zplitter.app.android.json;

import com.noza.zplitter.app.android.model.Profile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by omiwrench on 2016-02-18.
 */
public class ProfileParser {
    public static Profile toObject(String jsonProfile) throws JSONException{
        JSONObject json = new JSONObject(jsonProfile);
        return toObject(json);
    }
    public static Profile toObject(JSONObject json) throws JSONException{

        Profile profile = new Profile.Builder()
                                     .firstName(json.getString("firstName"))
                                     .lastName(json.getString("lastName"))
                                     .age(json.getInt("age"))
                                     .gender(json.getString("gender"))
                                     .build();
        return profile;
    }
}
