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
import io.netty.buffer.ByteBuf;

import com.exactpro.junit5.WithByteBuf;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestInt32 {

    private DecodeNullableInt32 nullableInt32Decoder = new DecodeNullableInt32();

    private DecodeMandatoryInt32 mandatoryInt32Decoder = new DecodeMandatoryInt32();

    private UnionRegister register = new UnionRegister();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertTrue(register.isNull);
    }

    @WithByteBuf("81")
    void optionalZero(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(0, register.int32Value);
    }

    @WithByteBuf("80")
    void mandatoryZero(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(0, register.int32Value);
    }

    @WithByteBuf("08 00 00 00 80")
    void testMaxNullable(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(Integer.MAX_VALUE, register.int32Value);
    }

    @WithByteBuf("07 7f 7f 7f ff")
    void testMaxMandatory(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(Integer.MAX_VALUE, register.int32Value);
    }

    @WithByteBuf("78 00 00 00 80")
    void testMinNullable(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(Integer.MIN_VALUE, register.int32Value);
    }

    @WithByteBuf("78 00 00 00 80")
    void testMinMandatory(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(Integer.MIN_VALUE, register.int32Value);
    }

    @WithByteBuf("08 00 00 00 81")
    void testMaxOverflowNullable1(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("08 00 00 00 00 00 00 80")
    void testMaxOverflowNullable2(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("08 00 00 00 80")
    void testMaxOverflowMandatory1(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("07 7f 00 7f 7f 7f ff")
    void testMaxOverflowMandatory2(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("77 7f 7f 7f ff")
    void testMinOverflowNullable1(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("78 00 00 00 00 80")
    void testMinOverflowNullable2(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("77 7f 7f 7f ff")
    void testMinOverflowMandatory1(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("78 00 00 00 00 80")
    void testMinOverflowMandatory2(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("39 45 a4")
    void optionalPositive(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(942755, register.int32Value);
    }

    @WithByteBuf("39 45 a3")
    void mandatoryPositive(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(942755, register.int32Value);
    }

    @WithByteBuf("46 3a dd")
    void optionalNegative(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(-942755, register.int32Value);
    }

    @WithByteBuf("7c 1b 1b 9d")
    void mandatoryNegative(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(-7942755, register.int32Value);
    }

    @WithByteBuf("ff")
    void optionalMinusOne(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(-1, register.int32Value);
    }

    @WithByteBuf("ff")
    void mandatoryMinusOne(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(-1, register.int32Value);
    }

    @WithByteBuf("00 00 40 82")
    void optionalSignExtensionPositive(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(8193, register.int32Value);
    }

    @WithByteBuf("00 00 40 81")
    void mandatorySignExtensionPositive(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(8193, register.int32Value);
    }

    @WithByteBuf("7f 3f ff")
    void optionalSignExtensionNegative(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(-8193, register.int32Value);
    }

    @WithByteBuf("7f 3f ff")
    void mandatorySignExtensionNegative(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(-8193, register.int32Value);
    }

    @WithByteBuf("7f 3f ff 7f 3f ff")
    void mandatoryNegativeTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(-8193, register.int32Value);

        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(-8193, register.int32Value);
    }

    @WithByteBuf("00 00 40 81 00 00 40 81")
    void mandatoryPositiveTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(8193, register.int32Value);

        decode(mandatoryInt32Decoder, buffers, register);
        assertEquals(8193, register.int32Value);
    }

    @WithByteBuf("7f 3f ff 7f 3f ff")
    void optionalNegativeTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(-8193, register.int32Value);

        decode(nullableInt32Decoder, buffers, register);
        assertEquals(-8193, register.int32Value);
    }

    @WithByteBuf("00 00 40 82 00 00 40 82")
    void optionalPositiveTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertEquals(8193, register.int32Value);

        decode(nullableInt32Decoder, buffers, register);
        assertEquals(8193, register.int32Value);
    }

    @WithByteBuf("00 39 45 a4")
    void mandatoryOverlong(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertTrue(register.isOverlong);
        assertEquals(942756, register.int32Value);
    }

    @WithByteBuf("00 40 81")
    void mandatoryNotOverlong(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertFalse(register.isOverlong);
        assertEquals(8193, register.int32Value);
    }

    @WithByteBuf("7f 7c 1b 1b 9d")
    void mandatoryOverlongNegative(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertTrue(register.isOverlong);
        assertEquals(-7942755, register.int32Value);
    }

    @WithByteBuf("7f 3f ff")
    void mandatoryNotOverlongNegative(Collection<ByteBuf> buffers) {
        decode(mandatoryInt32Decoder, buffers, register);
        assertFalse(register.isOverlong);
        assertEquals(-8193, register.int32Value);
    }

    @WithByteBuf("00 39 45 a4")
    void nullableOverlong(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertTrue(register.isOverlong);
        assertEquals(942755, register.int32Value);
    }

    @WithByteBuf("00 40 81")
    void nullableNotOverlong(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertFalse(register.isOverlong);
        assertEquals(8192, register.int32Value);
    }

    @WithByteBuf("7f 7c 1b 1b 9d")
    void nullableOverlongNegative(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertTrue(register.isOverlong);
        assertEquals(-7942755, register.int32Value);
    }

    @WithByteBuf("7f 3f ff")
    void nullableNotOverlongNegative(Collection<ByteBuf> buffers) {
        decode(nullableInt32Decoder, buffers, register);
        assertFalse(register.isOverlong);
        assertEquals(-8193, register.int32Value);
    }
}

