package com.noza.zplitter.app.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.noza.zplitter.app.android.service.AuthService;

/**
 * Created by omiwrench on 2016-03-05.
 */
public class LaunchActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(AuthService.isUserLoggedIn(this)){
            startActivity(MainActivity.class);
        }
        else{
            startActivity(LoginActivity.class);
        }
    }
    private void startActivity(Class<?> activityClass){
        Intent intent = new Intent(this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
