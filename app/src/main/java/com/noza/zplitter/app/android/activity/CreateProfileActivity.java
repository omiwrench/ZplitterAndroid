package com.noza.zplitter.app.android.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.adapter.LinkedHashMapAdapter;
import com.noza.zplitter.app.android.helper.InternetHelper;
import com.noza.zplitter.app.android.helper.SnackbarHelper;
import com.noza.zplitter.app.android.helper.TextHelper;
import com.noza.zplitter.app.android.listener.AvatarSavedListener;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.model.Gender;
import com.noza.zplitter.app.android.model.Profile;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.failcause.Cause;
import com.noza.zplitter.app.android.rest.response.failcause.GeneralFailCause;
import com.noza.zplitter.app.android.rest.response.user.ProfileCreationFailedResponse;
import com.noza.zplitter.app.android.rest.response.user.UserSuccessResponse;
import com.noza.zplitter.app.android.service.AvatarService;
import com.noza.zplitter.app.android.service.UserService;
import com.orhanobut.logger.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omiwrench on 2016-02-22.
 */
public class CreateProfileActivity extends AppCompatActivity{

    private final int SELECT_PHOTO = 1;

    private Toolbar toolbar;
    private ViewGroup avatarContainer;
    private ImageView avatar;
    private View avatarEditButton;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText ageInput;
    private Spinner genderSpinner;
    private ViewGroup inputsContainer;
    private TextView loadingText;

    private TextView agePrefix;

    private Bitmap selectedAvatar;

    private Dialog loadingDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Logger.init();

