package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.IDecodeContext;

public abstract class DecodeInteger implements IDecodeContext {

    protected static final int SIGN_BIT_MASK = 0b01000000;

    protected boolean overflow;

    protected boolean ready;

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }

    static void longToBytes(long value, byte[] bytes) {
        for (int i = 7; i >= 0; --i) {
            bytes[i] = (byte) value;
            value >>>= 8;
        }
    }
}
