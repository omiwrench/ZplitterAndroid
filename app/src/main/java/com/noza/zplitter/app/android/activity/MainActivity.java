package com.noza.zplitter.app.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.exception.auth.NoLoggedInUserException;
import com.noza.zplitter.app.android.listener.AvatarChangedListener;
import com.noza.zplitter.app.android.model.auth.User;
import com.noza.zplitter.app.android.service.AuthService;
import com.noza.zplitter.app.android.service.AvatarService;
import com.noza.zplitter.app.android.service.UserService;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AvatarChangedListener{

    User user;

    private NavigationView navigationView;
    private TextView sidebarUserName;
    private TextView sidebarUserCity;
    private ImageView avatarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init();

        try{
            user = UserService.getLoggedInUser(this);
            if(!user.hasProfile()){
                logout();
                return;
            }
            setupViews();
            bindViews();
            populateSidebar();

            AvatarService.addAvatarChangedListener(this);
        }
        catch(NoLoggedInUserException e){
            logout();
        }
    }

    private void setupViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void bindViews(){
        sidebarUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.sidebar_username);
        sidebarUserCity = (TextView) navigationView.getHeaderView(0).findViewById(R.id.sidebar_city);
        avatarView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
    }
    private void populateSidebar(){
        User loggedInUser = UserService.getLoggedInUser(this);
        sidebarUserName.setText(loggedInUser.getFullName());

        Bitmap avatar = AvatarService.getUserAvatar(this);
        if(avatar == null){
            AvatarService.updateUserAvatar(this);
        }
        else{
            avatarView.setImageBitmap(avatar);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAvatarChanged(final Bitmap newAvatar){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                avatarView.setImageBitmap(newAvatar);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            logout();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        AuthService.logout(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
