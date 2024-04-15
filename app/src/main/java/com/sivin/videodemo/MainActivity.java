package com.sivin.videodemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cc.sivin.videodemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        initView();
        initEvent();
    }

    private void initData() {
    }

    private void initView() {
    }

    private void initEvent() {

    }

}

