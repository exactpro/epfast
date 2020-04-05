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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.junit5.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestDecodeByteVector {

    private DecodeNullableByteVector nullableByteVectorDecoder = new DecodeNullableByteVector();

    private DecodeMandatoryByteVector mandatoryByteVectorDecoder = new DecodeMandatoryByteVector();

    @Test
    void testNull() throws IOException {
        withByteBuf("80", buffers -> {
            decode(nullableByteVectorDecoder, buffers);
            assertTrue(nullableByteVectorDecoder.isReady());
            assertNull(nullableByteVectorDecoder.getValue());
        });
    }

    @Test
    void testNullableZeroLen() throws IOException {
        withByteBuf("81", buffers -> {
            decode(nullableByteVectorDecoder, buffers);
            assertTrue(nullableByteVectorDecoder.isReady());
            assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        });
    }

    @Test
    void testMandatoryZeroLen() throws IOException {
        withByteBuf("80", buffers -> {
            decode(mandatoryByteVectorDecoder, buffers);
            assertTrue(mandatoryByteVectorDecoder.isReady());
            assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        });
    }

    @Test
    void testSimpleNullableVector() throws IOException {
        withByteBuf("87 41 42 42 43 44 45", buffers -> {
            decode(nullableByteVectorDecoder, buffers);
            assertTrue(nullableByteVectorDecoder.isReady());
            assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        });
    }

    @Test
    void testSimpleMandatoryVector() throws IOException {
        withByteBuf("86 41 42 42 43 44 45", buffers -> {
            decode(mandatoryByteVectorDecoder, buffers);
            assertTrue(mandatoryByteVectorDecoder.isReady());
            assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        });
    }

    @Test
    void testNullableZeroLenGetValueTwice() throws IOException {
        withByteBuf("81", buffers -> {
            decode(nullableByteVectorDecoder, buffers);
            assertTrue(nullableByteVectorDecoder.isReady());
            assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
            assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        });
    }

    @Test
    void testMandatoryZeroLenGetValueTwice() throws IOException {
        withByteBuf("80", buffers -> {
            decode(mandatoryByteVectorDecoder, buffers);
            assertTrue(mandatoryByteVectorDecoder.isReady());
            assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
            assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        });
    }

    @Test
    void testSimpleNullableVectorTwoValuesInRow() throws IOException {
        withByteBuf("87 41 42 42 43 44 45 81", buffers -> {
            decode(nullableByteVectorDecoder, buffers);
            assertTrue(nullableByteVectorDecoder.isReady());
            assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));

            decode(nullableByteVectorDecoder, buffers);
            assertTrue(nullableByteVectorDecoder.isReady());
            assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        });
    }

    @Test
    void testSimpleMandatoryVectorTwoValuesInRow() throws IOException {
        withByteBuf("86 41 42 42 43 44 45 80", buffers -> {
            decode(mandatoryByteVectorDecoder, buffers);
            assertTrue(mandatoryByteVectorDecoder.isReady());
            assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));

            decode(mandatoryByteVectorDecoder, buffers);
            assertTrue(mandatoryByteVectorDecoder.isReady());
            assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        });
    }
}
