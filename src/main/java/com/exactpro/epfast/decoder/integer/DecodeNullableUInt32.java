package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

public class DecodeNullableUInt32 extends DecodeInteger {

    private static final int POSITIVE_LIMIT = 33554432;

    private boolean maxValue;

    private int value;

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
            return maxValue ? 4294967295L : (long) --value & 0xFFFFFFFFL;
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
            maxValue = true;
        } else {
            overflow = true;
        }
    }
}
