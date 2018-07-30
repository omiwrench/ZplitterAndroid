package com.noza.zplitter.app.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.noza.zplitter.app.android.exception.auth.NoLoggedInUserException;
import com.noza.zplitter.app.android.fixture.UserTestData;
import com.noza.zplitter.app.android.model.Gender;
import com.noza.zplitter.app.android.model.Profile;
import com.noza.zplitter.app.android.model.auth.User;
import com.noza.zplitter.app.android.service.AuthService;
import com.noza.zplitter.app.android.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by omiwrench on 2016-02-24.
 */
@RunWith(AndroidJUnit4.class)
public class AuthServiceTest extends InstrumentationTestCase{
    @Override
    @Before
    public void setUp() throws Exception{
        super.setUp();
        this.injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void saveToken_SavesToken(){
        Context context = this.getInstrumentation().getTargetContext();
        String token = "abcdefghijklmnopqrstuvxyzåäö";

        AuthService.saveToken(token, context);
        String savedToken = AuthService.getToken(context);
        assertEquals(savedToken, token);
    }

    @Test
    public void logout_ClearsAllData(){

    }
}
