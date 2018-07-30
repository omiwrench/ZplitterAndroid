package com.noza.zplitter.app.android.helper.view;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noza.zplitter.app.android.view.EditTextBackEvent;

/**
 * Created by omiwrench on 2016-01-31.
 */
public class KeyboardAnimationHelper implements KeyboardDismissHelper.OnKeyboardDismissListener{
    private static final String TAG = KeyboardAnimationHelper.class.getName();
    private boolean viewIsUp = false;

    private final View view;
    private final float moveDistance;

    public KeyboardAnimationHelper(View view, float moveDistance){
        this.view = view;
        this.moveDistance = moveDistance;
    }

    public void registerForInputsIn(final ViewGroup inputsContainer){
        bindFocusListeners(inputsContainer, new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    moveViewUp();
                }
                else if(!hasFocus && inputsContainer.getFocusedChild() == null){
                    moveViewDown();
                }
            }
        });
        bindDoneListeners(inputsContainer, new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    moveViewDown();
                }
                return false;
            }
        });
        bindBackListeners(inputsContainer, new EditTextBackEvent.OnBackPressedListener() {
            @Override
            public void onBack() {
                moveViewDown();
            }
        });
        //View can still be focused without keyboard being up
        bindClickListeners(inputsContainer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewIsUp && isInputFocused(v)){
                    moveViewUp();
                }
            }
        });
    }
    public void unbind(){
        viewIsUp = false;
    }
    @Override
    public void onTapOutside(){
        if(viewIsUp){
            moveViewDown();
        }
    }
    private void bindFocusListeners(final View view, final View.OnFocusChangeListener focusListener){
        if(view instanceof EditText) {
            view.setOnFocusChangeListener(focusListener);
        }
        else if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                bindFocusListeners(innerView, focusListener);
            }
        }
    }
    private void bindDoneListeners(final View view, final TextView.OnEditorActionListener doneListener){
        if(view instanceof EditText) {
            ((EditText) view).setOnEditorActionListener(doneListener);
        }
        else if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                bindDoneListeners(innerView, doneListener);
            }
        }
    }
    private void bindBackListeners(final View view, final EditTextBackEvent.OnBackPressedListener backListener){
        if(view instanceof EditTextBackEvent) {
            ((EditTextBackEvent) view).setOnBackListener(backListener);
        }
        else if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                bindBackListeners(innerView, backListener);
            }
        }
    }
    private void bindClickListeners(final View view, final View.OnClickListener clickListener){
        if(view instanceof EditText) {
            view.setOnClickListener(clickListener);
        }
        else if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                bindClickListeners(innerView, clickListener);
            }
        }
    }

    private boolean isInputFocused(View view){
        if(view instanceof EditText){
            return view.isFocused();
        }
        else if(view instanceof ViewGroup){
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                if(isInputFocused(innerView)){
                    return true;
                }
            }
        }
        return false;
    }

    public void moveViewUp(){
        if(!viewIsUp){
            TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -moveDistance);
            anim.setDuration(350);
            anim.setAnimationListener(new TranslateAnimation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    params.topMargin -= moveDistance;
                    view.setLayoutParams(params);

                    //Fix for animation flicker
                    animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    animation.setDuration(1);
                    view.startAnimation(animation);

                    viewIsUp = true;
                }
            });
            view.startAnimation(anim);
        }
        else{
            Log.w(TAG, "KeyboardAnimationHelper attempted to move view up when it was already up");
        }
    }
    public void moveViewDown(){
        if(viewIsUp){
            TranslateAnimation anim = new TranslateAnimation(0, 0, 0, moveDistance);
            anim.setDuration(350);
            anim.setAnimationListener(new TranslateAnimation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    params.topMargin += moveDistance;
                    view.setLayoutParams(params);

                    //Fix for animation flicker
                    animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    animation.setDuration(1);
                    view.startAnimation(animation);

                    viewIsUp = false;
                }
            });
            view.startAnimation(anim);
        }
        else{
            Log.w(TAG, "KeyboardAnimationHelper attempted to move view down when it was already down");
        }
    }
}
