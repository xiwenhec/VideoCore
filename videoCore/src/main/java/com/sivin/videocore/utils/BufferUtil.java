package com.sivin.videocore.utils;

import java.nio.ByteBuffer;

public class BufferUtil {
    public static ByteBuffer clone(ByteBuffer original) {
        ByteBuffer clone = ByteBuffer.allocate(original.capacity());
        // 使缓冲区准备好重新读取已经包含的数据：它保持限制不变，并将位置设置为零。
        // 因为 put(buffer) 会在内部遍历buffer，如果不执行rewind，position值将不会被重置
        original.rewind();
        clone.put(original);
        original.rewind();
        // 这个是将clone转换为读的这状态，否则将无法读取出数据
        clone.flip();
        return clone;
    }
}
