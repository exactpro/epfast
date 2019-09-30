package com.exactpro.epfast.decoder;

import io.netty.buffer.ByteBuf;

public interface IDecodeContext {
    int CHECK_STOP_BIT_MASK = 0b10000000;
    int CLEAR_STOP_BIT_MASK = 0b01111111;

    void decode(ByteBuf buf);

    void continueDecode(ByteBuf buf);

    boolean isReady();
}