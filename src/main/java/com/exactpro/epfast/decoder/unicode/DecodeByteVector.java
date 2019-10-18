package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public abstract class DecodeByteVector implements IDecodeContext {

    List<Byte> value;

    int counter;

    boolean lengthReady;

    boolean ready;

    boolean overflow;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public abstract byte[] getValue() throws OverflowException;

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }

    public final void reset() {
        lengthReady = false;
        ready = false;
        overflow = false;
        counter = 0;
        value = new ArrayList<>();
    }
}


