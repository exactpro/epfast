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

import java.math.BigInteger;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestUInt64 {

    private DecodeNullableUInt64 nullableUInt64Decoder = new DecodeNullableUInt64();

    private DecodeMandatoryUInt64 mandatoryUInt64Decoder = new DecodeMandatoryUInt64();

    private UnionRegister register = new UnionRegister();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertTrue(register.isNull);
    }

    @WithByteBuf("81")
    void optionalZero(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("0"), register.unsignedInt64Value);
    }

    @WithByteBuf("80")
    void mandatoryZero(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("0"), register.unsignedInt64Value);
    }

    @WithByteBuf("02 00 00 00 00 00 00 00 00 80")
    void testMaxNullable(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("18446744073709551615"), register.unsignedInt64Value);
    }

    @WithByteBuf("01 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void testMaxMandatory(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("18446744073709551615"), register.unsignedInt64Value);
    }

    @WithByteBuf("02 00 00 00 00 00 00 00 00 81")
    void testMaxOverflowNullable1(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("02 00 00 00 00 00 00 00 00 00 80")
    void testMaxOverflowNullable2(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("02 00 00 00 00 00 00 00 00 80")
    void testMaxOverflowMandatory1(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("01 7f 7f 7f 7f 00 7f 7f 7f 7f ff")
    void testMaxOverflowMandatory2(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("39 45 a4")
    void optionalSimpleNumber1(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);
    }

    @WithByteBuf("01 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void optionalSimpleNumber2(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("18446744073709551614"), register.unsignedInt64Value);
    }

    @WithByteBuf("39 45 a3")
    void mandatorySimpleNumber1(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);
    }

    @WithByteBuf("01 10 78 20 76 62 2a 62 51 cf")
    void mandatorySimpleNumber2(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("10443992354206034127"), register.unsignedInt64Value);
    }

    @WithByteBuf("39 45 a4")
    void optionalSimpleNumber1GetValueTwice(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);
    }

    @WithByteBuf("39 45 a3")
    void mandatorySimpleNumber1GetValueTwice(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);
    }

    @WithByteBuf("39 45 a4 01 7f 7f 7f 7f 7f 7f 7f 7f ff")
    void optionalSimpleNumbersTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);

        decode(nullableUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("18446744073709551614"), register.unsignedInt64Value);
    }

    @WithByteBuf("39 45 a3 01 10 78 20 76 62 2a 62 51 cf")
    void mandatorySimpleNumbersTwoValuesInRow(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);

        decode(mandatoryUInt64Decoder, buffers, register);
        assertEquals(new BigInteger("10443992354206034127"), register.unsignedInt64Value);
    }

    @WithByteBuf("00 39 45 a4")
    void mandatoryOverlong(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertTrue(register.isOverlong);
        assertEquals(new BigInteger("942756"), register.unsignedInt64Value);
    }

    @WithByteBuf("00 40 81")
    void mandatoryNotOverlong(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt64Decoder, buffers, register);
        assertFalse(register.isOverlong);
        assertEquals(new BigInteger("8193"), register.unsignedInt64Value);
    }

    @WithByteBuf("00 39 45 a4")
    void nullableOverlong(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertTrue(register.isOverlong);
        assertEquals(new BigInteger("942755"), register.unsignedInt64Value);
    }

    @WithByteBuf("00 40 81")
    void nullableNotOverlong(Collection<ByteBuf> buffers) {
        decode(nullableUInt64Decoder, buffers, register);
        assertFalse(register.isOverlong);
        assertEquals(new BigInteger("8192"), register.unsignedInt64Value);
    }
}
