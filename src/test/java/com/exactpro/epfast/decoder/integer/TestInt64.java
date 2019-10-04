package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestInt64 {

    private DecodeNullableInt64 nullableInt64Decoder = new DecodeNullableInt64();

    private DecodeMandatoryInt64 mandatoryInt64Decoder = new DecodeMandatoryInt64();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        Long val = nullableInt64Decoder.getValue();
        assertNull(val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x81");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        Long val = nullableInt64Decoder.getValue();
        assertEquals(0, val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(0, val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test min/max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x01 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(Long.MAX_VALUE, val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x00 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0xff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(Long.MAX_VALUE, val);
    }

    @Test
    void testMinNullable() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7f 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(Long.MIN_VALUE, val);
    }

    @Test
    void testMinMandatory() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7f 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(Long.MIN_VALUE, val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x01 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x81");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x01 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x01 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x00 0x7f 0x00 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0xff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverflow());
    }

    @Test
    void testMinOverflowNullable1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x77 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0xff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverflow());
    }

    @Test
    void testMinOverflowNullable2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7f 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverflow());
    }

    @Test
    void testMinOverflowMandatory1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x77 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0xff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverflow());
    }

    @Test
    void testMinOverflowMandatory2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7f 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45 0xa4");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalPositiveSplit() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45");
        nullableInt64Decoder.decode(buf);
        assertFalse(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());

        buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xa4");
        nullableInt64Decoder.continueDecode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatoryPositive() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45 0xa3");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatoryPositiveSplit() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45");
        mandatoryInt64Decoder.decode(buf);
        assertFalse(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());

        buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xa3");
        mandatoryInt64Decoder.continueDecode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalNegative() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x46 0x3a 0xdd");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void optionalNegativeSplit() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x46 0x3a");
        nullableInt64Decoder.decode(buf);
        assertFalse(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());

        buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xdd");
        nullableInt64Decoder.continueDecode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void mandatoryNegative() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7c 0x1b 0x1b 0x9d");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void mandatoryNegativeSplit() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7c 0x1b");
        mandatoryInt64Decoder.decode(buf);
        assertFalse(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());

        buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x1b 0x9d");
        mandatoryInt64Decoder.continueDecode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void optionalMinusOne() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void mandatoryMinusOne() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void optionalSignExtensionPositive() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x00 0x00 0x40 0x82");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void mandatorySignExtensionPositive() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x00 0x00 0x40 0x81");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void optionalSignExtensionNegative() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7f 0x3f 0xff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatorySignExtensionNegative() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7f 0x3f 0xff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-8193, val);
    }
}
