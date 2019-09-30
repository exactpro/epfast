package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeInteger implements IDecodeContext {

    boolean overflow;

    boolean ready;

    long positiveLimit;

    long negativeLimit;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public abstract boolean isReady();

    public abstract boolean isOverflow();
}
