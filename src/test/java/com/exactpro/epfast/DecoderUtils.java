package com.exactpro.epfast;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

import java.util.Iterator;

public class DecoderUtils {

    public static void decode(IDecodeContext decoder, Iterable<ByteBuf> buffers) {
        Iterator<ByteBuf> it = buffers.iterator();
        decoder.decode(it.next());
        while (!decoder.isReady() && it.hasNext()) {
            decoder.continueDecode(it.next());
        }
    }
}
