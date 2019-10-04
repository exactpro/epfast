package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

class FillBuffer {

    static void fillBuffer(ByteBuf buf, String val) {
        String[] values = val.split(" ");
        for (String value : values) {
            buf.writeByte(Integer.decode(value));
        }
    }
}
