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

package com.exactpro.epfast.decoder.presencemap;

import com.exactpro.epfast.decoder.message.UnionRegister;
import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.Collection;

import static com.exactpro.epfast.DecoderUtils.decode;
import static org.junit.jupiter.api.Assertions.*;

class TestDecodePresenceMap {

    private DecodePresenceMap presenceMapDecoder = new DecodePresenceMap();
    
    private UnionRegister register = new UnionRegister();

    @WithByteBuf("95") //0b10010101
    void testSingleByte(Collection<ByteBuf> buffers) {
        decode(presenceMapDecoder, buffers, register);
        PresenceMap presenceMap = register.presenceMap;
        assertTrue(presenceMap.getValue(0));
        assertFalse(presenceMap.getValue(1));
        assertTrue(presenceMap.getValue(2));
        assertFalse(presenceMap.getValue(3));
        assertTrue(presenceMap.getValue(4));
        assertFalse(presenceMap.getValue(5));
        assertFalse(presenceMap.getValue(6));
    }

    @WithByteBuf("15 15 00 00 00 80")
    void testOverlong(Collection<ByteBuf> buffers) {
        decode(presenceMapDecoder, buffers, register);
        PresenceMap presenceMap = register.presenceMap;
        assertTrue(presenceMap.getValue(0));
        assertFalse(presenceMap.getValue(1));
        assertTrue(presenceMap.getValue(2));
        assertFalse(presenceMap.getValue(3));
        assertTrue(presenceMap.getValue(4));
        assertFalse(presenceMap.getValue(5));
        assertFalse(presenceMap.getValue(6));
        assertTrue(register.isOverlong);
    }

    @WithByteBuf("15 15 00 00 00 82")
    void testTruncateWhenNotOverlong(Collection<ByteBuf> buffers) {
        decode(presenceMapDecoder, buffers, register);
        PresenceMap presenceMap = register.presenceMap;
        assertTrue(presenceMap.getValue(0));
        assertFalse(presenceMap.getValue(1));
        assertTrue(presenceMap.getValue(2));
        assertFalse(presenceMap.getValue(3));
        assertTrue(presenceMap.getValue(4));
        assertFalse(presenceMap.getValue(5));
        assertFalse(presenceMap.getValue(6));
        assertFalse(register.isOverlong);
    }
}
