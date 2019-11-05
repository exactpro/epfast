package com.exactpro.epfast;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.ArrayList;

public class ByteBufUtils {

    private static ByteBuf fromHex(String hex) {
        ByteBuf buffer = Unpooled.buffer();
        if (!hex.isEmpty()) {
            String[] values = hex.split(" ");
            for (String value : values) {
                buffer.writeByte(Integer.parseInt(value, 16));
            }
        }
        return buffer;
    }

    public static void withByteBuf(String hex, ByteBufProcessor processor) throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(fromHex(hex));
        processor.process(buffers);
    }
}
