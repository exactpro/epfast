package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

class DecodeMandatoryInt32 extends DecodeInteger {

    private static final int SIGN_BIT_MASK = 0b01000000;

    private int value;

    private boolean positive;

    DecodeMandatoryInt32() {
        positiveLimit = Integer.MAX_VALUE >> 7;
        negativeLimit = Integer.MIN_VALUE >> 7;
    }

    public void decode(ByteBuf buf) {
        int oneByte = buf.readByte();
        positive = (oneByte & SIGN_BIT_MASK) != 0;
        if (positive) {
            value = -1;
            accumulateNegative(oneByte);
            while (buf.isReadable() && !ready) {
                accumulateNegative(buf.readByte());
            }
        } else {
            accumulatePositive(oneByte);
            while (buf.isReadable() && !ready) {
                accumulatePositive(buf.readByte());
            }
        }
    }

    public void continueDecode(ByteBuf buf) {
        if (positive) {
            while (buf.isReadable() && !ready) {
                accumulateNegative(buf.readByte());
            }
        } else {
            while (buf.isReadable() && !ready) {
                accumulatePositive(buf.readByte());
            }
        }
    }

    public int getValue() {
        return value;
    }

    private void accumulatePositive(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = (oneByte & CLEAR_STOP_BIT_MASK);
            ready = true;
        }
        if (value <= positiveLimit) {
            value = (value << 7) + oneByte;
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
        if (value >= negativeLimit) {
            value = (value << 7) + oneByte;
        } else {
            value = 0;
            overflow = true;
        }
    }

}
