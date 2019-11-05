package com.exactpro.epfast.decoder.decimal;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestDecodeDecimal {

    private DecodeNullableDecimal nullableDecimalDecoder = new DecodeNullableDecimal();

    private DecodeMandatoryDecimal mandatoryDecimalDecoder = new DecodeMandatoryDecimal();

    @Test
    void testNull() throws IOException {
        withByteBuf("80", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertNull(nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testNullablePositive1() throws IOException {
        withByteBuf("83 39 45 a3", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testNullablePositive() throws IOException {
        withByteBuf("82 04 3f 34 de", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testMandatoryPositive() throws IOException {
        withByteBuf("82 39 45 a3", buffers -> {
            decode(mandatoryDecimalDecoder, buffers);
            assertTrue(mandatoryDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        });
    }

    @Test
    void testMandatoryPositive2() throws IOException {
        withByteBuf("81 04 3f 34 de", buffers -> {
            decode(mandatoryDecimalDecoder, buffers);
            assertTrue(mandatoryDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        });
    }

    @Test
    void testMandatoryPositive3() throws IOException {
        withByteBuf("fe 39 45 a3", buffers -> {
            decode(mandatoryDecimalDecoder, buffers);
            assertTrue(mandatoryDecimalDecoder.isReady());
            assertEquals(new BigDecimal("9427.55"), mandatoryDecimalDecoder.getValue());
        });
    }

    @Test
    void testNullablePositive3() throws IOException {
        withByteBuf("fe 39 45 a3", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertEquals(new BigDecimal("9427.55"), nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testNullableNegative() throws IOException {
        withByteBuf("fe 46 3a dd", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertEquals(new BigDecimal("-9427.55"), nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testNullableNegativeSignExtension() throws IOException {
        withByteBuf("fd 7f 3f ff", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertEquals(new BigDecimal("-8.193"), nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testExponentIOException() throws IOException {
        withByteBuf("39 45 a4 7f 3f ff", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertThrows(IOException.class, () -> nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testNullablePositiveGetValueTwice() throws IOException {
        withByteBuf("82 04 3f 34 de", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testMandatoryPositiveGetValueTwice() throws IOException {
        withByteBuf("82 39 45 a3", buffers -> {
            decode(mandatoryDecimalDecoder, buffers);
            assertTrue(mandatoryDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        });
    }

    @Test
    void testNullablePositiveTwoValuesInRow() throws IOException {
        withByteBuf("83 39 45 a3 82 04 3f 34 de", buffers -> {
            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());

            decode(nullableDecimalDecoder, buffers);
            assertTrue(nullableDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        });
    }

    @Test
    void testMandatoryPositiveTwoValuesInRow() throws IOException {
        withByteBuf("82 39 45 a3 81 04 3f 34 de", buffers -> {
            decode(mandatoryDecimalDecoder, buffers);
            assertTrue(mandatoryDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());

            decode(mandatoryDecimalDecoder, buffers);
            assertTrue(mandatoryDecimalDecoder.isReady());
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        });
    }
}
