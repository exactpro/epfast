/******************************************************************************
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
 ******************************************************************************/

package com.exactpro.epfast.decoder.unicode;

import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestDecodeByteVector {

    private DecodeNullableByteVector nullableByteVectorDecoder = new DecodeNullableByteVector();

    private DecodeMandatoryByteVector mandatoryByteVectorDecoder = new DecodeMandatoryByteVector();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableByteVectorDecoder, buffers);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertNull(nullableByteVectorDecoder.getValue());
    }

    @WithByteBuf("81")
    void testNullableZeroLen(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableByteVectorDecoder, buffers);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @WithByteBuf("80")
    void testMandatoryZeroLen(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryByteVectorDecoder, buffers);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @WithByteBuf("10 00 00 00 81 41 42 42 43 44 45")
    void testNullableLengthOverflow1(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertThrows(IOException.class, () -> nullableByteVectorDecoder.getValue());
    }

    @WithByteBuf("10 00 00 00 00 00 80 41 42 42 43 44 45")
    void testNullableLengthOverflow2(Collection<ByteBuf> buffers) {
        decode(nullableByteVectorDecoder, buffers);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertThrows(IOException.class, () -> nullableByteVectorDecoder.getValue());
    }

    @WithByteBuf("10 00 00 00 80 41 42 42 43 44 45")
    void testMandatoryLengthOverflow1(Collection<ByteBuf> buffers) {
        decode(mandatoryByteVectorDecoder, buffers);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertThrows(IOException.class, () -> mandatoryByteVectorDecoder.getValue());
    }

    @WithByteBuf("0f 7f 7f 7f 7f 00 ff 41 42 42 43 44 45")
    void testMandatoryLengthOverflow2(Collection<ByteBuf> buffers) {
        decode(mandatoryByteVectorDecoder, buffers);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertThrows(IOException.class, () -> mandatoryByteVectorDecoder.getValue());
    }

    @WithByteBuf("87 41 42 42 43 44 45")
    void testSimpleNullableVector(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableByteVectorDecoder, buffers);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @WithByteBuf("86 41 42 42 43 44 45")
    void testSimpleMandatoryVector(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryByteVectorDecoder, buffers);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @WithByteBuf("81")
    void testNullableZeroLenGetValueTwice(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableByteVectorDecoder, buffers);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @WithByteBuf("80")
    void testMandatoryZeroLenGetValueTwice(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryByteVectorDecoder, buffers);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @WithByteBuf("87 41 42 42 43 44 45 81")
    void testSimpleNullableVectorTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableByteVectorDecoder, buffers);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));

        decode(nullableByteVectorDecoder, buffers);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @WithByteBuf("86 41 42 42 43 44 45 80")
    void testSimpleMandatoryVectorTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryByteVectorDecoder, buffers);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));

        decode(mandatoryByteVectorDecoder, buffers);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }
}
