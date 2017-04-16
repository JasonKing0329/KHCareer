package com.king.khcareer.common.helper;

import android.graphics.Bitmap;

import com.king.khcareer.common.image.ImageFactory;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class BackgroundProvider {

    public Bitmap loadBackgound() {
        return new ImageFactory().getDefBackground();
    }
}
