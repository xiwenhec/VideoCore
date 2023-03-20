package com.sivin.videocore.recorder.hardware;


import java.io.IOException;

public interface MediaCodecWrapperFactory {
  MediaCodecWrapper createByCodecName(String name) throws IOException;
  MediaCodecWrapper createEncoderByType(String type) throws IOException;
}