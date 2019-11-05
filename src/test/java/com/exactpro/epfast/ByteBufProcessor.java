package com.exactpro.epfast;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

@FunctionalInterface
public interface ByteBufProcessor {
    void process(Iterable<ByteBuf> buffers) throws IOException;
}
