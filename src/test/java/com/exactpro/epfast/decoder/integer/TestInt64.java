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

package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.message.UnionRegister;
import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestInt64 {

    private DecodeNullableInt64 nullableInt64Decoder = new DecodeNullableInt64();

    private DecodeMandatoryInt64 mandatoryInt64Decoder = new DecodeMandatoryInt64();

    private UnionRegister decodeResult = new UnionRegister();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers) {
        decodeResult.isNull = false;

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("81")
    void optionalZero(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(0, decodeResult.int64Value);
    }

    @WithByteBuf("80")
    void mandatoryZero(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(0, decodeResult.int64Value);
    }

    @WithByteBuf("01 00 00 00 00 00 00 00 00 80")
    void testMaxNullable(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(Long.MAX_VALUE, decodeResult.int64Value);
    }

    @WithByteBuf("00 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void testMaxMandatory(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(Long.MAX_VALUE, decodeResult.int64Value);
    }

    @WithByteBuf("7f 00 00 00 00 00 00 00 00 80")
    void testMinNullable(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(Long.MIN_VALUE, decodeResult.int64Value);
    }

    @WithByteBuf("7f 00 00 00 00 00 00 00 00 80")
    void testMinMandatory(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(Long.MIN_VALUE, decodeResult.int64Value);
    }

    @WithByteBuf("01 00 00 00 00 00 00 00 00 81")
    void testMaxOverflowNullable1(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("01 00 00 00 00 00 00 00 00 00 80")
    void testMaxOverflowNullable2(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("01 00 00 00 00 00 00 00 00 80")
    void testMaxOverflowMandatory1(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("00 7f 00 7f 7f 7f 7f 7f 7f 7f ff")
    void testMaxOverflowMandatory2(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("77 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void testMinOverflowNullable1(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("7f 00 00 00 00 00 00 00 00 00 80")
    void testMinOverflowNullable2(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("77 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void testMinOverflowMandatory1(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("7f 00 00 00 00 00 00 00 00 00 80")
    void testMinOverflowMandatory2(Collection<ByteBuf> buffers) {
        decodeResult.isOverflow = false;

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverflow);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverlong);
    }

    @WithByteBuf("39 45 a4")
    void optionalPositive(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(942755, decodeResult.int64Value);
    }

    @WithByteBuf("39 45 a3")
    void mandatoryPositive(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(942755, decodeResult.int64Value);
    }

    @WithByteBuf("46 3a dd")
    void optionalNegative(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-942755, decodeResult.int64Value);
    }

    @WithByteBuf("7c 1b 1b 9d")
    void mandatoryNegative(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-7942755, decodeResult.int64Value);
    }

    @WithByteBuf("ff")
    void optionalMinusOne(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-1, decodeResult.int64Value);
    }

    @WithByteBuf("ff")
    void mandatoryMinusOne(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-1, decodeResult.int64Value);
    }

    @WithByteBuf("00 40 82")
    void optionalSignExtensionPositive(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(8193, decodeResult.int64Value);
    }

    @WithByteBuf("00 00 40 82")
    void optionalSignExtensionPositiveOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertTrue(decodeResult.isOverlong);
        assertEquals(8193, decodeResult.int64Value);
    }

    @WithByteBuf("00 40 81")
    void mandatorySignExtensionPositive(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(8193, decodeResult.int64Value);
    }

    @WithByteBuf("00 00 40 81")
    void mandatorySignExtensionPositiveOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertTrue(decodeResult.isOverlong);
        assertEquals(8193, decodeResult.int64Value);
    }

    @WithByteBuf("7f 3f ff")
    void optionalSignExtensionNegative(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-8193, decodeResult.int64Value);
    }

    @WithByteBuf("7f 3f ff")
    void mandatorySignExtensionNegative(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-8193, decodeResult.int64Value);
    }

    @WithByteBuf("7f 3f ff 7f 3f ff")
    void mandatoryNegativeTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-8193, decodeResult.int64Value);

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-8193, decodeResult.int64Value);
    }

    @WithByteBuf("00 40 81 00 40 81")
    void mandatoryPositiveTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(8193, decodeResult.int64Value);

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(8193, decodeResult.int64Value);
    }

    @WithByteBuf("7f 3f ff 7f 3f ff")
    void optionalNegativeTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-8193, decodeResult.int64Value);

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(-8193, decodeResult.int64Value);
    }

    @WithByteBuf("00 40 82 00 40 82")
    void optionalPositiveTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(8193, decodeResult.int64Value);

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertFalse(decodeResult.isOverlong);
        assertEquals(8193, decodeResult.int64Value);
    }

    @WithByteBuf("00 39 45 a4")
    void mandatoryOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertEquals(942756, decodeResult.int64Value);
    }

    @WithByteBuf("00 40 81")
    void mandatoryNotOverlong(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertEquals(8193, decodeResult.int64Value);
    }

    @WithByteBuf("7f 7c 1b 1b 9d")
    void mandatoryOverlongNegative(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertEquals(-7942755, decodeResult.int64Value);
    }

    @WithByteBuf("7f 3f ff")
    void mandatoryNotOverlongNegative(Collection<ByteBuf> buffers) {
        decode(mandatoryInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertEquals(-8193, decodeResult.int64Value);
    }

    @WithByteBuf("00 39 45 a4")
    void nullableOverlong(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertEquals(942755, decodeResult.int64Value);
    }

    @WithByteBuf("00 40 81")
    void nullableNotOverlong(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertEquals(8192, decodeResult.int64Value);
    }

    @WithByteBuf("7f 7c 1b 1b 9d")
    void nullableOverlongNegative(Collection<ByteBuf> buffers) {
        decodeResult.isOverlong = false;

        decode(nullableInt64Decoder, buffers, decodeResult);
        assertTrue(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertEquals(-7942755, decodeResult.int64Value);
    }

    @WithByteBuf("7f 3f ff")
    void nullableNotOverlongNegative(Collection<ByteBuf> buffers) {
        decode(nullableInt64Decoder, buffers, decodeResult);
        assertFalse(decodeResult.isOverlong);
        assertFalse(decodeResult.isNull);
        assertFalse(decodeResult.isOverflow);
        assertEquals(-8193, decodeResult.int64Value);
    }

    @BeforeEach
    void resetRegisterFlags() {
        decodeResult.isOverlong = true;
        decodeResult.isNull = true;
        decodeResult.isOverflow = true;
        decodeResult.int64Value = -999;
    }
}
