package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public final class DecodeNullableUInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = 0x02000000_00000000L;

    private byte[] bytes = new byte[8];

    private boolean isUInt64Limit;

    private long value;

    public void decode(ByteBuf buf) {
        reset();
        isUInt64Limit = false;
        value = 0;
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        while (readerIndex < readLimit && !ready) {
            accumulate(buf.getByte(readerIndex++));
        }
        buf.readerIndex(readerIndex);
    }

    public void continueDecode(ByteBuf buf) {
        decode(buf);
    }

    public BigInteger getValue() throws OverflowException {
        if (overflow) {
            throw new OverflowException("UInt64 Overflow");
        } else if (value == 0) {
            return null;
        } else {
            if (isUInt64Limit) {
                longToBytes(-1L, bytes);
                return new BigInteger(1, bytes);
            } else {
                longToBytes(value - 1, bytes);
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
