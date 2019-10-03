package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public class DecodeNullableUInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = 0x02000000_00000000L;

    private byte[] bytes = new byte[8];

    private boolean isUInt64Limit;

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
            if (isUInt64Limit) {
                longToBytes(-1L, bytes);
                return new BigInteger(1, bytes);
            } else {
                longToBytes(--value, bytes);
                return new BigInteger(1, bytes);
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
            isUInt64Limit = true;
        } else {
            overflow = true;
        }
    }
}
