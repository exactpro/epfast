package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public class DecodeMandatoryUInt64 extends DecodeInteger {

    private long value;

    private static final long OVERFLOW_MASK = 0xFE00000000000000L;

    DecodeMandatoryUInt64() {
    }

    public void decode(ByteBuf buf) {
        while (buf.isReadable() && !ready) {
            accumulatePositive(buf.readByte());
        }
    }

    public void continueDecode(ByteBuf buf) {
        decode(buf);
    }

    public BigInteger getValue() {
        return new BigInteger(1, new byte[] {
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

}
