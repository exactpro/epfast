package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

public class DecodeNullableUInt32 extends DecodeInteger {

    private static final int POSITIVE_LIMIT = 0x02000000;

    private boolean isUInt32Limit;

    private int value;

    public void decode(ByteBuf buf) {
        value = 0;
        ready = false;
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
            return isUInt32Limit ? 0x0_FFFFFFFFL : value - 1 & 0x0_FFFFFFFFL;
        }
    }

    private void accumulate(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value < POSITIVE_LIMIT) {
            value = (value << 7) | oneByte;
        } else if (value == POSITIVE_LIMIT && oneByte == 0 && ready) {
            isUInt32Limit = true;
        } else {
            overflow = true;
        }
    }
}
