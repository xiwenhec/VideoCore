package com.sivin.videodemo.recorder.presenter;

import android.opengl.EGLContext;
import android.view.SurfaceView;

import com.sivin.videocore.camera.CameraEngine;
import com.sivin.videodemo.recorder.contract.RecorderContract;
import com.sivin.videocore.logger.ELog;
import com.sivin.videocore.recorder.RecorderEngine;
import com.sivin.videocore.recorder.RecordParams;
import com.sivin.videocore.recorder.audio.AudioFrame;
import com.sivin.videocore.recorder.audio.AudioProcessor;
import com.sivin.videocore.render.Render;

public class RecorderPresenter implements RecorderContract.Presenter, Render.OnRenderCallback, AudioProcessor {
    private static final String TAG = "RecorderPresenter";

    private RecorderContract.View mView;
    private CameraEngine mCameraEngine;


    public void attachView(RecorderContract.View view) {
        mView = view;
    }


    public void init() {
        SurfaceView preview = mView.getPreview();
        if (preview == null) {
            ELog.e(TAG, "preview == null : true");
            return;
        }
        mCameraEngine = CameraEngine.getInstance();
        mCameraEngine.setPreview(preview);
        mCameraEngine.setRenderFrameCallback(this);
        RecorderEngine.setAudioProcessor(this);
    }

    @Override
    public void switchCamera() {
        mCameraEngine.switchCamera();
    }

    @Override
    public void onEglContextCreate(EGLContext eglContext) {
        RecorderEngine.setShareContext(eglContext);
    }


    @Override
    public int onFrameCallback(int textureId, int width, int height) {
        RecorderEngine.sendVideoFrame(textureId, width, height);
        return 0;
    }

    @Override
    public void onEglContextDestroy() {
    }


    @Override
    public void setRecorderParameter(RecordParams params) {
        RecorderEngine.setParams(params);
    }

    @Override
    public void startRecorder() {
        RecorderEngine.startRecorder();
    }

    @Override
    public void stopRecorder() {
        RecorderEngine.stopRecorder();
    }

    @Override
    public void jointVideo() {
        RecorderEngine.jointVideo();
    }

    @Override
    public boolean isRecording() {
        return RecorderEngine.isRecording();
    }


    @Override
    public AudioFrame onReceiveAudioFrame(AudioFrame frame) {
        //对声音进行编码前预处理
        return null;
    }
}
