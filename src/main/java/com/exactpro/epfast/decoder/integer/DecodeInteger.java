package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeInteger implements IDecodeContext {

    static final int SIGN_BIT_MASK = 0b01000000;

    protected boolean ready;

    protected boolean overflow;

    protected boolean overlong;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public boolean isReady() {
        return ready;
    }

    protected boolean isOverlong() {
        return overlong;
    }

    protected final void reset() {
        ready = false;
        overflow = false;
        overlong = false;
    }

    static void longToBytes(long value, byte[] bytes) {
        for (int i = 7; i >= 0; --i) {
            bytes[i] = (byte) value;
            value >>>= 8;
        }
    }
}
