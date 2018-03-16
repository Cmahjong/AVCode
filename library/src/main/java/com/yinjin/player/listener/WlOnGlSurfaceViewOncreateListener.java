package com.yinjin.player.listener;

import android.graphics.Bitmap;
import android.view.Surface;



public interface WlOnGlSurfaceViewOncreateListener{

    void onGlSurfaceViewOncreate(Surface surface);

    void onCutVideoImg(Bitmap bitmap);

}
