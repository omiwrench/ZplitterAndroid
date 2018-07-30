package com.noza.zplitter.app.android.helper;

import android.graphics.Bitmap;

import com.orhanobut.logger.Logger;

/**
 * Created by omiwrench on 2016-03-28.
 */
public class BitmapHelper {

    public static Bitmap scaleDownTo(Bitmap realImage, float maxImageSize, boolean filter){
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight()
        );
        if(ratio > 1){
            return realImage;
        }
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }
}
