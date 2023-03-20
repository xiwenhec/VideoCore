package com.gpufast.effectcamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.gpufast.effectcamera.recorder.ui.RecorderActivity;
import com.gpufast.logger.ELog;
import com.tbruyelle.rxpermissions3.RxPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button mStartCameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
    }

    private void initData() {
    }

    private void initView() {
        mStartCameraBtn = findViewById(R.id.start_camera_btn);
    }

    private void initEvent() {
        mStartCameraBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_camera_btn:
                startCameraActivity();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void startCameraActivity() {
        Intent intent = new Intent(MainActivity.this, RecorderActivity.class);
        startActivity(intent);
    }
}
