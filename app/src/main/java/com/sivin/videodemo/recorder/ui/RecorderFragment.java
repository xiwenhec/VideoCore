package com.sivin.videodemo.recorder.ui;

import android.os.Build;
import android.os.Environment;
import android.view.SurfaceView;
import com.sivin.videodemo.BaseFragment;
import com.sivin.videodemo.recorder.contract.RecorderContract;
import com.sivin.videodemo.recorder.presenter.RecorderPresenter;
import com.sivin.videocore.recorder.RecordParams;

import java.io.File;

import cc.sivin.videodemo.R;

public class RecorderFragment extends BaseFragment implements RecorderContract.View {
    private static final String TAG = "RecorderFragment";
    private SurfaceView mPreview;
    private RecorderPresenter mPresenter;



    @Override
    protected int getLayoutId() {
        return R.layout.recoder_fragment_layout;
    }

    @Override
    protected void onInitView() {
        mPreview = findViewById(R.id.id_camera_preview);

//        Objects.requireNonNull(mSwitchCameraBtn).setOnClickListener(this);
//        Objects.requireNonNull(mStartRecorderBtn).setOnClickListener(this);

        mPresenter = new RecorderPresenter();
        initRecorderParams();
        mPresenter.attachView(this);
        mPresenter.init();
    }

    private String getSavePath() {
        String path;
        if (Build.VERSION.SDK_INT > 29) {
            path = getContext().getExternalFilesDir(null).getAbsolutePath();
        } else {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        return path;
    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private void initRecorderParams() {
        String path = getSavePath() + "/test";
        File parent = new File(path);
        parent.mkdirs();
        String mp4Path = path + "/recoder.mp4";
        File mp4File = new File(mp4Path);
        if (mp4File.exists()) {
            mp4File.delete();
        }
        RecordParams.Builder builder = new RecordParams.Builder();
        builder.setVideoWidth(720)
                .setVideoHeight(1280)
                .setEnableHwEncoder(true)
                .setSavePath(mp4Path);
        mPresenter.setRecorderParameter(builder.build());
    }


    @Override
    public SurfaceView getPreview() {
        return mPreview;
    }

    @Override
    public void onStartRecorder() {

    }

    @Override
    public void onRecorderProgress(int time_s) {

    }

    @Override
    public void onRecorderFinish() {

    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.id_switch_camera:
//                mPresenter.switchCamera();
//                break;
//            case R.id.id_start_recorder_btn:
//                if (mPresenter.isRecording()) {
//                    mPresenter.stopRecorder();
//                } else {
//                    mPresenter.startRecorder();
//                }
//                break;
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) mPresenter.stopRecorder();
    }
}
