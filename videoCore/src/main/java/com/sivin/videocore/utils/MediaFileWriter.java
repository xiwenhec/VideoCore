package com.sivin.videocore.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.sivin.videocore.VideoCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaFileWriter {
    private FileOutputStream mFos;
    int length = 0;

    public MediaFileWriter(String fileName) {
        length = 0;
        File pcmFile = new File(getDataDirectory(VideoCore.getContext()) + "/" + fileName);
        if (pcmFile.exists()) {
            pcmFile.delete();
        } else {
            File parent = pcmFile.getParentFile();
            parent.mkdirs();
        }
        try {
            mFos = new FileOutputStream(pcmFile, true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDataDirectory(Context context) {
        String path;
        if (Build.VERSION.SDK_INT > 29) {
            path = context.getExternalFilesDir(null).getAbsolutePath();
        } else {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        return path;
    }

    public void writePCMData(ByteBuffer buffer) {
        writePCMData(buffer, 0);
    }

    public void writePCMData(ByteBuffer buffer, int len) {
        try {
            if (len == 0) {
                mFos.write(buffer.array());
            } else {
                mFos.write(buffer.array(), 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeACCData(ByteBuffer buffer, int len) {
        int aacDataSize = len + 7;
        byte[] aacData = new byte[len + 7];
        addADTStoPacket(aacData, aacDataSize);
        buffer.get(aacData, 7, len);
        try {
            mFos.write(aacData, 0, aacDataSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void close() {
        if (mFos != null) {
            try {
                mFos.flush();
                mFos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void addADTStoPacket(byte[] packet, int packetLen) {
        // AAC LC
        int profile = 2;
        // 44.1KHz
        int freqIdx = 4;
        // CPE
        int chanCfg = 1;
        // fill in ADTS data
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }
}
