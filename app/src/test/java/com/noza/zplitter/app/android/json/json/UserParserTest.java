package com.noza.zplitter.app.android.json.json;

import com.noza.zplitter.app.android.json.UserParser;
import com.noza.zplitter.app.android.model.auth.User;

import org.json.JSONException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by omiwrench on 2016-02-17.
 */
public class UserParserTest {
    private static final String TAG = UserParserTest.class.getName();

    @Test
    public void userParser_CorrectJson_ReturnsCorrect(){
        String validUser =
                "{\"user\":{" +
                    "\"email\": \"felix@zplitter.se\", " +
                    "\"id\": 12345" +
                "}}";
        try{
            User user = UserParser.toObject(validUser);
            assertEquals(user.getEmail(), "felix@zplitter.se");
            assertEquals(user.getId(), 12345);
        }
        catch(JSONException e){
            assert(false);
        }
    }
    @Test
    public void userParser_InvalidJson_ThrowsException(){
        String invalidUser = "{invalid: json}";
        try{
            User user = UserParser.toObject(invalidUser);
            assert(false);
        }
        catch(JSONException e){
            assert(true);
        }
    }
}
