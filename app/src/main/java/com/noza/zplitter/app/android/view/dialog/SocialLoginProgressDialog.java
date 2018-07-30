package com.noza.zplitter.app.android.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by omiwrench on 2016-02-22.
 */
public abstract class SocialLoginProgressDialog extends ProgressDialog {
    protected SocialLoginProgressDialog(Context context, int themeId){
        super(context, themeId);
    }

    @Override
    public void show(){
        this.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.setMessage("Logging you in...");
        this.setIndeterminate(true);
        this.setCanceledOnTouchOutside(false);
        super.show();
    }
}