        bindViews();
        setupToolbar();
        setupSpinner();
        bindListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK){
            finishImagePick(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            sendProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        returnToLogin();
    }

    private void bindViews(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.avatarContainer = (ViewGroup) findViewById(R.id.avatar_container);
        this.avatar = (ImageView) findViewById(R.id.avatar);
        this.avatarEditButton = findViewById(R.id.avatar_edit_button);
        this.firstNameInput = (EditText) findViewById(R.id.input_firstname);
        this.lastNameInput = (EditText) findViewById(R.id.input_lastname);
        this.ageInput = (EditText) findViewById(R.id.input_age);
        this.genderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        this.agePrefix = (TextView) findViewById(R.id.age_prefix);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.inputsContainer = (ViewGroup) findViewById(R.id.inputs_container);
        this.loadingText = (TextView) findViewById(R.id.text_loading);
    }
    private void bindListeners(){
        this.ageInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                checkVowel();
            }
        });
        this.avatarContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }
    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToLogin();
            }
        });
    }
    private void setupSpinner(){
        LinkedHashMap<Gender, String> map = new LinkedHashMap<>();
        map.put(Gender.MALE, getResources().getString(R.string.gender_spinner_male));
        map.put(Gender.FEMALE, getResources().getString(R.string.gender_spinner_female));
        map.put(Gender.OTHER, getResources().getString(R.string.gender_spinner_other));

        LinkedHashMapAdapter<Gender, String> adapter = new LinkedHashMapAdapter<>(this, android.R.layout.simple_dropdown_item_1line, map);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.genderSpinner.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    private void sendProfile(){
        final long baseTime = System.nanoTime();

        if(!validateInputs()){
            return;
        }
        if(!InternetHelper.isConnectedToInternet(this)){
            SnackbarHelper.showErrorSnackbar(this, findViewById(android.R.id.content), R.string.error_general_no_internet);
            return;
        }
        setStateLoading();

        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        Gender gender = ((Map.Entry<Gender, String>) genderSpinner.getSelectedItem()).getKey();
        int age = Integer.parseInt(ageInput.getText().toString());

        Profile profile = new Profile.Builder()
                                     .firstName(firstName)
                                     .lastName(lastName)
                                     .gender(gender)
                                     .age(age)
                                     .build();
        UserService.setUserProfile(profile, this, new RequestFinishedListener<UserSuccessResponse, ProfileCreationFailedResponse>() {
            @Override
            public void onSuccess(UserSuccessResponse response) {
                Logger.d("profile set took " + (System.nanoTime() - baseTime)/1000000 + " ms");
                uploadAvatar();
            }

            @Override
            public void onFail(ProfileCreationFailedResponse response) {
                Cause cause = response.getCause();
                if (cause instanceof ProfileCreationFailedResponse.ProfileCreationFailCause) {
                    switch ((ProfileCreationFailedResponse.ProfileCreationFailCause) cause) {
                        case AGE_TOO_OLD:
                            showErrorSnackbar(R.string.error_createprofile_age_too_old);
                            break;
                        case AGE_TOO_YOUNG:
                            showErrorSnackbar(R.string.error_createprofile_age_too_young);
                            break;
                        case MISSING_TOKEN:
                        case INVALID_TOKEN:
                            Intent intent = new Intent(CreateProfileActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            break;
                        case UNKNOWN:
                            showErrorSnackbar(R.string.error_general_unknown);
                    }
                } else if (cause instanceof GeneralFailCause) {
                    SnackbarHelper.showUnknownErrorSnackbar(CreateProfileActivity.this, findViewById(android.R.id.content));
                }
                setStateNormal();
            }

            @Override
            public void onError(ErrorResponse response) {
                showErrorSnackbar(R.string.error_general_unknown);
                setStateNormal();
            }
        });
    }
    private void uploadAvatar(){
        if(selectedAvatar != null){
            AvatarService.uploadAvatar(selectedAvatar, this, new AvatarSavedListener() {
                @Override
                public void avatarSaved(Bitmap avatar) {
                    startMainActivity();
                }
            });
        }
    }

    private void openImagePicker(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }
    private void finishImagePick(Intent intent){
        try{
            Uri imageUri = intent.getData();
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            selectedAvatar = selectedImage;
            this.avatar.setImageBitmap(selectedImage);
        }
        catch(FileNotFoundException e){
            showErrorSnackbar(R.string.error_createprofile_avatar_error);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStateLoading(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            inputsContainer.setTransitionGroup(true);
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.transition_activity_create_profile_loading);
            TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.root), transition);
            inputsContainer.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
            avatarEditButton.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.VISIBLE);
        }
        else{
            loadingDialog = ProgressDialog.show(
                                this,
                                getString(R.string.dialog_profile_create_loading_title),
                                getString(R.string.dialog_profile_create_loading_message),
                                true);
        }

    }
    private void setStateNormal(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.transition_activity_create_profile_loading_cancelled);
            TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.root), transition);
            inputsContainer.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            avatarEditButton.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.GONE);
        }
        else{
            loadingDialog.dismiss();
        }
    }

    private boolean validateInputs(){
        if(firstNameInput.getText().toString().equalsIgnoreCase("")){
            showErrorSnackbar(R.string.error_createprofile_firstname_missing);
            return false;
        }
        else if(lastNameInput.getText().toString().equalsIgnoreCase("")){
            showErrorSnackbar(R.string.error_createprofile_lastname_missing);
            return false;
        }
        else if(ageInput.getText().toString().equalsIgnoreCase("")){
            showErrorSnackbar(R.string.error_createprofile_age_missing);
            return false;
        }
        return true;
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void showAvatarFailedDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_profile_create_avatar_failed_title)
                .setMessage(R.string.dialog_profile_create_avatar_failed_message)
                .setPositiveButton(R.string.dialog_profile_create_cancel_button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startMainActivity();
                    }
                })
                .setNegativeButton(R.string.dialog_profile_create_avatar_button_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadAvatar();
                    }
                }).create();
        dialog.show();
    }
    private void returnToLogin(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                                            .setTitle(R.string.dialog_profile_create_cancel_title)
                                            .setMessage(R.string.dialog_profile_create_cancel_message)
                                            .setPositiveButton(R.string.dialog_profile_create_cancel_button_positive, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(CreateProfileActivity.this, LoginActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            })
                                            .setNegativeButton(R.string.dialog_profile_create_cancel_button_negative, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create();
        dialog.show();
    }

    private void checkVowel(){
        try{
            int age = Integer.valueOf(ageInput.getText().toString());
            if(TextHelper.startsWithVowel(age)){
                agePrefix.setText(R.string.profile_create_age_prefix_vowel);
            }
            else{
                agePrefix.setText(R.string.profile_create_age_prefix);
            }
        }
        catch(NumberFormatException e){
            agePrefix.setText(R.string.profile_create_age_prefix);
        }
    }

    private void showErrorSnackbar(int messageId){
        SnackbarHelper.showErrorSnackbar(CreateProfileActivity.this, findViewById(android.R.id.content), messageId);
    }
}
