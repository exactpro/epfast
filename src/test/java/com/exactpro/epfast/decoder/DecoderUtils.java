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

package com.exactpro.epfast.decoder;

import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

import java.util.Iterator;

public class DecoderUtils {

    public static void decode(StreamDecoderCommand decoder, Iterable<ByteBuf> buffers, UnionRegister register) {
        Iterator<ByteBuf> it = buffers.iterator();
        while (decoder.decode(nextNonEmptyBuffer(it), register) == StreamDecoderCommand.MORE_DATA_NEEDED) {
        }
    }

    private static ByteBuf nextNonEmptyBuffer(Iterator<ByteBuf> buffers) {
        while (buffers.hasNext()) {
            ByteBuf buffer = buffers.next();
            if (buffer.isReadable()) {
                return buffer;
            }
        }
        throw new IllegalArgumentException("No non-empty buffers are left");
    }
}
