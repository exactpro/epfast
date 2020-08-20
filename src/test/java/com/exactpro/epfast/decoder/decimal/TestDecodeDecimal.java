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

import java.math.BigDecimal;
import java.util.Collection;

import com.exactpro.epfast.decoder.message.UnionRegister;
import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.decoder.DecoderUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TestDecodeDecimal {

    private DecodeNullableDecimal nullableDecimalDecoder = new DecodeNullableDecimal();

    private DecodeMandatoryDecimal mandatoryDecimalDecoder = new DecodeMandatoryDecimal();

    private UnionRegister decodeResult = new UnionRegister();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers) {
        decodeResult.isNull = false;
        decodeResult.decimalValue = new BigDecimal(1);

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertTrue(decodeResult.isNull);
    }

    @WithByteBuf("83 39 45 a3")
    void testNullablePositive1(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("82 04 3f 34 de")
    void testNullablePositive(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("82 39 45 a3")
    void testMandatoryPositive(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("81 04 3f 34 de")
    void testMandatoryPositive2(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("fe 39 45 a3")
    void testMandatoryPositive3(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("9427.55"), decodeResult.decimalValue);
    }

    @WithByteBuf("fe 39 45 a3")
    void testNullablePositive3(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("9427.55"), decodeResult.decimalValue);
    }

    @WithByteBuf("fe 46 3a dd")
    void testNullableNegative(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("-9427.55"), decodeResult.decimalValue);
    }

    @WithByteBuf("fd 7f 3f ff")
    void testNullableNegativeSignExtension(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("-8.193"), decodeResult.decimalValue);
    }

    @WithByteBuf("39 45 a4 7f 3f ff")
    void testExponentIOException(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("82 04 3f 34 de")
    void testNullablePositiveGetValueTwice(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("82 39 45 a3")
    void testMandatoryPositiveGetValueTwice(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("83 39 45 a3 82 04 3f 34 de")
    void testNullablePositiveTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("82 39 45 a3 81 04 3f 34 de")
    void testMandatoryPositiveTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("08 00 00 00 81 39 45 a3")
    void testNullableExponentOverflow1(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("08 00 00 00 80 39 45 a3")
    void testMandatoryExponentOverflow1(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("08 00 00 00 00 81 39 45 a3")
    void testNullableExponentOverflow2(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("07 7f 00 7f 7f 7f ff 39 45 a3")
    void testMandatoryExponentOverflow2(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("86 01 00 00 00 00 00 00 00 00 80")
    void testNullableMantissaOverflow1(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("86 01 00 00 00 00 00 00 00 00 80")
    void testMandatoryMantissaOverflow1(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("86 00 7f 00 7f 7f 7f 7f 7f 7f 7f ff")
    void testNullableMantissaOverflow2(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = true;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("86 00 7f 00 7f 7f 7f 7f 7f 7f 7f ff")
    void testMandatoryMantissaOverflow2(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
    }

    @WithByteBuf("00 83 39 45 a3")
    void testNullableExponentOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("7f fe 46 3a dd")
    void testNullableNegativeExponentOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("-9427.55"), decodeResult.decimalValue);
    }

    @WithByteBuf("00 82 39 45 a3")
    void testMandatoryExponentOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("7f fe 46 3a dd")
    void testMandatoryNegativeExponentOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("-9427.55"), decodeResult.decimalValue);
    }

    @WithByteBuf("fe 7f 46 3a dd")
    void testNullableNegativeMantissaOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("-9427.55"), decodeResult.decimalValue);
    }

    @WithByteBuf("83 00 39 45 a3")
    void testNullableMantissaOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(nullableDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("82 00 39 45 a3")
    void testMandatoryMantissaOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("94275500"), decodeResult.decimalValue);
    }

    @WithByteBuf("fe 7f 46 3a dd")
    void testMandatoryNegativeMantissaOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(mandatoryDecimalDecoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertEquals(new BigDecimal("-9427.55"), decodeResult.decimalValue);
    }

    @BeforeEach
    void resetRegisterFlags() {
        decodeResult.isOverlong = true;
        decodeResult.isNull = true;
        decodeResult.isOverflow = true;
        decodeResult.decimalValue = null;
    }
}
