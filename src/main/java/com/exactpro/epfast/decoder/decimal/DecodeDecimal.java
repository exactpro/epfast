package com.exactpro.epfast.decoder.decimal;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeDecimal implements IDecodeContext {

    static final int POSITIVE_LIMIT = Integer.MAX_VALUE >> 7;

    static final int NEGATIVE_LIMIT = Integer.MIN_VALUE >> 7;

    static final int SIGN_BIT_MASK = 0b01000000;

    protected boolean ready;

    protected boolean overflow;

    boolean positive;

    int exponent;

    int mantissa;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }
}
