package com.noza.zplitter.app.android.view.behavior;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by omiwrench on 2016-03-05.
 */
public class MoveUpwardBehavior extends CoordinatorLayout.Behavior<View>{
    private static final boolean SNACKBAR_BEHAVIOR_ENABLED;
    static{
        SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;
    }

    public MoveUpwardBehavior(Context context, AttributeSet attrs){}

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency){
        return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency){
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
