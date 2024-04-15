package com.sivin.videocore.recorder.video;

import android.opengl.EGLContext;

import com.sivin.videocore.recorder.video.encoder.VideoCodecInfo;


public interface VideoEncoderFactory {

    void setShareContext(EGLContext shareContext);

    VideoCodecInfo[] getSupportedCodecs();

    VideoEncoder createEncoder(VideoCodecInfo inputCodecInfo);

}
