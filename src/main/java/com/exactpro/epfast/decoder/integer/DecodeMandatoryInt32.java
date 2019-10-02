package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;

class DecodeMandatoryInt32 extends DecodeInteger {

    private static final int POSITIVE_LIMIT = Integer.MAX_VALUE >> 7;

    private static final int NEGATIVE_LIMIT = Integer.MIN_VALUE >> 7;

    private int value;

    public void decode(ByteBuf buf) {
        int oneByte = buf.readByte();
        if ((oneByte & SIGN_BIT_MASK) == 0) {
            value = 0;
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
        if (value >= 0) {
            while (buf.isReadable() && !ready) {
                accumulatePositive(buf.readByte());
            }
        } else {
            while (buf.isReadable() && !ready) {
                accumulateNegative(buf.readByte());
            }
        }
    }

    public int getValue() {
        return value;
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
