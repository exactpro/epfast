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

package com.exactpro.epfast.decoder.decimal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;

import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestDecodeDecimal {

    private DecodeNullableDecimal nullableDecimalDecoder = new DecodeNullableDecimal();

    private DecodeMandatoryDecimal mandatoryDecimalDecoder = new DecodeMandatoryDecimal();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertNull(nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("83 39 45 a3")
    void testNullablePositive1(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("82 04 3f 34 de")
    void testNullablePositive(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("82 39 45 a3")
    void testMandatoryPositive(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertFalse(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("81 04 3f 34 de")
    void testMandatoryPositive2(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertFalse(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("fe 39 45 a3")
    void testMandatoryPositive3(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertFalse(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("9427.55"), mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("fe 39 45 a3")
    void testNullablePositive3(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("9427.55"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("fe 46 3a dd")
    void testNullableNegative(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("-9427.55"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("fd 7f 3f ff")
    void testNullableNegativeSignExtension(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("-8.193"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("39 45 a4 7f 3f ff")
    void testExponentIOException(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertThrows(IOException.class, () -> nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("82 04 3f 34 de")
    void testNullablePositiveGetValueTwice(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("82 39 45 a3")
    void testMandatoryPositiveGetValueTwice(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertFalse(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("83 39 45 a3 82 04 3f 34 de")
    void testNullablePositiveTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());

        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertFalse(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("82 39 45 a3 81 04 3f 34 de")
    void testMandatoryPositiveTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertFalse(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());

        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertFalse(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("08 00 00 00 81 39 45 a3")
    void testNullableExponentOverflow1(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertThrows(IOException.class, () -> nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("08 00 00 00 80 39 45 a3")
    void testMandatoryExponentOverflow1(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertThrows(IOException.class, () -> mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("08 00 00 00 00 81 39 45 a3")
    void testNullableExponentOverflow2(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertThrows(IOException.class, () -> nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("07 7f 00 7f 7f 7f ff 39 45 a3")
    void testMandatoryExponentOverflow2(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertThrows(IOException.class, () -> mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("86 01 00 00 00 00 00 00 00 00 80")
    void testNullableMantissaOverflow1(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertThrows(IOException.class, () -> nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("86 01 00 00 00 00 00 00 00 00 80")
    void testMandatoryMantissaOverflow1(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertThrows(IOException.class, () -> mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("86 00 7f 00 7f 7f 7f 7f 7f 7f 7f ff")
    void testNullableMantissaOverflow2(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertThrows(IOException.class, () -> nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("86 00 7f 00 7f 7f 7f 7f 7f 7f 7f ff")
    void testMandatoryMantissaOverflow2(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertThrows(IOException.class, () -> mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("00 83 39 45 a3")
    void testNullableExponentOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertTrue(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("7f fe 46 3a dd")
    void testNullableNegativeExponentOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertTrue(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("-9427.55"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("00 82 39 45 a3")
    void testMandatoryExponentOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertTrue(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("7f fe 46 3a dd")
    void testMandatoryNegativeExponentOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertTrue(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("-9427.55"), mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("fe 7f 46 3a dd")
    void testNullableNegativeMantissaOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertTrue(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("-9427.55"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("83 00 39 45 a3")
    void testNullableMantissaOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(nullableDecimalDecoder, buffers);
        assertTrue(nullableDecimalDecoder.isReady());
        assertTrue(nullableDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @WithByteBuf("82 00 39 45 a3")
    void testMandatoryMantissaOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertTrue(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @WithByteBuf("fe 7f 46 3a dd")
    void testMandatoryNegativeMantissaOverlong(Collection<ByteBuf> buffers) throws IOException {
        decode(mandatoryDecimalDecoder, buffers);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertTrue(mandatoryDecimalDecoder.isOverlong());
        assertEquals(new BigDecimal("-9427.55"), mandatoryDecimalDecoder.getValue());
    }
}
