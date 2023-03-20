package com.sivin.videocore.recorder.audio.encoder;

import android.media.MediaFormat;

import com.sivin.videocore.recorder.audio.AudioFrame;
import com.sivin.videocore.recorder.audio.AudioSetting;
import com.sivin.videocore.recorder.audio.EncodedAudio;

public interface AudioEncoder {

    class Settings {
        final int bitrate; // Kilobits per second.
        final int sampleRate;

        public Settings(int sampleRate, int bitrate) {
            this.sampleRate = sampleRate;
            this.bitrate = bitrate;
        }
    }

    AudioCodecStatus init(AudioSetting settings, AudioEncoderCallback callback);

    void encode(AudioFrame frame);

    void release();

    interface AudioEncoderCallback {

        void onUpdateAudioMediaFormat(MediaFormat mediaFormat);

        void onEncodedAudio(EncodedAudio frame);

        void onAudioEncoderStop();
    }
}
