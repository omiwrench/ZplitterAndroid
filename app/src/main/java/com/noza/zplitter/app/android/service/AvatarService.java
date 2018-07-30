package com.noza.zplitter.app.android.service;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.helper.BitmapHelper;
import com.noza.zplitter.app.android.listener.AvatarChangedListener;
import com.noza.zplitter.app.android.listener.AvatarSavedListener;
import com.noza.zplitter.app.android.listener.RequestFinishedListener;
import com.noza.zplitter.app.android.model.auth.User;
import com.noza.zplitter.app.android.provider.user.DivisionProfileProvider;
import com.noza.zplitter.app.android.rest.response.ErrorResponse;
import com.noza.zplitter.app.android.rest.response.SuccessResponse;
import com.noza.zplitter.app.android.rest.response.user.AvatarUploadFailedResponse;
import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omiwrench on 2016-03-08.
 */
public class AvatarService {

    private static final float MAX_AVATAR_SIZE = 2000; //px

    private static final String FILE_NAME = "avatar.jpg";
    private static final List<AvatarChangedListener> avatarChangedListeners = new ArrayList<>();

    public static void saveToStorage(Context context, Bitmap avatar) throws IOException{
        File path=new File(getAvatarDir(context), FILE_NAME);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            avatar.compress(Bitmap.CompressFormat.PNG, 100, fos);
            updateListeners(avatar);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
    }
    public static Bitmap getUserAvatar(Context context){
        try {
            File f=new File(getAvatarDir(context), FILE_NAME);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            return null;
        }
    }
    public static void updateUserAvatar(Context context){
        DownloadAvatarTask downloadTask = new DownloadAvatarTask(context, UserService.getLoggedInUser(context).getId());
        downloadTask.execute();
    }

    public static void addAvatarChangedListener(AvatarChangedListener listener){
        avatarChangedListeners.add(listener);
    }
    private static void updateListeners(Bitmap newAvatar){
        for(AvatarChangedListener listener : avatarChangedListeners){
            listener.onAvatarChanged(newAvatar);
        }
    }

    public static void uploadAvatar(final Bitmap avatar, final Context context){
        uploadAvatar(avatar, context, null);
    }
    public static void uploadAvatar(final Bitmap avatar, final Context context, final AvatarSavedListener onSavedListener){
        uploadAvatar(avatar, context, onSavedListener, null);
    }
    public static void uploadAvatar(final Bitmap avatar, final Context context, final AvatarSavedListener onSavedListener, RequestFinishedListener<SuccessResponse, AvatarUploadFailedResponse> listener){
        Bitmap finalAvatar = BitmapHelper.scaleDownTo(avatar, MAX_AVATAR_SIZE, true);
        SaveAvatarTask saveTask = new SaveAvatarTask(context, finalAvatar, onSavedListener);
        saveTask.execute();
        UploadAvatarTask uploadTask = new UploadAvatarTask(context, finalAvatar, listener);
        uploadTask.execute();
    }

    public static Bitmap downloadUserAvatar(Context context){
        User currentUser = UserService.getLoggedInUser(context);
        return downloadAvatar(context, currentUser.getId());
    }
    public static Bitmap downloadAvatar(Context context, int userId){
        Bitmap avatar = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try{
            URL url = new URL(context.getResources().getString(R.string.url_download_avatar) + "?userId=" + userId);
            URLConnection connection = url.openConnection();
            connection.connect();

            int lengthOfFile = connection.getContentLength();
            in = new BufferedInputStream(url.openStream(), 8192);
            out = new ByteArrayOutputStream();

            int count;
            byte[] bytes = new byte[8192];
            while((count = in.read(bytes)) != -1){
                out.write(bytes, 0, count);
            }
            avatar = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
        }
        catch(MalformedURLException e){
            Logger.e("Malformed url");
            return null;
        }
        catch(IOException e){
            Logger.e("IOException");
            Logger.e(e.getMessage());
            return null;
        }
        finally {
            try{
                if(out != null){
                    out.flush();
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }
            catch (IOException e){
                Logger.e("Could not close streams....");
            }
        }
        return avatar;
    }

    static void clearAvatar(Context context){
        File path = new File(getAvatarDir(context), FILE_NAME);
        boolean deleted = path.delete();
        if(!deleted){
            Logger.e("User avatar not deleted");
        }
    }

    private static File getAvatarDir(Context context){
        ContextWrapper cw = new ContextWrapper(context);
        return cw.getDir("imageDir", Context.MODE_PRIVATE);
    }

    private static class SaveAvatarTask extends AsyncTask<Void, Void, Void>{
        private Context context;
        private Bitmap avatar;
        private AvatarSavedListener listener;
        SaveAvatarTask(Context context, Bitmap avatar){
            this.context = context;
            this.avatar = avatar;
        }
        SaveAvatarTask(Context context, Bitmap avatar, AvatarSavedListener listener){
            this.context = context;
            this.avatar = avatar;
            this.listener = listener;
        }
        @Override
        protected Void doInBackground(Void... nothing){
            try{
                saveToStorage(context, avatar);
                if(listener != null){
                    listener.avatarSaved(avatar);
                }
            }
            catch(IOException e){
                Logger.e("Could not save avatar to storage: " + e.getMessage());
            }
            return null;
        }
    }
    private static class DownloadAvatarTask extends AsyncTask<Void, Void, Bitmap>{
        private Context context;
        private int userId;
        DownloadAvatarTask(Context context, int userId){
            this.context = context;
            this.userId = userId;
        }
        @Override
        protected Bitmap doInBackground(Void... nothing){
            return downloadAvatar(context, userId);
        }
        @Override
        protected void onPostExecute(Bitmap avatar){
            try{
                saveToStorage(context, avatar);
            }
            catch(IOException e){
                Logger.e(e.getMessage());
            }
        }
    }
    private static class UploadAvatarTask extends AsyncTask<Void, Void, Void>{
        private Context context;
        private Bitmap avatar;
        private RequestFinishedListener<SuccessResponse, AvatarUploadFailedResponse> listener;

        UploadAvatarTask(Context context, Bitmap avatar, RequestFinishedListener<SuccessResponse, AvatarUploadFailedResponse> listener){
            this.context = context;
            this.avatar = avatar;
            this.listener = listener;
        }
        @Override
        protected Void doInBackground(Void... nothing){
            DivisionProfileProvider provider = new DivisionProfileProvider(context);
            provider.uploadAvatar(AuthService.getToken(context), avatar, new RequestFinishedListener<SuccessResponse, AvatarUploadFailedResponse>() {
                @Override
                public void onSuccess(SuccessResponse response) {
                    if(listener != null) {
                        listener.onSuccess(response);
                    }
                }

                @Override
                public void onFail(AvatarUploadFailedResponse response) {
                    Logger.e(response.getCauseString());
                    if(listener != null) {
                        listener.onFail(response);
                    }
                }

                @Override
                public void onError(ErrorResponse response) {
                    Logger.e(response.getMessage());
                    if(listener != null) {
                        listener.onError(response);
                    }
                }
            });
            return null;
        }
    }
}
