package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public class DecodeMandatoryUInt64 extends DecodeInteger {

    private static final long OVERFLOW_MASK = 0xFE00000000000000L;

    private byte[] byteArrayValue = new byte[8];

    private long value;

    public void decode(ByteBuf buf) {
        while (buf.isReadable() && !ready) {
            accumulate(buf.readByte());
        }
    }

    public void continueDecode(ByteBuf buf) {
        decode(buf);
    }

    public BigInteger getValue() {
        fillByteArray(byteArrayValue, value);
        return new BigInteger(1, byteArrayValue);
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
