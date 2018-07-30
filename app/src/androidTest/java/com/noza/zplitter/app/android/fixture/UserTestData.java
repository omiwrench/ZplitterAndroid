package com.noza.zplitter.app.android.fixture;

import com.noza.zplitter.app.android.model.Gender;
import com.noza.zplitter.app.android.model.Profile;
import com.noza.zplitter.app.android.model.auth.User;

/**
 * Created by omiwrench on 2016-03-05.
 */
public class UserTestData {
    public static final User GOOD_USER;
    public static final Profile GOOD_PROFILE;
    static{
        GOOD_PROFILE = new Profile.Builder()
                                    .firstName("Felix")
                                    .lastName("Andersson")
                                    .gender(Gender.MALE)
                                    .age(18)
                                    .build();
        GOOD_USER = new User.Builder().email("test@test.com").id(1).profile(GOOD_PROFILE).build();
    }
}
