package com.sivin.videodemo.recorder.ui;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cc.sivin.videodemo.databinding.ActivityRecorderBinding;


public class RecorderActivity extends AppCompatActivity {

    private ActivityRecorderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityRecorderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addFragment();
    }

    private void addFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(binding.getRoot().getId(), new RecorderFragment());
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
