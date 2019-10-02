package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public class DecodeNullableUInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = 144115188075855872L;

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
                value = -1;
                return new BigInteger(1, new byte[]{
                    (byte) (value >> 56),
                    (byte) ((value >> 48) & 0xffL),
                    (byte) ((value >> 40) & 0xffL),
                    (byte) ((value >> 32) & 0xffL),
                    (byte) ((value >> 24) & 0xff),
                    (byte) ((value >> 16) & 0xffL),
                    (byte) ((value >> 8) & 0xffL),
                    (byte) (value & 0xffL)
                });
            } else {
                value--;
                return new BigInteger(1, new byte[]{
                    (byte) (value >> 56),
                    (byte) ((value >> 48) & 0xffL),
                    (byte) ((value >> 40) & 0xffL),
                    (byte) ((value >> 32) & 0xffL),
                    (byte) ((value >> 24) & 0xff),
                    (byte) ((value >> 16) & 0xffL),
                    (byte) ((value >> 8) & 0xffL),
                    (byte) (value & 0xffL)
                });
            }
        }
    }

    private void accumulate(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = oneByte & CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value < POSITIVE_LIMIT) {
            value = (value << 7) + oneByte;
        } else if (value == POSITIVE_LIMIT && oneByte == 0 && ready) {
            maxValue = true;
        } else {
            value = 0;
            overflow = true;
        }
    }
}
