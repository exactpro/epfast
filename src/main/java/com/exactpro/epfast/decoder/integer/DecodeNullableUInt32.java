package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

public class DecodeNullableUInt32 extends DecodeInteger {

    private int value;

    public DecodeNullableUInt32() {
        positiveLimit = 33554432;
    }

    public void decode(ByteBuf buf) {
        while (buf.isReadable() && !ready) {
            accumulate(buf.readByte());
        }
    }

    public void continueDecode(ByteBuf buf) {
        decode(buf);
    }

    public Long getValue() {
        if (value == 0) {
            return null;
        } else {
            if (value > 0) {
                value--;
            }
            return (long) value & 0xffffffffL;
        }
    }

    private void accumulate(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = oneByte & CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value < positiveLimit) {
            value = (value << 7) + oneByte;
        } else if (value == positiveLimit && oneByte == 0 && ready) {
            value = -1;
        } else {
            value = 0;
            overflow = true;
        }
    }

}
