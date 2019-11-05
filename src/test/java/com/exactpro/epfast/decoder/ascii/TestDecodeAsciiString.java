package com.exactpro.epfast.decoder.ascii;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestDecodeAsciiString {

    private DecodeNullableAsciiString nullableStringDecoder = new DecodeNullableAsciiString();

    private DecodeMandatoryAsciiString mandatoryStringDecoder = new DecodeMandatoryAsciiString();

    @Test
    void testNull() throws IOException {
        withByteBuf("80", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertNull(nullableStringDecoder.getValue());
        });
    }

    @Test
    void testOptionalEmptyString() throws IOException {
        withByteBuf("00 80", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertEquals("", nullableStringDecoder.getValue());
        });
    }

    @Test
    void testMandatoryEmptyString() throws IOException {
        withByteBuf("80", buffers -> {
            decode(mandatoryStringDecoder, buffers);
            assertTrue(mandatoryStringDecoder.isReady());
            assertEquals("", mandatoryStringDecoder.getValue());
        });
    }

    @Test
    void testSimpleString() throws IOException {
        withByteBuf("41 42 c3", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertEquals("ABC", nullableStringDecoder.getValue());
        });
    }

    @Test
    void testZeroByteStringNullable1() throws IOException {
        withByteBuf("00 00 80", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertEquals("\0", nullableStringDecoder.getValue());
        });
    }

    @Test
    void testZeroByteStringNullable2() throws IOException {
        withByteBuf("00 00 00 00 80", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertEquals("\0\0\0", nullableStringDecoder.getValue());
        });
    }

    @Test
    void testZeroByteStringMandatory1() throws IOException {
        withByteBuf("00 00 80", buffers -> {
            decode(mandatoryStringDecoder, buffers);
            assertTrue(mandatoryStringDecoder.isReady());
            assertEquals("\0\0", mandatoryStringDecoder.getValue());
        });
    }

    @Test
    void testZeroByteStringMandatory2() throws IOException {
        withByteBuf("00 00 00 00 80", buffers -> {
            decode(mandatoryStringDecoder, buffers);
            assertTrue(mandatoryStringDecoder.isReady());
            assertEquals("\0\0\0\0", mandatoryStringDecoder.getValue());
        });
    }

    @Test
    void testOverlong1() throws IOException {
        withByteBuf("00 81", buffers -> {
            decode(mandatoryStringDecoder, buffers);
            assertTrue(mandatoryStringDecoder.isReady());
            mandatoryStringDecoder.setCheckOverlong();

            assertThrows(IOException.class, () -> mandatoryStringDecoder.getValue());
            mandatoryStringDecoder.clearCheckOverlong();
            mandatoryStringDecoder.getValue();
        });
    }

    @Test
    void testOverlong2() throws IOException {
        withByteBuf("00 00 00 81", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            nullableStringDecoder.setCheckOverlong();

            assertThrows(IOException.class, () -> nullableStringDecoder.getValue());
            nullableStringDecoder.clearCheckOverlong();
            nullableStringDecoder.getValue();
        });
    }

    @Test
    void testNullableReuse() throws IOException {
        withByteBuf("41 42 c3 42 42 c3 41 44 c3", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertEquals("ABC", nullableStringDecoder.getValue());
            decode(nullableStringDecoder, buffers);
            assertEquals("BBC", nullableStringDecoder.getValue());
            decode(nullableStringDecoder, buffers);
            assertEquals("ADC", nullableStringDecoder.getValue());
        });
    }

    @Test
    void testSimpleStringGetValueTwice() throws IOException {
        withByteBuf("41 42 c3", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertEquals("ABC", nullableStringDecoder.getValue());
            assertEquals("ABC", nullableStringDecoder.getValue());
        });
    }

    @Test
    void testMandatoryEmptyStringGetValueTwice() throws IOException {
        withByteBuf("80", buffers -> {
            decode(mandatoryStringDecoder, buffers);
            assertTrue(mandatoryStringDecoder.isReady());
            assertEquals("", mandatoryStringDecoder.getValue());
            assertEquals("", mandatoryStringDecoder.getValue());
        });
    }

    @Test
    void testZeroByteStringNullableTwoValuesInRow() throws IOException {
        withByteBuf("00 00 80 00 00 00 00 80", buffers -> {
            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertEquals("\0", nullableStringDecoder.getValue());

            decode(nullableStringDecoder, buffers);
            assertTrue(nullableStringDecoder.isReady());
            assertEquals("\0\0\0", nullableStringDecoder.getValue());
        });
    }

    @Test
    void testZeroByteStringMandatoryTwoValuesInRow() throws IOException {
        withByteBuf("00 00 80 00 00 00 00 80", buffers -> {
            decode(mandatoryStringDecoder, buffers);
            assertTrue(mandatoryStringDecoder.isReady());
            assertEquals("\0\0", mandatoryStringDecoder.getValue());

            decode(mandatoryStringDecoder, buffers);
            assertTrue(mandatoryStringDecoder.isReady());
            assertEquals("\0\0\0\0", mandatoryStringDecoder.getValue());
        });
    }
}
