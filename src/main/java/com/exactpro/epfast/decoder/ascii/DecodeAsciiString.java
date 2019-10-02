package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeAsciiString implements IDecodeContext {

    StringBuilder value = new StringBuilder();

    int relativeLength;

    boolean zeroPreamble;

    boolean overlong;

    protected boolean ready;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public abstract String getValue();

    public boolean isReady() {
        return ready;
    }

    public boolean isOverlong() {
        return overlong;
    }
}
