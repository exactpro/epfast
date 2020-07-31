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

package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.message.UnionRegister;
import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestDecodeByteVector {

    private DecodeNullableByteVector nullableByteVectorDecoder = new DecodeNullableByteVector();

    private DecodeMandatoryByteVector mandatoryByteVectorDecoder = new DecodeMandatoryByteVector();

    private UnionRegister register = new UnionRegister();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers, register);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertNull(register.byteVectorValue);
    }

    @WithByteBuf("81")
    void testNullableZeroLen(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers, register);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(register.byteVectorValue, StandardCharsets.UTF_8));
    }

    @WithByteBuf("80")
    void testMandatoryZeroLen(Collection<ByteBuf> buffers) {
        decode(mandatoryByteVectorDecoder, buffers, register);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(register.byteVectorValue, StandardCharsets.UTF_8));
    }

    @WithByteBuf("10 00 00 00 81 41 42 42 43 44 45")
    void testNullableLengthOverflow1(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers, register);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("10 00 00 00 00 00 80 41 42 42 43 44 45")
    void testNullableLengthOverflow2(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers, register);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("10 00 00 00 80 41 42 42 43 44 45")
    void testMandatoryLengthOverflow1(Collection<ByteBuf> buffers) {
        decode(mandatoryByteVectorDecoder, buffers, register);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("0f 7f 7f 7f 7f 00 ff 41 42 42 43 44 45")
    void testMandatoryLengthOverflow2(Collection<ByteBuf> buffers) {
        decode(mandatoryByteVectorDecoder, buffers, register);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("87 41 42 42 43 44 45")
    void testSimpleNullableVector(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers, register);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(register.byteVectorValue, StandardCharsets.UTF_8));
    }

    @WithByteBuf("86 41 42 42 43 44 45")
    void testSimpleMandatoryVector(Collection<ByteBuf> buffers) {
        decode(mandatoryByteVectorDecoder, buffers, register);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(register.byteVectorValue, StandardCharsets.UTF_8));
    }

    @WithByteBuf("81")
    void testNullableZeroLenGetValueTwice(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers, register);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(register.byteVectorValue, StandardCharsets.UTF_8));
        assertEquals("", new String(register.byteVectorValue, StandardCharsets.UTF_8));
    }

    @WithByteBuf("80")
    void testMandatoryZeroLenGetValueTwice(Collection<ByteBuf> buffers) {
        decode(mandatoryByteVectorDecoder, buffers, register);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(register.byteVectorValue, StandardCharsets.UTF_8));
        assertEquals("", new String(register.byteVectorValue, StandardCharsets.UTF_8));
    }

    @WithByteBuf("87 41 42 42 43 44 45 81")
    void testSimpleNullableVectorTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers, register);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(register.byteVectorValue, StandardCharsets.UTF_8));

        decode(nullableByteVectorDecoder, buffers, register);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(register.byteVectorValue, StandardCharsets.UTF_8));
    }

    @WithByteBuf("86 41 42 42 43 44 45 80")
    void testSimpleMandatoryVectorTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(mandatoryByteVectorDecoder, buffers, register);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(register.byteVectorValue, StandardCharsets.UTF_8));

        decode(mandatoryByteVectorDecoder, buffers, register);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(register.byteVectorValue, StandardCharsets.UTF_8));
    }
}
