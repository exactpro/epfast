package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

public final class DecodeMandatoryInt32 extends DecodeInteger {

    private static final int POSITIVE_LIMIT = Integer.MAX_VALUE >> 7;

    private static final int NEGATIVE_LIMIT = Integer.MIN_VALUE >> 7;

    private int value;

    public void decode(ByteBuf buf) {
        reset();
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        int oneByte = buf.getByte(readerIndex++);
        if ((oneByte & SIGN_BIT_MASK) == 0) {
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
        if (value >= 0) {
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

    public int getValue() throws OverflowException {
        if (overflow) {
            throw new OverflowException("Int32 Overflow");
        } else {
            return value;
        }
    }

    private void accumulatePositive(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value <= POSITIVE_LIMIT) {
            value = (value << 7) | oneByte;
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
