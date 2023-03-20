package com.sivin.videocore.camera;

public interface ISurface {

    void register(SurfaceChangeListener listener);

    int getWidth();

    int getHeight();
}
