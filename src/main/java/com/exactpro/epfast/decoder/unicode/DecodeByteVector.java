package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.integer.DecodeInteger;
import com.exactpro.epfast.decoder.integer.DecodeMandatoryInt32;
import com.exactpro.epfast.decoder.integer.DecodeNullableUInt32;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public abstract class DecodeByteVector implements IDecodeContext {

    List<Byte> value;

    int counter;

    int readableBytes;

    int readIndex;

    boolean overflow;

    boolean lengthReady;

    protected boolean ready;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public abstract byte[] getValue() throws OverflowException;

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow(){
        return overflow;
    }
}


