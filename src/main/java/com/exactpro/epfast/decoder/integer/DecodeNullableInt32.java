package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

public class DecodeNullableInt32 extends DecodeInteger {

    private static final int POSITIVE_LIMIT = 16777216;

    private static final int NEGATIVE_LIMIT = Integer.MIN_VALUE >> 7;

    private static final int SIGN_BIT_MASK = 0b01000000;

    private boolean positive;

    private int value;

    public void decode(ByteBuf buf) {
        int oneByte = buf.readByte();
        positive = (oneByte & SIGN_BIT_MASK) == 0;
        if (positive) {
            accumulatePositive(oneByte);
            while (buf.isReadable() && !ready) {
                accumulatePositive(buf.readByte());
            }
        } else {
            value = -1;
            accumulateNegative(oneByte);
            while (buf.isReadable() && !ready) {
                accumulateNegative(buf.readByte());
            }
        }
    }

    public void continueDecode(ByteBuf buf) {
        if (positive) {
            while (buf.isReadable() && !ready) {
                accumulatePositive(buf.readByte());
            }
        } else {
            while (buf.isReadable() && !ready) {
                accumulateNegative(buf.readByte());
            }
        }
    }

    public Integer getValue() {
        return value == 0 ? null : positive ? value - 1 : value;
    }

    private void accumulatePositive(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = (oneByte & CLEAR_STOP_BIT_MASK);
            ready = true;
        }
        if (value < POSITIVE_LIMIT) {
            value = (value << 7) + oneByte;
        } else if (value == POSITIVE_LIMIT && oneByte == 0 && ready) {
            value = (value << 7);
        } else {
            value = 0;
            overflow = true;
        }
    }

    private void accumulateNegative(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = (oneByte & CLEAR_STOP_BIT_MASK);
            ready = true;
        }
        if (value >= NEGATIVE_LIMIT) {
            value = (value << 7) + oneByte;
        } else {
            value = 0;
            overflow = true;
        }
    }

}
