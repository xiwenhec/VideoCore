package com.sivin.videocore.recorder.audio.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;

import com.sivin.videocore.logger.ELog;
import com.sivin.videocore.recorder.hardware.MediaCodecUtils;
import com.sivin.videocore.recorder.hardware.MediaCodecWrapperFactoryImpl;

import java.util.ArrayList;

public class HwAudioEncoderFactory implements AudioEncoderFactory {
    private static final String TAG = "HwAudioEncoderFactory";

    @Override
    public AudioCodecInfo getSupportCodecInfo() {
        AudioCodecInfo info = null;
        for (AudioCodecType type : new AudioCodecType[]{AudioCodecType.AAC}) {
            MediaCodecInfo codecInfo = findCodecForType(type);
            if (codecInfo != null) {
                String name = type.name();
                info = new AudioCodecInfo(name, type);
            }
        }
        return info;
    }

    private MediaCodecInfo findCodecForType(AudioCodecType type) {
        MediaCodecList codecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
        MediaCodecInfo[] codecInfos = codecList.getCodecInfos();
        ArrayList<MediaCodecInfo> encoderList = new ArrayList<>();

        for (int i = 0; i < codecInfos.length; ++i) {
            MediaCodecInfo info = null;
            try {
                info = codecInfos[i];
            } catch (IllegalArgumentException e) {
                ELog.e(TAG, "Cannot retrieve encoder codec info:" + e);
            }
            if (info == null || !info.isEncoder()) {
                continue;
            }
            if (!codecSupportsType(info, type)) {
                continue;
            }

            //判断是否是硬编码器
            if (isHWCodec(info)) {
                return info;
            }

            //查找谷歌软件实现的硬编码器
            if (info.getName().contains(MediaCodecUtils.GOOGLE_PREFIX)) {
                return info;
            }
            encoderList.add(info);
        }
        return encoderList.size() > 0 ? encoderList.get(0) : null;
    }

    private boolean isHWCodec(MediaCodecInfo info) {
        if (info.getName().startsWith(MediaCodecUtils.QTI_PREFIX)) {
            return true;
        }
        return false;
    }


    private boolean codecSupportsType(MediaCodecInfo info, AudioCodecType type) {
        for (String mimeType : info.getSupportedTypes()) {
            if (type.mimeType().equals(mimeType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AudioEncoder createEncoder(AudioCodecInfo inputCodecInfo) {
        //编码器的类型
        AudioCodecType type = AudioCodecType.valueOf(inputCodecInfo.name);
        //根据类型查找编码器的信息
        MediaCodecInfo info = findCodecForType(type);
        if (info == null) {
            ELog.e(TAG, "can't find Encoder by type" + inputCodecInfo.name);
            return null;
        }
        String name = info.getName();
        String mime = type.mimeType();
        ELog.i(TAG, "crate audio encoder, name:" + name + " mime:" + mime);
        return new HwAudioEncoder(new MediaCodecWrapperFactoryImpl(), type, name);
    }
}
