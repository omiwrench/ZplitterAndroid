package com.noza.zplitter.app.android.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.exception.auth.NoLoggedInUserException;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.listener.auth.SetProfileCompletedListener;
import com.noza.zplitter.app.android.model.Profile;
import com.noza.zplitter.app.android.model.auth.User;
import com.noza.zplitter.app.android.provider.user.DivisionProfileProvider;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.SuccessResponse;
import com.noza.zplitter.app.android.rest.response.user.AvatarUploadFailedResponse;
import com.noza.zplitter.app.android.rest.response.user.ProfileCreationFailedResponse;
import com.noza.zplitter.app.android.rest.response.user.UserSuccessResponse;
import com.orhanobut.logger.Logger;

/**
 * Created by omiwrench on 2016-02-24.
 */
public class UserService {

    public static void setUserProfile(Profile profile, final Context context, final RequestFinishedListener<UserSuccessResponse, ProfileCreationFailedResponse> callback){
        DivisionProfileProvider provider = new DivisionProfileProvider(context);
        provider.setProfile(AuthService.getToken(context), profile, new SetProfileCompletedListener<ProfileCreationFailedResponse>() {
            @Override
            public void onSuccess(UserSuccessResponse response) {
                UserService.saveCurrentUser(response.getUser(), context);
                callback.onSuccess(response);
            }
            @Override
            public void onFail(ProfileCreationFailedResponse response) {
                callback.onFail(response);
            }
            @Override
            public void onError(ErrorResponse response) {
                callback.onError(response);
            }
        });
    }

    public static User getLoggedInUser(Context context) throws NoLoggedInUserException{
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_pref_file_key), Context.MODE_PRIVATE);

        if(!AuthService.isUserLoggedIn(context) ||
           !sharedPreferences.contains(context.getString(R.string.user_first_name_key))){
            throw new NoLoggedInUserException();
        }

        Profile profile = new Profile.Builder()
                                     .firstName(sharedPreferences.getString(context.getString(R.string.user_first_name_key), null))
                                     .lastName(sharedPreferences.getString(context.getString(R.string.user_last_name_key), null))
                                     .gender(sharedPreferences.getString(context.getString(R.string.user_gender_key), null))
                                     .age(sharedPreferences.getInt(context.getString(R.string.user_age_key), -1))
                                     .build();
        User user = new User.Builder()
                            .id(sharedPreferences.getInt(context.getString(R.string.user_id_key), -1))
                            .email(sharedPreferences.getString(context.getString(R.string.user_email_key), null))
                            .profile(profile)
                            .build();
        return user;
    }
    public static void saveCurrentUser(User currentUser, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(context.getString(R.string.user_id_key), currentUser.getId());
        editor.putString(context.getString(R.string.user_email_key), currentUser.getEmail());
        if(currentUser.hasProfile()){
            editor.putString(context.getString(R.string.user_first_name_key), currentUser.getFirstName());
            editor.putString(context.getString(R.string.user_last_name_key), currentUser.getLastName());
            editor.putString(context.getString(R.string.user_gender_key), currentUser.getGenderAsString());
            editor.putInt(context.getString(R.string.user_age_key), currentUser.getAge());
        }

        editor.apply();
    }
    public static void removeCurrentUser(Context context){
        SharedPreferences sharedPreferences = getSharedPrefs(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(context.getString(R.string.user_id_key));
        editor.remove(context.getString(R.string.user_email_key));
        editor.remove(context.getString(R.string.user_first_name_key));
        editor.remove(context.getString(R.string.user_last_name_key));
        editor.remove(context.getString(R.string.user_gender_key));
        editor.remove(context.getString(R.string.user_age_key));

        editor.apply();
    }

    private static boolean userIsValid(User user){
        return user.getId() > 0 &&
                user.getEmail() != null &&
                user.getFirstName() != null &&
                user.getLastName() != null &&
                user.getGender() != null &&
                user.getAge() > 0;
    }

    private static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences(context.getString(R.string.user_pref_file_key), Context.MODE_PRIVATE);
    }
}
