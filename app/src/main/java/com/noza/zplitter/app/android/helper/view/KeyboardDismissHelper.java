package com.noza.zplitter.app.android.helper.view;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class KeyboardDismissHelper {

    public interface OnKeyboardDismissListener{
        void onTapOutside();
    }

    public static void hideSoftKeyboard(Activity activity){
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void setAutoHideKeyboard(final View view, final Activity activity) {
        setAutoHideKeyboard(view, activity, null);
    }
    public static void setAutoHideKeyboard(final View view, final Activity activity, final OnKeyboardDismissListener listener){
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    if(listener != null){
                        listener.onTapOutside();
                    }
                    return false;
                }

            });
        }
        else if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setAutoHideKeyboard(innerView, activity);
            }
        }
    }
}
