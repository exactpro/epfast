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

package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.junit5.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestDecodeAsciiString {

    static String fastAsciiStringOf(char character, int length) {
        if (character > 0 && character < 128) {
            StringBuilder stringBuilder = new StringBuilder(length);
            for (int i = 1; i < length; i++) {
                stringBuilder.append(Integer.toHexString(character)).append(' ');
            }
            stringBuilder.append(Integer.toHexString(character | 0x80));
            return stringBuilder.toString();
        }
        throw new InvalidParameterException("Character must be in range [1, 127]");
    }

    @Nested
    class TestNullable {

        private DecodeNullableAsciiString decoder = new DecodeNullableAsciiString();

        @WithByteBuf("80")
        void testNull(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertNull(decoder.getValue());
            assertFalse(decoder.isOverlong());
        }

        @WithByteBuf("00 80")
        void testOptionalEmptyString(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertEquals("", decoder.getValue());
            assertFalse(decoder.isOverlong());
        }

        @WithByteBuf("41 42 c3")
        void testSimpleString(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertEquals("ABC", decoder.getValue());
            assertFalse(decoder.isOverlong());
        }

        @WithByteBuf("00 00 80")
        void testZeroByteStringNullable1(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertEquals("\0", decoder.getValue());
            assertFalse(decoder.isOverlong());
        }

        @WithByteBuf("00 00 00 00 80")
        void testZeroByteStringNullable2(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertEquals("\0\0\0", decoder.getValue());
            assertFalse(decoder.isOverlong());
        }

        @WithByteBuf("41 42 c3 42 42 c3 41 44 c3")
        void testNullableReuse(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("ABC", decoder.getValue());
            decode(decoder, buffers);
            assertEquals("BBC", decoder.getValue());
            decode(decoder, buffers);
            assertEquals("ADC", decoder.getValue());
        }

        @WithByteBuf("41 42 c3")
        void testSimpleStringGetValueTwice(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("ABC", decoder.getValue());
            assertEquals("ABC", decoder.getValue());
        }

        @WithByteBuf("00 00 00 81")
        void testNullableOverlongNoException(Collection<ByteBuf> buffers) {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertTrue(decoder.isOverlong());
        }

        @Nested
        class TestOverflowException {

            private DecodeNullableAsciiString decoder = new DecodeNullableAsciiString(true);

            @WithByteBuf("00 00 00 81")
            void testNullableOverlong1(Collection<ByteBuf> buffers) {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertTrue(decoder.isOverlong());
                assertThrows(OverflowException.class, () -> decoder.getValue());
            }

            @Test
            void testNullableOverlong2() {
                decoder.decode(fromHex(fastAsciiStringOf(')', 2 * DecodeAsciiString.MAX_ALLOWED_LENGTH)));
                assertTrue(decoder.isReady());
                assertThrows(OverflowException.class, () -> decoder.getValue());
            }
        }

        @WithByteBuf("00 00 80 00 00 00 00 80")
        void testZeroByteStringNullableTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("\0", decoder.getValue());

            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("\0\0\0", decoder.getValue());
        }
    }

    @Nested
    class TestMandatory {

        private DecodeMandatoryAsciiString decoder = new DecodeMandatoryAsciiString();

        @WithByteBuf("80")
        void testMandatoryEmptyString(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("", decoder.getValue());
        }

        @WithByteBuf("00 00 80")
        void testZeroByteStringMandatory1(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("\0\0", decoder.getValue());
        }

        @WithByteBuf("00 00 00 00 80")
        void testZeroByteStringMandatory2(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("\0\0\0\0", decoder.getValue());
        }

        @WithByteBuf("80")
        void testMandatoryEmptyStringGetValueTwice(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("", decoder.getValue());
            assertEquals("", decoder.getValue());
        }

        @WithByteBuf("00 00 00 81")
        void testMandatoryOverlongNoException(Collection<ByteBuf> buffers) {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertTrue(decoder.isOverlong());
        }

        @Nested
        class TestOverflowException {

            private DecodeMandatoryAsciiString decoder = new DecodeMandatoryAsciiString(true);

            @WithByteBuf("00 81")
            void testMandatoryOverlong1(Collection<ByteBuf> buffers) {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertTrue(decoder.isOverlong());
                assertThrows(OverflowException.class, () -> decoder.getValue());
            }

            @Test
            void testMandatoryOverlong2() {
                decoder.decode(fromHex(fastAsciiStringOf('*', 3 * DecodeAsciiString.MAX_ALLOWED_LENGTH)));
                assertTrue(decoder.isReady());
                assertThrows(OverflowException.class, () -> decoder.getValue());
            }
        }

        @WithByteBuf("00 00 80 00 00 00 00 80")
        void testZeroByteStringMandatoryTwoValuesInRow(Collection<ByteBuf> buffers) throws IOException {
            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("\0\0", decoder.getValue());

            decode(decoder, buffers);
            assertTrue(decoder.isReady());
            assertFalse(decoder.isOverlong());
            assertEquals("\0\0\0\0", decoder.getValue());
        }
    }
}
