package com.sivin.videocore;

import android.content.Context;

import com.sivin.videocore.logger.FwLog;

public class VideoCore {
    private static Context mCtx;

    public static void init(Context context) {
        mCtx = context.getApplicationContext();
        FwLog.init(mCtx,"0.0.1");
    }

    public static Context getContext() {
        return mCtx;
    }
}
