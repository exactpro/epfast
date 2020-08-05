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

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestUInt32 {

    private DecodeNullableUInt32 nullableUInt32Decoder = new DecodeNullableUInt32();

    private DecodeMandatoryUInt32 mandatoryUInt32Decoder = new DecodeMandatoryUInt32();
    
    private UnionRegister register = new UnionRegister();

    @WithByteBuf("80")
    void testNull(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertTrue(register.isNull);
    }

    @WithByteBuf("81")
    void optionalZero(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(0, register.uInt32Value);
    }

    @WithByteBuf("80")
    void mandatoryZero(Collection<ByteBuf> buffers)  {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(0, register.uInt32Value);
    }

    @WithByteBuf("10 00 00 00 80")
    void testMaxNullable(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(4294967295L, register.uInt32Value);
    }

    @WithByteBuf("0f 7f 7f 7f ff")
    void testMaxMandatory(Collection<ByteBuf> buffers)  {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(4294967295L, register.uInt32Value);
    }

    @WithByteBuf("10 00 00 00 81")
    void testMaxOverflowNullable1(Collection<ByteBuf> buffers) {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("10 00 00 00 00 00 80")
    void testMaxOverflowNullable2(Collection<ByteBuf> buffers) {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("10 00 00 00 80")
    void testMaxOverflowMandatory1(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("0f 7f 7f 7f 7f 00 ff")
    void testMaxOverflowMandatory2(Collection<ByteBuf> buffers) {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertTrue(register.isOverflow);
    }

    @WithByteBuf("39 45 a4")
    void optionalSimpleNumber(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(942755, register.uInt32Value);
    }

    @WithByteBuf("0f 7f 7f 7f ff")
    void optionalSimpleNumber2(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(4294967294L, register.uInt32Value);
    }

    @WithByteBuf("39 45 a3")
    void mandatorySimpleNumber(Collection<ByteBuf> buffers)  {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(942755, register.uInt32Value);
    }

    @WithByteBuf("39 45 a4")
    void optionalSimpleNumberGetValueTwice(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(942755, register.uInt32Value);
        assertEquals(942755, register.uInt32Value);
    }

    @WithByteBuf("39 45 a3")
    void mandatorySimpleNumberGetValueTwice(Collection<ByteBuf> buffers)  {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(942755, register.uInt32Value);
        assertEquals(942755, register.uInt32Value);
    }

    @WithByteBuf("39 45 a4 0f 7f 7f 7f ff")
    void optionalSimpleNumbersTwoValuesInRow(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(942755, register.uInt32Value);

        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(4294967294L, register.uInt32Value);
    }

    @WithByteBuf("39 45 a3 39 45 a3")
    void mandatorySimpleNumbersTwoValuesInRow(Collection<ByteBuf> buffers)  {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(942755, register.uInt32Value);

        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(942755, register.uInt32Value);
    }

    @WithByteBuf("00 39 45 a4")
    void mandatoryOverlong(Collection<ByteBuf> buffers)  {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertTrue(register.isOverlong);
        assertEquals(942756, register.uInt32Value);
    }

    @WithByteBuf("00 40 81")
    void mandatoryNotOverlong(Collection<ByteBuf> buffers)  {
        decode(mandatoryUInt32Decoder, buffers, register);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertFalse(register.isOverlong);
        assertEquals(8193, register.uInt32Value);
    }

    @WithByteBuf("00 39 45 a4")
    void nullableOverlong(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertTrue(register.isOverlong);
        assertEquals(942755, register.uInt32Value);
    }

    @WithByteBuf("00 40 81")
    void nullableNotOverlong(Collection<ByteBuf> buffers)  {
        decode(nullableUInt32Decoder, buffers, register);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(register.isOverlong);
        assertEquals(8192, register.uInt32Value);
    }
}
