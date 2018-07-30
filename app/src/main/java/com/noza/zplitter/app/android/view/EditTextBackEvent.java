package com.noza.zplitter.app.android.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by omiwrench on 2016-01-31.
 */
public class EditTextBackEvent extends AppCompatEditText {

    public interface OnBackPressedListener{
        void onBack();
    }

    private OnBackPressedListener mOnImeBack;

    public EditTextBackEvent(Context context) {
        super(context);
    }

    public EditTextBackEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextBackEvent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) mOnImeBack.onBack();
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnBackListener(OnBackPressedListener listener) {
        mOnImeBack = listener;
    }

}