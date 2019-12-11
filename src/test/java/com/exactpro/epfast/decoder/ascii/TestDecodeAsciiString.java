package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.OverflowException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidParameterException;

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

        @Test
        void testNull() throws IOException {
            withByteBuf("80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertNull(decoder.getValue());
            });
        }

        @Test
        void testOptionalEmptyString() throws IOException {
            withByteBuf("00 80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("", decoder.getValue());
            });
        }

        @Test
        void testSimpleString() throws IOException {
            withByteBuf("41 42 c3", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("ABC", decoder.getValue());
            });
        }

        @Test
        void testZeroByteStringNullable1() throws IOException {
            withByteBuf("00 00 80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("\0", decoder.getValue());
            });
        }

        @Test
        void testZeroByteStringNullable2() throws IOException {
            withByteBuf("00 00 00 00 80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("\0\0\0", decoder.getValue());
            });
        }

        @Test
        void testNullableReuse() throws IOException {
            withByteBuf("41 42 c3 42 42 c3 41 44 c3", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("ABC", decoder.getValue());
                decode(decoder, buffers);
                assertEquals("BBC", decoder.getValue());
                decode(decoder, buffers);
                assertEquals("ADC", decoder.getValue());
            });
        }

        @Test
        void testSimpleStringGetValueTwice() throws IOException {
            withByteBuf("41 42 c3", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("ABC", decoder.getValue());
                assertEquals("ABC", decoder.getValue());
            });
        }

        @Nested
        class TestOverflowException {

            private DecodeNullableAsciiString decoder = new DecodeNullableAsciiString(true);

            @Test
            void testNullableOverlong1() throws IOException {
                withByteBuf("00 00 00 81", buffers -> {
                    decode(decoder, buffers);
                    assertTrue(decoder.isReady());
                    assertThrows(OverflowException.class, () -> decoder.getValue());
                });
            }

            @Test
            void testNullableOverlong2() {
                decoder.decode(fromHex(fastAsciiStringOf(')', 2 * DecodeAsciiString.MAX_ALLOWED_LENGTH)));
                assertTrue(decoder.isReady());
                assertThrows(OverflowException.class, () -> decoder.getValue());
            }
        }

        @Test
        void testZeroByteStringNullableTwoValuesInRow() throws IOException {
            withByteBuf("00 00 80 00 00 00 00 80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("\0", decoder.getValue());

                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("\0\0\0", decoder.getValue());
            });
        }
    }

    @Nested
    class TestMandatory {

        private DecodeMandatoryAsciiString decoder = new DecodeMandatoryAsciiString();

        @Test
        void testMandatoryEmptyString() throws IOException {
            withByteBuf("80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("", decoder.getValue());
            });
        }

        @Test
        void testZeroByteStringMandatory1() throws IOException {
            withByteBuf("00 00 80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("\0\0", decoder.getValue());
            });
        }

        @Test
        void testZeroByteStringMandatory2() throws IOException {
            withByteBuf("00 00 00 00 80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("\0\0\0\0", decoder.getValue());
            });
        }

        @Test
        void testMandatoryEmptyStringGetValueTwice() throws IOException {
            withByteBuf("80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("", decoder.getValue());
                assertEquals("", decoder.getValue());
            });
        }

        @Nested
        class TestOverflowException {

            private DecodeMandatoryAsciiString decoder = new DecodeMandatoryAsciiString(true);

            @Test
            void testMandatoryOverlong1() throws IOException {
                withByteBuf("00 81", buffers -> {
                    decode(decoder, buffers);
                    assertTrue(decoder.isReady());
                    assertThrows(OverflowException.class, () -> decoder.getValue());
                });
            }

            @Test
            void testMandatoryOverlong2() {
                decoder.decode(fromHex(fastAsciiStringOf('*', 3 * DecodeAsciiString.MAX_ALLOWED_LENGTH)));
                assertTrue(decoder.isReady());
                assertThrows(OverflowException.class, () -> decoder.getValue());
            }
        }

        @Test
        void testZeroByteStringMandatoryTwoValuesInRow() throws IOException {
            withByteBuf("00 00 80 00 00 00 00 80", buffers -> {
                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("\0\0", decoder.getValue());

                decode(decoder, buffers);
                assertTrue(decoder.isReady());
                assertEquals("\0\0\0\0", decoder.getValue());
            });
        }
    }
}
