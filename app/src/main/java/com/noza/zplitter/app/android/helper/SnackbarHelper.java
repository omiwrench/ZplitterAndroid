package com.noza.zplitter.app.android.helper;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.noza.zplitter.app.android.R;

/**
 * Created by omiwrench on 2016-03-05.
 */
public class SnackbarHelper {

    private static Snackbar snackbar;

    public static void showErrorSnackbar(Context context, View view, int messageId){
        snackbar = Snackbar.make(view, messageId, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorError));
        snackbar.show();
    }
    public static void showUnknownErrorSnackbar(Context context, View view){
        snackbar = Snackbar.make(view, R.string.error_general_unknown, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorError));
        snackbar.show();
    }
    public static void showPersistantSnackbar(Context context, View view, int messageId){
        snackbar = Snackbar.make(view, messageId, Snackbar.LENGTH_INDEFINITE);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.cyan900));
        snackbar.show();
    }
    public static void dismissPersistantSnackbar(){
        if(snackbar != null){
            snackbar.dismiss();
            snackbar = null;
        }
    }
}
