package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

class FillBuffer {

    static ByteBuf fromHex(String hex) {
        ByteBuf buf = Unpooled.buffer();
        String[] values = hex.split(" ");
        for (String value : values) {
            buf.writeByte(Integer.decode("0x".concat(value)));
        }
        return buf;
    }
}
