package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

class DecodeMandatoryUInt32 extends DecodeInteger {

    private static final int OVERFLOW_MASK = 0xFE000000;

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

    public long getValue() {
        return value & 0x0_FFFFFFFFL;
    }

    private void accumulate(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if ((value & OVERFLOW_MASK) == 0) {
            value = (value << 7) | oneByte;
        } else {
            overflow = true;
        }
    }
}
