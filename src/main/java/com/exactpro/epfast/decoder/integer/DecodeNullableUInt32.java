package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

public final class DecodeNullableUInt32 extends DecodeInteger {

    private static final int POSITIVE_LIMIT = 0x02000000;

    private boolean isUInt32Limit;

    private int value;

    public void decode(ByteBuf buf) {
        reset();
        isUInt32Limit = false;
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

    public Long getValue() throws OverflowException {
        if (overflow) {
            throw new OverflowException("UInt32 Overflow");
        } else if (value == 0) {
            return null;
        } else {
            return isUInt32Limit ? 0x0_FFFFFFFFL : value - 1 & 0x0_FFFFFFFFL;
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
            isUInt32Limit = true;
        } else {
            overflow = true;
        }
    }
}
