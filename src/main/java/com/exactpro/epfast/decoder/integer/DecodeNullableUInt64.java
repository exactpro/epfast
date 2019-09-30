package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public class DecodeNullableUInt64 extends DecodeInteger {

    private long value;

    DecodeNullableUInt64() {
        positiveLimit = 144115188075855872L;
    }

    public void decode(ByteBuf buf) {
        while (buf.isReadable() && !ready) {
            accumulate(buf.readByte());
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

    public void continueDecode(ByteBuf buf) {
        decode(buf);
    }

    BigInteger getValue() {
        if (value == 0) {
            return null;
        } else {
            if (value > 0) {
                value--;
            }
            return new BigInteger(1, new byte[]
                {
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

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }
}
