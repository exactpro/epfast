package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public class DecodeNullableUInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = 144115188075855872L;

    private byte[] byteArrayValue = new byte[8];

    private boolean maxValue;

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
        if (value == 0) {
            return null;
        } else {
            if (maxValue) {
                fillByteArray(byteArrayValue, -1L);
                return new BigInteger(1, byteArrayValue);
            } else {
                fillByteArray(byteArrayValue, --value);
                return new BigInteger(1, byteArrayValue);
            }
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
