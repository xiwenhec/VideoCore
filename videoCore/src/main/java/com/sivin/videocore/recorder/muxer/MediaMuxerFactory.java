package com.sivin.videocore.recorder.muxer;

import com.sivin.videocore.recorder.RecordParams;

public class MediaMuxerFactory {

    public static IMediaMuxer createMediaMuxer(RecordParams params, MuxerType type) {
        switch (type) {
            case MP4:
                return Mp4MuxerCreator.create(params);
            case FLV:
            case MKV:
            case AVI:
                return null;
        }
        return null;
    }


}
