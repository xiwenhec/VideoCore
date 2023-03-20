package com.sivin.videocore.recorder.audio.encoder;

import static android.media.MediaFormat.MIMETYPE_AUDIO_AAC;
import static android.media.MediaFormat.MIMETYPE_AUDIO_OPUS;

/**
 * 音频编码器类型
 */
public enum  AudioCodecType {
    OPUS(MIMETYPE_AUDIO_OPUS),
    AAC(MIMETYPE_AUDIO_AAC);

    private final String mimeType;

    AudioCodecType(String mimeType) {
        this.mimeType = mimeType;
    }

    String mimeType() {
        return mimeType;
    }

}
