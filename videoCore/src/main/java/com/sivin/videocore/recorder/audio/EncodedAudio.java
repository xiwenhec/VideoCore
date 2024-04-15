package com.sivin.videocore.recorder.audio;

import android.media.MediaCodec;

import com.sivin.videocore.utils.BufferUtil;

import java.nio.ByteBuffer;

public class EncodedAudio {

    public final ByteBuffer buffer;
    public final MediaCodec.BufferInfo bufferInfo;

    private EncodedAudio(ByteBuffer mBuffer, MediaCodec.BufferInfo mBufferInfo) {
        this.buffer = mBuffer;
        this.bufferInfo = mBufferInfo;
    }


    public static class Builder {
        private ByteBuffer buffer;
        private MediaCodec.BufferInfo bufferInfo;

        public Builder() {
        }

        public Builder setBuffer(ByteBuffer buffer) {
            this.buffer = BufferUtil.clone(buffer);
            return this;
        }


        public Builder setBufferInfo(MediaCodec.BufferInfo bufferInfo) {
            this.bufferInfo = new MediaCodec.BufferInfo();
            this.bufferInfo.set(bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
            return this;
        }

        public EncodedAudio createEncodedAudio() {
            return new EncodedAudio(buffer, bufferInfo);
        }
    }
}
