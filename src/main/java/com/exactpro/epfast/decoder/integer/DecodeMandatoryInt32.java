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
            if (oneByte < 0) {
                buf.readerIndex(readerIndex);
                return;
            }
            if (readerIndex < readLimit) {
                checkOverlongPositive(buf.getByte(readerIndex)); //check second byte
                do {
                    accumulatePositive(buf.getByte(readerIndex++));
                } while (!ready && readerIndex < readLimit);
            } else {
                checkForSignExtension = true;
            }
        } else {
            value = -1;
            accumulateNegative(oneByte);
            if (oneByte < 0) {
                buf.readerIndex(readerIndex);
                return;
            }
            if (readerIndex < readLimit) {
                checkOverlongNegative(buf.getByte(readerIndex)); //check second byte
                do {
                    accumulateNegative(buf.getByte(readerIndex++));
                } while (!ready && readerIndex < readLimit);
            } else {
                checkForSignExtension = true;
            }
        }
        buf.readerIndex(readerIndex);
    }

    public void continueDecode(ByteBuf buf) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        if (value >= 0) {
            if (checkForSignExtension) {
                checkOverlongPositive(buf.getByte(readerIndex)); //continue checking
                checkForSignExtension = false;
            }
            do {
                accumulatePositive(buf.getByte(readerIndex++));
            } while (!ready && readerIndex < readLimit);
        } else {
            if (checkForSignExtension) {
                checkOverlongNegative(buf.getByte(readerIndex)); //check first and second bytes
                checkForSignExtension = false;
            }
            do {
                accumulateNegative(buf.getByte(readerIndex++));
            } while (!ready && readerIndex < readLimit);
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

    private void checkOverlongPositive(int secondByte) {
        overlong = value == 0 && ((secondByte & SIGN_BIT_MASK) == 0);
    }

    private void checkOverlongNegative(int secondByte) {
        overlong = value == -1 && ((secondByte & SIGN_BIT_MASK) != 0);
    }
}
