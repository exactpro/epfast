package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

class DecodeMandatoryUInt32 extends DecodeInteger {

    private int value;

    private static final int OVERFLOW_MASK = 0xFE000000;

    DecodeMandatoryUInt32() {
        positiveLimit = 262143;
    }

    public void decode(ByteBuf buf) {
        while (buf.isReadable() && !ready) {
            accumulatePositive(buf.readByte());
        }
    }

    private void accumulatePositive(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = oneByte & CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if ((value & OVERFLOW_MASK) == 0) {
            value = (value << 7) + oneByte;
        } else {
            overflow = true;
            value = 0;
        }
    }

    public void continueDecode(ByteBuf buf) {
        decode(buf);
    }

    long getValue() {
        return (long) value & 0xffffffffL;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }
}
