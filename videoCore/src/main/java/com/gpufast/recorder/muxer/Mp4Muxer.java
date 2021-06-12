package com.gpufast.recorder.muxer;

import android.media.MediaFormat;
import android.media.MediaMuxer;

import com.gpufast.logger.ELog;
import com.gpufast.recorder.audio.EncodedAudio;
import com.gpufast.recorder.video.EncodedImage;
import com.gpufast.recorder.video.VideoFrame;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 视频合成接口
 */
public class Mp4Muxer extends IMediaMuxer {
    private static final String TAG = Mp4Muxer.class.getSimpleName();

    private final InnerMuxer mInnerMuxer;
    private volatile boolean videoTrackReady = false;
    private volatile boolean audioTrackReady = false;
    private boolean muteMic;  //是否禁止录制音频
    private MediaResource mMediaResource;
    private MuxerWorker mMuxerWorker;

    Mp4Muxer(Setting setting) {
        if (setting == null)
            throw new IllegalArgumentException("setting is null object");
        mInnerMuxer = new InnerMuxer(setting.savePath);
        initMuxer(setting);
    }

    private void initMuxer(Setting setting) {
        muteMic = setting.muteMic;
        muteMic = false;
        mMediaResource = new MediaResource();
        mMuxerWorker = new MuxerWorker(mInnerMuxer,mMediaResource);
    }


    @Override
    public void onUpdateAudioMediaFormat(MediaFormat mediaFormat) {
        if (audioTrackReady || muteMic) return;
        ELog.i(TAG, "onUpdateAudioMediaFormat" + mediaFormat);
        boolean suc = mInnerMuxer.addAudioTrack(mediaFormat);
        if (!suc) {
            ELog.e(TAG, "Add audio track failed");
            return;
        }
        audioTrackReady = true;
        ELog.i(TAG, "audio track has ready");
        if (videoTrackReady) {
            mMuxerWorker.start();
        }
    }


    @Override
    public void onUpdateVideoMediaFormat(MediaFormat mediaFormat) {
        if (videoTrackReady) return;
        ELog.i(TAG, "onUpdateVideoMediaFormat" + mediaFormat);
        boolean suc = mInnerMuxer.addVideoTrack(mediaFormat);
        if (!suc) {
            ELog.e(TAG, "Add video track failed");
            return;
        }
        videoTrackReady = true;
        ELog.i(TAG, "video track has ready");
        if(muteMic) {
            mMuxerWorker.start();
            return;
        }
        if (audioTrackReady) {
            mMuxerWorker.start();
        }
    }
    @Override
    public void onEncodedFrame(EncodedImage frame) {
        mMediaResource.putVideoFrame(frame);
    }

    @Override
    public void onEncodedAudio(EncodedAudio frame) {
        if (!muteMic) {
            mMediaResource.putAudioFrame(frame);
        }
    }

    @Override
    public void onVideoEncoderStop() {
        ELog.i(TAG, "onVideoEncoderStop....");
        mMuxerWorker.stopFlag = true;
    }

    @Override
    public void onAudioEncoderStop() {
        ELog.i(TAG, "onAudioEncoderStop....");
        mMuxerWorker.stopFlag = true;
    }

    static class MuxerWorker extends Thread {
        private volatile boolean workerStarted = false;
        private boolean stopFlag = false;
        private final InnerMuxer mInnerMuxer;
        private final MediaResource mediaResource;

        MuxerWorker(InnerMuxer muxer,MediaResource resource) {
            this.mInnerMuxer = muxer;
            mediaResource = resource;
        }

        @Override
        public synchronized void start() {
            if (workerStarted) return;
            workerStarted = true;
            super.start();
        }

        @Override
        public void run() {
            onMuxerStart();
            while (!stopFlag) {
                writeVideo();
                writeAudio();
            }
            drainAllMediaData();
            onMuxerExit();
        }

        private void onMuxerStart() {
            mInnerMuxer.start();
        }

        private void writeAudio() {
            EncodedAudio audioFrame = mediaResource.getAudioFrame();
            mInnerMuxer.writeAudioData(audioFrame);
        }

        private void writeVideo() {
            EncodedImage videoFrame = mediaResource.getVideoFrame();
            mInnerMuxer.writeVideoData(videoFrame);
        }

        private void drainAllMediaData() {
            boolean haveData = true;
            while (haveData) {
                EncodedAudio audioFrame = mediaResource.getAudioFrame();
                EncodedImage videoFrame = mediaResource.getVideoFrame();
                if(audioFrame == null && videoFrame == null) {
                    haveData = false;
                }
                mInnerMuxer.writeAudioData(audioFrame);
                mInnerMuxer.writeVideoData(videoFrame);
            }
        }
        //muxer线程退出工作
        private void onMuxerExit() {
            mInnerMuxer.release();
        }
    }
    static class MediaResource {
        private final LinkedBlockingQueue<EncodedImage> videoQueue;
        private final LinkedBlockingQueue<EncodedAudio> audioQueue;

        MediaResource() {
            videoQueue = new LinkedBlockingQueue<>(300);
            audioQueue = new LinkedBlockingQueue<>(500);
        }

        public void putAudioFrame(EncodedAudio frame) {
            audioQueue.offer(frame);
        }

        public EncodedAudio getAudioFrame() {
            return audioQueue.poll();
        }

        public void putVideoFrame(EncodedImage frame) {
            videoQueue.offer(frame);
        }

        public EncodedImage getVideoFrame() {
            return videoQueue.poll();
        }
    }

    static class InnerMuxer {
        private MediaMuxer mediaMuxer;
        private int audioTrackIndex = -1;
        private int videoTrackIndex = -1;
        private boolean started = false;
        public InnerMuxer(String savePath) {
            try {
                mediaMuxer = new MediaMuxer(savePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            } catch (IOException e) {
                ELog.e(TAG, "Init Mp4Muxer:" + e.getMessage());
            }
        }

        public synchronized boolean addAudioTrack(MediaFormat format) {
            audioTrackIndex = mediaMuxer.addTrack(format);
            return audioTrackIndex >= 0;
        }

        public synchronized boolean addVideoTrack(MediaFormat format) {
            videoTrackIndex = mediaMuxer.addTrack(format);
            return videoTrackIndex >= 0;
        }

        public void start() {
            if(started) return;
            started = true;
            mediaMuxer.start();
        }

        public void writeAudioData(EncodedAudio frame) {
            if (frame == null || audioTrackIndex < 0) return;
            mediaMuxer.writeSampleData(audioTrackIndex,frame.buffer,frame.bufferInfo);
            ELog.i(TAG, "mux audio data，timeStamp:" + frame.bufferInfo.presentationTimeUs);
        }

        public void writeVideoData(EncodedImage frame) {
            if (frame == null || videoTrackIndex < 0) return;
            mediaMuxer.writeSampleData(videoTrackIndex, frame.buffer, frame.bufferInfo);
            ELog.i(TAG, "mux video data, index=" + frame.index
                    + " timeStamp:" + frame.bufferInfo.presentationTimeUs);
        }
        public void release() {
            mediaMuxer.release();
            ELog.i(TAG,"muxer release...");
        }
    }
}
