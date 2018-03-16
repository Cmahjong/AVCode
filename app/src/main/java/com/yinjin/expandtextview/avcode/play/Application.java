package com.yinjin.expandtextview.avcode.play;

/**
 * Created by ywl on 2017-12-30.
 */

public class Application extends android.app.Application {

    private static Application instance;
    public static Application getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
