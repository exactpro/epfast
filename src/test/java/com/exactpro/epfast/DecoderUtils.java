package com.exactpro.epfast;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

import java.util.Iterator;

public class DecoderUtils {

    public static void decode(IDecodeContext decoder, Iterable<ByteBuf> buffers) {
        Iterator<ByteBuf> it = buffers.iterator();
        decoder.decode(nextNonEmptyBuffer(it));
        while (!decoder.isReady()) {
            decoder.continueDecode(nextNonEmptyBuffer(it));
        }
    }

    private static ByteBuf nextNonEmptyBuffer(Iterator<ByteBuf> buffers) {
        while (buffers.hasNext()) {
            ByteBuf buffer = buffers.next();
            if (buffer.isReadable()) {
                return buffer;
            }
        }
        throw new IllegalArgumentException("No non-empty buffers are left");
    }
}
