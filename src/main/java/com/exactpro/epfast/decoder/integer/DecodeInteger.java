package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeInteger implements IDecodeContext {

    protected boolean overflow;

    protected boolean ready;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }
}
