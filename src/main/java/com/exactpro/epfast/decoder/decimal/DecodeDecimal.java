package com.exactpro.epfast.decoder.decimal;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.integer.DecodeMandatoryInt64;
import io.netty.buffer.ByteBuf;

public abstract class DecodeDecimal implements IDecodeContext {

    DecodeMandatoryInt64 mantissaDecoder = new DecodeMandatoryInt64();

    boolean exponentReady;

    boolean startedMantissa;

    protected boolean ready;

    protected boolean overflow;

    long mantissa;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }
}
