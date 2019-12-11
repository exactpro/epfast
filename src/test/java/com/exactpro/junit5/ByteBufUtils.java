package com.exactpro.junit5;

import com.exactpro.epfast.ByteBufProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.ArrayList;

public class ByteBufUtils {

    public static ByteBuf fromHex(String hex) {
        return buffFromBytes(bytesFromHex(hex));
    }

    static byte[] bytesFromHex(String hex) {
        if (!hex.isEmpty()) {
            String[] values = hex.split(" +");
            byte[] bytes = new byte[values.length];
            for (int i = 0; i < values.length; i++) {
                try {
                    bytes[i] = (byte) Integer.parseInt(values[i], 16);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Illegal Argument Format. " +
                        "Invalid value " + values[i] + " in \"" + hex + "\" " +
                        "Need to pass 1 byte hexadecimal values separated by space. " +
                        "regex format \"([0-9|a-f]{2} )\"");
                }
            }
            return bytes;
        } else {
            return new byte[0];
        }

    }

    static ByteBuf buffFromBytes(byte[] bytes) {
        return Unpooled.copiedBuffer(bytes);
    }

    public static void withByteBuf(String hex, ByteBufProcessor processor) throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(fromHex(hex));
        processor.process(buffers);
    }
}
