/*
 * Copyright 2019-2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        if (hex.isEmpty()) {
            return new byte[0];
        }
        String[] values = hex.split(" +");
        int size = 0;
        for (String value : values) {
            if (value.length() % 2 == 1) {
                throw new IllegalArgumentException("Illegal Argument Format. " +
                    "Invalid value " + value + " in \"" + hex + "\" " +
                    "Hexadecimal values must have even length");
            }
            size += value.length() / 2;
        }
        byte[] bytes = new byte[size];
        int index = 0;
        for (String value : values) {
            for (int i = 0; i < value.length(); i += 2) {
                try {
                    bytes[index++] = (byte) Integer.parseInt(value.substring(i, i + 2), 16);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Illegal Argument Format. " +
                        "Invalid value " + value.substring(i, i + 2) + " in \"" + hex + "\" " +
                        "required regex format \"([0-9|a-f]{2} )\"");
                }
            }
        }
        return bytes;
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
