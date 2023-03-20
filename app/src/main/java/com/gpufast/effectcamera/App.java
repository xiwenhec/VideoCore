package com.gpufast.effectcamera;

import android.app.Application;
import android.content.Context;

import com.gpufast.VideoCore;
import com.gpufast.logger.FwLog;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
    @Override
    public void onCreate() {
        super.onCreate();
        VideoCore.init(getApplicationContext());
    }
}
