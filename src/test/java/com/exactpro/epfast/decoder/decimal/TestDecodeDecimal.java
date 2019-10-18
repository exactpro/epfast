package com.exactpro.epfast.decoder.decimal;

import com.exactpro.epfast.decoder.FillBuffer;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDecodeDecimal {

    private DecodeNullableDecimal nullableDecimalDecoder = new DecodeNullableDecimal();

    private DecodeMandatoryDecimal mandatoryDecimalDecoder = new DecodeMandatoryDecimal();

    @Test
    void testNull() {
        ByteBuf buf = FillBuffer.fromHex("80");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertNull(nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullablePositive1() {
        ByteBuf buf = FillBuffer.fromHex("83 39 45 a3");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullablePositiveSplit() {
        ByteBuf buf = FillBuffer.fromHex("83 39");
        nullableDecimalDecoder.decode(buf);
        assertFalse(nullableDecimalDecoder.isReady());

        buf = FillBuffer.fromHex("45 a3");
        nullableDecimalDecoder.continueDecode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullablePositive() {
        ByteBuf buf = FillBuffer.fromHex("82 04 3f 34 de");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullablePositiveSplit2() {
        ByteBuf buf = FillBuffer.fromHex("82 04 3f 34");
        nullableDecimalDecoder.decode(buf);
        assertFalse(nullableDecimalDecoder.isReady());

        buf = FillBuffer.fromHex("de");
        nullableDecimalDecoder.continueDecode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryPositive() {
        ByteBuf buf = FillBuffer.fromHex("82 39 45 a3");
        mandatoryDecimalDecoder.decode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryPositiveSplit() {
        ByteBuf buf = FillBuffer.fromHex("82 39");
        mandatoryDecimalDecoder.decode(buf);
        assertFalse(mandatoryDecimalDecoder.isReady());

        buf = FillBuffer.fromHex("45 a3");
        mandatoryDecimalDecoder.continueDecode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryPositive2() {
        ByteBuf buf = FillBuffer.fromHex("81 04 3f 34 de");
        mandatoryDecimalDecoder.decode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryPositiveSplit2() {
        ByteBuf buf = FillBuffer.fromHex("81 04 3f 34");
        mandatoryDecimalDecoder.decode(buf);
        assertFalse(mandatoryDecimalDecoder.isReady());

        buf = FillBuffer.fromHex("de");
        mandatoryDecimalDecoder.continueDecode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryPositive3() {
        ByteBuf buf = FillBuffer.fromHex("fe 39 45 a3");
        mandatoryDecimalDecoder.decode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("9427.55"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullablePositive3() {
        ByteBuf buf = FillBuffer.fromHex("fe 39 45 a3");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("9427.55"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullableNegative() {
        ByteBuf buf = FillBuffer.fromHex("fe 46 3a dd");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("-9427.55"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullableNegativeSignExtension() {
        ByteBuf buf = FillBuffer.fromHex("fd 7f 3f ff");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("-8.193"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testExponentOverflowException() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4 7f 3f ff");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        assertThrows(OverflowException.class, () -> nullableDecimalDecoder.getValue());
    }

    @Test
    void testNullablePositiveGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("82 04 3f 34 de");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryPositiveGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("82 39 45 a3");
        mandatoryDecimalDecoder.decode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullablePositiveTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("83 39 45 a3 82 04 3f 34 de");
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        nullableDecimalDecoder.decode(buf);
        assertTrue(nullableDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), nullableDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryPositiveTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("82 39 45 a3 81 04 3f 34 de");
        mandatoryDecimalDecoder.decode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        mandatoryDecimalDecoder.decode(buf);
        assertTrue(mandatoryDecimalDecoder.isReady());
        try {
            assertEquals(new BigDecimal("94275500"), mandatoryDecimalDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }
}
