package com.exactpro.epfast.decoder.decimal;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.decoder.FillBuffer.*;

class TestDecodeDecimal {

    private DecodeNullableDecimal nullableDecimalDecoder = new DecodeNullableDecimal();

    private DecodeMandatoryDecimal mandatoryDecimalDecoder = new DecodeMandatoryDecimal();

    @Test
    void testNull() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("80"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertNull(nullableDecimalDecoder.getValue());
    }

    @Test
    void testNullablePositive1() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("83 39 45 a3"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testNullablePositiveSplit() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("83 39"));
        assertFalse(nullableDecimalDecoder.isReady());

        nullableDecimalDecoder.continueDecode(fromHex("45 a3"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testNullablePositive() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("82 04 3f 34 de"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testNullablePositiveSplit2() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("82 04 3f 34"));
        assertFalse(nullableDecimalDecoder.isReady());

        nullableDecimalDecoder.continueDecode(fromHex("de"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testMandatoryPositive() throws OverflowException {
        mandatoryDecimalDecoder.decode(fromHex("82 39 45 a3"));
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @Test
    void testMandatoryPositiveSplit() throws OverflowException {
        mandatoryDecimalDecoder.decode(fromHex("82 39"));
        assertFalse(mandatoryDecimalDecoder.isReady());

        mandatoryDecimalDecoder.continueDecode(fromHex("45 a3"));
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @Test
    void testMandatoryPositive2() throws OverflowException {
        mandatoryDecimalDecoder.decode(fromHex("81 04 3f 34 de"));
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @Test
    void testMandatoryPositiveSplit2() throws OverflowException {
        mandatoryDecimalDecoder.decode(fromHex("81 04 3f 34"));
        assertFalse(mandatoryDecimalDecoder.isReady());

        mandatoryDecimalDecoder.continueDecode(fromHex("de"));
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @Test
    void testMandatoryPositive3() throws OverflowException {
        mandatoryDecimalDecoder.decode(fromHex("fe 39 45 a3"));
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertEquals(new BigDecimal("9427.55"), mandatoryDecimalDecoder.getValue());
    }

    @Test
    void testNullablePositive3() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("fe 39 45 a3"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("9427.55"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testNullableNegative() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("fe 46 3a dd"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("-9427.55"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testNullableNegativeSignExtension() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("fd 7f 3f ff"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("-8.193"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testExponentOverflowException() {
        nullableDecimalDecoder.decode(fromHex("39 45 a4 7f 3f ff"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertThrows(OverflowException.class, () -> nullableDecimalDecoder.getValue());
    }

    @Test
    void testNullablePositiveGetValueTwice() throws OverflowException {
        nullableDecimalDecoder.decode(fromHex("82 04 3f 34 de"));
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testMandatoryPositiveGetValueTwice() throws OverflowException {
        mandatoryDecimalDecoder.decode(fromHex("82 39 45 a3"));
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }

    @Test
    void testNullablePositiveTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("83 39 45 a3 82 04 3f 34 de");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());

        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
    }

    @Test
    void testMandatoryPositiveTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("82 39 45 a3 81 04 3f 34 de");
        mandatoryDecimalDecoder.decode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());

        mandatoryDecimalDecoder.decode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
    }
}
