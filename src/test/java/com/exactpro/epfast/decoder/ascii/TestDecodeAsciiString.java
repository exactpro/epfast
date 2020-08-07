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

package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.message.UnionRegister;
import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.junit5.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestDecodeAsciiString {

    private final UnionRegister decodeResult = new UnionRegister();

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
    class TestOptional {
        private final DecodeNullableAsciiString decoder = new DecodeNullableAsciiString(true);

        @WithByteBuf("80")
        void testNull(Collection<ByteBuf> buffers) {
            decodeResult.isNull = false;
            decodeResult.stringValue = "";

            decode(decoder, buffers, decodeResult);
            assertNull(decodeResult.stringValue);
            assertTrue(decodeResult.isNull);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isOverlong);
        }

        @WithByteBuf("00 80")
        void testOptionalEmptyString(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("", decodeResult.stringValue);
        }

        @WithByteBuf("41 42 c3")
        void testOptionalSimpleString(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("ABC", decodeResult.stringValue);
        }

        @WithByteBuf("00 C1")
        void testOptionalOverlongString1(Collection<ByteBuf> buffers) {
            decodeResult.isOverlong = false;

            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertTrue(decodeResult.isOverlong);
            assertEquals("A", decodeResult.stringValue);
        }

        @WithByteBuf("00 00 C1")
        void testOptionalOverlongString2(Collection<ByteBuf> buffers) {
            decodeResult.isOverlong = false;

            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertTrue(decodeResult.isOverlong);
            assertEquals("A", decodeResult.stringValue);
        }

        @WithByteBuf("00 00 00 00 80")
        void testOptionalStringAllZeros(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0\0\0", decodeResult.stringValue);
        }

        @WithByteBuf("41 42 c3 42 42 c3 41 44 c3")
        void testOptionalStringDecoderReuse(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("ABC", decodeResult.stringValue);

            resetRegisterFlags();
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("BBC", decodeResult.stringValue);

            resetRegisterFlags();
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("ADC", decodeResult.stringValue);
        }

        @WithByteBuf("00 00 00 C1")
        void testOptionalStringStartedWithZero(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0A", decodeResult.stringValue);
        }

        @Nested
        class TestOverflowException {
            private final DecodeNullableAsciiString decoder = new DecodeNullableAsciiString(true);

            @WithByteBuf("00 00 00 C1")
            void testOptionalOverlong1(Collection<ByteBuf> buffers) {
                decode(decoder, buffers, decodeResult);
                assertFalse(decodeResult.isOverflow);
                assertFalse(decodeResult.isNull);
                assertFalse(decodeResult.isOverlong);
                assertEquals("\0A", decodeResult.stringValue);
            }

            @Test
            void testOptionalOverlong2() {
                decodeResult.isOverflow = false;

                decoder.decode(
                    fromHex(fastAsciiStringOf(')', 2 * DecodeAsciiString.MAX_ALLOWED_LENGTH)),
                    decodeResult);
                assertTrue(decodeResult.isOverflow);
                assertFalse(decodeResult.isNull);
                assertFalse(decodeResult.isOverlong);
            }
        }

        @WithByteBuf("00 00 80 00 00 00 00 80")
        void testOptionalStringTwoValuesInRow(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0", decodeResult.stringValue);

            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0\0\0", decodeResult.stringValue);
        }
    }

    @Nested
    class TestMandatory {
        private final DecodeMandatoryAsciiString decoder = new DecodeMandatoryAsciiString(true);

        @WithByteBuf("80")
        void testMandatoryEmptyString(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("", decodeResult.stringValue);
        }

        @WithByteBuf("00 00 80")
        void testMandatoryStringAllZeros1(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0\0", decodeResult.stringValue);
        }

        @WithByteBuf("00 00 00 00 80")
        void testMandatoryStringAllZeros2(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0\0\0\0", decodeResult.stringValue);
        }

        @WithByteBuf("00 C1")
        void testMandatoryOverlongString(Collection<ByteBuf> buffers) {
            decodeResult.isOverlong = false;

            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertTrue(decodeResult.isOverlong);
            assertEquals("A", decodeResult.stringValue);
        }

        @WithByteBuf("00 00 C1")
        void testMandatoryStringStartedWithZero(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0A", decodeResult.stringValue);
        }

        @Nested
        class TestOverflowException {
            private final DecodeMandatoryAsciiString decoder = new DecodeMandatoryAsciiString(true);

            @WithByteBuf("00 C1")
            void testMandatoryOverlong1(Collection<ByteBuf> buffers) {
                decodeResult.isOverlong = false;

                decode(decoder, buffers, decodeResult);
                assertFalse(decodeResult.isOverflow);
                assertFalse(decodeResult.isNull);
                assertTrue(decodeResult.isOverlong);
                assertEquals("A", decodeResult.stringValue);
            }

            @Test
            void testMandatoryOverlong2() {
                decodeResult.isOverflow = false;

                decoder.decode(
                    fromHex(fastAsciiStringOf('*', 3 * DecodeAsciiString.MAX_ALLOWED_LENGTH)),
                    decodeResult);
                assertTrue(decodeResult.isOverflow);
                assertFalse(decodeResult.isNull);
                assertFalse(decodeResult.isOverlong);
            }
        }

        @WithByteBuf("00 00 80 00 00 00 00 80")
        void testMandatoryStringTwoValuesInRow(Collection<ByteBuf> buffers) {
            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0\0", decodeResult.stringValue);

            decode(decoder, buffers, decodeResult);
            assertFalse(decodeResult.isOverflow);
            assertFalse(decodeResult.isNull);
            assertFalse(decodeResult.isOverlong);
            assertEquals("\0\0\0\0", decodeResult.stringValue);
        }
    }

    @BeforeEach
    void resetRegisterFlags() {
        decodeResult.isOverlong = true;
        decodeResult.isNull = true;
        decodeResult.isOverflow = true;
        decodeResult.stringValue = null;
    }
}
