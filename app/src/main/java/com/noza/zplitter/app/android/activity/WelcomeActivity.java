package com.noza.zplitter.app.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.adapter.MockViewFragmentPageAdapter;
import com.noza.zplitter.app.android.model.auth.User;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by omiwrench on 2016-02-18.
 */
public class WelcomeActivity extends AppCompatActivity{

    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private TextView age;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Logger.init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("You");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        String url = "http://dev.zplitter.se/api/profile/get/avatar/fullsize?userId=" + user.getId();
        Logger.d(url);
        new DownloadAvatarTask((ImageView) findViewById(R.id.avatar)).execute(url);

        bindViews();
        populateViews(user);
    }

    private void bindViews(){
        firstName = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        gender = (TextView) findViewById(R.id.gender);
        age = (TextView) findViewById(R.id.age);

        ViewPager pager = (ViewPager) findViewById(R.id.mock_pager);
        pager.setAdapter(new MockViewFragmentPageAdapter(getSupportFragmentManager(), this));
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
    }
    private void populateViews(User user){
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        gender.setText(user.getGenderAsString());
        age.setText(user.getAge() + " years old");
    }

    private class DownloadAvatarTask extends AsyncTask<String, Void, Bitmap>{
        private ImageView avatarView;
        public DownloadAvatarTask(ImageView avatarView){
            this.avatarView = avatarView;
        }
        protected Bitmap doInBackground(String... urls){
            String url = urls[0];
            Bitmap avatar = null;
            try{
                InputStream in = new URL(url).openStream();
                avatar = BitmapFactory.decodeStream(in);
            }
            catch(MalformedURLException e){
                Logger.e("Malformed url: " + url);
            }
            catch(IOException e){
                Logger.e("IOException: " + e.getMessage());
            }
            return avatar;
        }

        protected void onPostExecute(Bitmap bitmap){
            avatarView.setImageBitmap(bitmap);
        }
    }
}
