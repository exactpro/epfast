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

package com.exactpro.epfast.decoder.integer;

import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestInt64 {

    private DecodeNullableInt64 nullableInt64Decoder = new DecodeNullableInt64();

    private DecodeMandatoryInt64 mandatoryInt64Decoder = new DecodeMandatoryInt64();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertNull(nullableInt64Decoder.getValue());
    }

    @WithByteBuf("81")
    void optionalZero(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(0, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("80")
    void mandatoryZero(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(0, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("01 00 00 00 00 00 00 00 00 80")
    void testMaxNullable(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(Long.MAX_VALUE, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("00 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void testMaxMandatory(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(Long.MAX_VALUE, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("7f 00 00 00 00 00 00 00 00 80")
    void testMinNullable(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(Long.MIN_VALUE, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("7f 00 00 00 00 00 00 00 00 80")
    void testMinMandatory(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(Long.MIN_VALUE, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("01 00 00 00 00 00 00 00 00 81")
    void testMaxOverflowNullable1(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(IOException.class, () -> nullableInt64Decoder.getValue());
    }

    @WithByteBuf("01 00 00 00 00 00 00 00 00 00 80")
    void testMaxOverflowNullable2(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(IOException.class, () -> nullableInt64Decoder.getValue());
    }

    @WithByteBuf("01 00 00 00 00 00 00 00 00 80")
    void testMaxOverflowMandatory1(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(IOException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("00 7f 00 7f 7f 7f 7f 7f 7f 7f ff")
    void testMaxOverflowMandatory2(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(IOException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("77 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void testMinOverflowNullable1(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(IOException.class, () -> nullableInt64Decoder.getValue());
    }

    @WithByteBuf("7f 00 00 00 00 00 00 00 00 00 80")
    void testMinOverflowNullable2(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(IOException.class, () -> nullableInt64Decoder.getValue());
    }

    @WithByteBuf("77 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void testMinOverflowMandatory1(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(IOException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("7f 00 00 00 00 00 00 00 00 00 80")
    void testMinOverflowMandatory2(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(IOException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("39 45 a4")
    void optionalPositive(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(942755, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("39 45 a3")
    void mandatoryPositive(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(942755, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("46 3a dd")
    void optionalNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-942755, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("7c 1b 1b 9d")
    void mandatoryNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-7942755, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("ff")
    void optionalMinusOne(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-1, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("ff")
    void mandatoryMinusOne(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-1, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("00 00 40 82")
    void optionalSignExtensionPositive(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(8193, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("00 00 40 81")
    void mandatorySignExtensionPositive(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(8193, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("7f 3f ff")
    void optionalSignExtensionNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-8193, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("7f 3f ff")
    void mandatorySignExtensionNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-8193, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("7f 3f ff 7f 3f ff")
    void mandatoryNegativeTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-8193, mandatoryInt64Decoder.getValue());

        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-8193, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("00 00 40 81 00 00 40 81")
    void mandatoryPositiveTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(8193, mandatoryInt64Decoder.getValue());

        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(8193, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("7f 3f ff 7f 3f ff")
    void optionalNegativeTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-8193, nullableInt64Decoder.getValue());

        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-8193, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("00 00 40 82 00 00 40 82")
    void optionalPositiveTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(8193, nullableInt64Decoder.getValue());

        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(8193, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("00 39 45 a4")
    void mandatoryOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverlong());
        assertEquals(942756, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("00 40 81")
    void mandatoryNotOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverlong());
        assertEquals(8193, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("7f 7c 1b 1b 9d")
    void mandatoryOverlongNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverlong());
        assertEquals(-7942755, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("7f 3f ff")
    void mandatoryNotOverlongNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverlong());
        assertEquals(-8193, mandatoryInt64Decoder.getValue());
    }

    @WithByteBuf("00 39 45 a4")
    void nullableOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverlong());
        assertEquals(942755, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("00 40 81")
    void nullableNotOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverlong());
        assertEquals(8192, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("7f 7c 1b 1b 9d")
    void nullableOverlongNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverlong());
        assertEquals(-7942755, nullableInt64Decoder.getValue());
    }

    @WithByteBuf("7f 3f ff")
    void nullableNotOverlongNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverlong());
        assertEquals(-8193, nullableInt64Decoder.getValue());
    }
}
