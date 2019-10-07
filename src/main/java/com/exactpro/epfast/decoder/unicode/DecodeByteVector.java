package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

import java.util.List;

public abstract class DecodeByteVector implements IDecodeContext {

    List<Byte> value;

    boolean lengthReady;

    boolean ready;

    boolean overflow;

    int readLimit;

    int readerIndex;

    int counter;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public abstract byte[] getValue() throws OverflowException;

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }
}


