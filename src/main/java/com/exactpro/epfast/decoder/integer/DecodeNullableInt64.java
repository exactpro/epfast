package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

public final class DecodeNullableInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = 0x01000000_00000000L;

    private static final long NEGATIVE_LIMIT = Long.MIN_VALUE >> 7;

    private boolean positive;

    private long value;

    public void decode(ByteBuf buf) {
        reset();
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        int oneByte = buf.getByte(readerIndex++);
        positive = (oneByte & SIGN_BIT_MASK) == 0;
        if (positive) {
            value = 0;
            accumulatePositive(oneByte);
            while (readerIndex < readLimit && !ready) {
                accumulatePositive(buf.getByte(readerIndex++));
            }
        } else {
            value = -1;
            accumulateNegative(oneByte);
            while (readerIndex < readLimit && !ready) {
                accumulateNegative(buf.getByte(readerIndex++));
            }
        }
        buf.readerIndex(readerIndex);
    }

    public void continueDecode(ByteBuf buf) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        if (positive) {
            while (readerIndex < readLimit && !ready) {
                accumulatePositive(buf.getByte(readerIndex++));
            }
        } else {
            while (readerIndex < readLimit && !ready) {
                accumulateNegative(buf.getByte(readerIndex++));
            }
        }
        buf.readerIndex(readerIndex);
    }

    public Long getValue() throws OverflowException {
        if (overflow) {
            throw new OverflowException("Int64 Overflow");
        } else {
            return value == 0 ? null : positive ? value - 1 : value;
        }
    }

    private void accumulatePositive(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value < POSITIVE_LIMIT) {
            value = (value << 7) | oneByte;
        } else if (value == POSITIVE_LIMIT && oneByte == 0 && ready) {
            value = (value << 7);
        } else {
            overflow = true;
        }
    }

    private void accumulateNegative(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value >= NEGATIVE_LIMIT) {
            value = (value << 7) | oneByte;
        } else {
            overflow = true;
        }
    }
}
