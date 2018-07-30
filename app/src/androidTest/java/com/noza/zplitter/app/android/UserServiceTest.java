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
import com.noza.zplitter.app.android.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by omiwrench on 2016-02-24.
 */
@RunWith(AndroidJUnit4.class)
public class UserServiceTest extends InstrumentationTestCase{

    private User testUser;

    @Override
    @Before
    public void setUp() throws Exception{
        super.setUp();
        Profile testProfile = new Profile.Builder()
                                        .firstName("testFirstName")
                                        .lastName("testLastName")
                                        .age(18)
                                        .gender(Gender.MALE)
                                        .build();
        testUser = new User.Builder().id(1)
                                     .email("test@email.com")
                                     .profile(testProfile)
                                     .build();
        this.injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void saveUser_GoodUser_SavesUser(){
        UserService.saveCurrentUser(testUser, this.getInstrumentation().getTargetContext());

        assertSavedUserHasInt(R.string.user_id_key, testUser.getId());
        assertSavedUserHasString(R.string.user_email_key, testUser.getEmail());
        assertSavedUserHasString(R.string.user_first_name_key, testUser.getFirstName());
        assertSavedUserHasString(R.string.user_last_name_key, testUser.getLastName());
        assertSavedUserHasString(R.string.user_gender_key, testUser.getGenderAsString());
        assertSavedUserHasInt(R.string.user_age_key, testUser.getAge());
    }

    @Test
    public void getLoggedInUser_ReturnsUser(){
        Context context = this.getInstrumentation().getTargetContext();
        UserService.saveCurrentUser(testUser, context);

        try{
            User currentUser = UserService.getLoggedInUser(context);
            assertEquals(currentUser.getId(), testUser.getId());
            assertEquals(currentUser.getEmail(), testUser.getEmail());
            assertEquals(currentUser.getFirstName(), testUser.getFirstName());
            assertEquals(currentUser.getLastName(), testUser.getLastName());
            assertEquals(currentUser.getGender(), testUser.getGender());
            assertEquals(currentUser.getAge(), testUser.getAge());
        }
        catch(NoLoggedInUserException e){
            fail("No logged in user");
        }
    }

    @Test
    public void removeUser_RemovesUser(){
        Context context = this.getInstrumentation().getTargetContext();
        UserService.saveCurrentUser(UserTestData.GOOD_USER, context);

        UserService.removeCurrentUser(context);
        try{
            UserService.getLoggedInUser(context);
            fail("UserService did not throw NoLoggedInUserException");
        }
        catch(NoLoggedInUserException e){
            assert(true);
        }
    }

    private void assertSavedUserHasString(int keyId, String match){
        Context context = this.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_pref_file_key), Context.MODE_PRIVATE);
        String saved = sharedPreferences.getString(context.getString(keyId), null);
        assertEquals(match, saved);
    }
    private  void assertSavedUserHasInt(int keyId, int match){
        Context context = this.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_pref_file_key), Context.MODE_PRIVATE);
        int saved = sharedPreferences.getInt(context.getString(keyId), -1);
        assertEquals(match, saved);
    }
}
