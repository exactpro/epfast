package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeByteVector implements IDecodeContext {

    StringBuilder val = new StringBuilder();

    int messageLength;

    int counter;

    protected boolean ready;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public abstract String getValue();

    public boolean isReady() {
        return ready;
    }

}
