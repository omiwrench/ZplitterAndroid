package com.noza.zplitter.app.android.provider.user;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.listener.auth.SetProfileCompletedListener;
import com.noza.zplitter.app.android.model.Profile;
import com.noza.zplitter.app.android.rest.AvatarUploadRequest;
import com.noza.zplitter.app.android.rest.request.user.SetProfileRequest;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.SuccessResponse;
import com.noza.zplitter.app.android.rest.response.user.AvatarUploadFailedResponse;
import com.noza.zplitter.app.android.rest.response.user.ProfileCreationFailedResponse;
import com.noza.zplitter.app.android.rest.response.user.UserSuccessResponse;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by omiwrench on 2016-03-02.
 */
public class DivisionProfileProvider {
    private final Context context;

    public DivisionProfileProvider(Context context){
        this.context = context;
    }

    public void setProfile(String token, Profile profile, final SetProfileCompletedListener<ProfileCreationFailedResponse> callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        SetProfileRequest request = new SetProfileRequest(context, profile, token, new Response.Listener<DivisionResponse>() {
            @Override
            public void onResponse(DivisionResponse response) {
                if(response.wasSuccess()){
                    callback.onSuccess((UserSuccessResponse) response);
                }
                else{
                    callback.onFail((ProfileCreationFailedResponse) response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(new ErrorResponse(error.getMessage(), error.networkResponse.statusCode));
            }
        });

        request.setShouldCache(false);
        queue.add(request);
    }
    public void uploadAvatar(String token, Bitmap image, final RequestFinishedListener<SuccessResponse, AvatarUploadFailedResponse> callback){
        try{
            File avatarFile = getFileFromBitmap(image);
            RequestQueue queue = Volley.newRequestQueue(context);
            AvatarUploadRequest request = new AvatarUploadRequest(context, token, avatarFile, new Response.Listener<DivisionResponse>() {
                @Override
                public void onResponse(DivisionResponse response) {
                    if(response.wasSuccess()){
                        callback.onSuccess((SuccessResponse) response);
                    }
                    else{
                        callback.onFail((AvatarUploadFailedResponse) response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new ErrorResponse(error.getMessage(), error.networkResponse.statusCode);
                }
            });

            request.setShouldCache(false);
            queue.add(request);
        }
        catch(IOException e){
            callback.onError(new ErrorResponse("Could not write bitmap to file for upload."));
        }
    }

    private File getFileFromBitmap(Bitmap bitmap) throws IOException{
        File f = new File(context.getCacheDir(), "com.noza.zplitter.app.android-temp.avatar");
        f.createNewFile();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        return f;
    }
}
