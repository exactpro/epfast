package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestInt32 {

    private DecodeNullableInt32 nullableInt32Decoder = new DecodeNullableInt32();

    private DecodeMandatoryInt32 mandatoryInt32Decoder = new DecodeMandatoryInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        Integer val = nullableInt32Decoder.getValue();
        assertNull(val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x81");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        Integer val = nullableInt32Decoder.getValue();
        assertEquals(0, val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x80");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(0, val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test min/max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x08 0x00 0x00 0x00 0x80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(Integer.MAX_VALUE, val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x07 0x7f 0x7f 0x7f 0xff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(Integer.MAX_VALUE, val);
    }

    @Test
    void testMinNullable() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x78 0x00 0x00 0x00 0x80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(Integer.MIN_VALUE, val);
    }

    @Test
    void testMinMandatory() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x78 0x00 0x00 0x00 0x80");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(Integer.MIN_VALUE, val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x08 0x00 0x00 0x00 0x81");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x08 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x08 0x00 0x00 0x00 0x80");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x07 0x7f 0x00 0x7f 0x7f 0x7f 0xff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void testMinOverflowNullable1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x77 0x7f 0x7f 0x7f 0xff");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void testMinOverflowNullable2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x78 0x00 0x00 0x00 0x00 0x80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void testMinOverflowMandatory1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x77 0x7f 0x7f 0x7f 0xff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void testMinOverflowMandatory2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x78 0x00 0x00 0x00 0x00 0x80");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45 0xa4");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalPositiveSplit() {

        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45");
        nullableInt32Decoder.decode(buf);
        assertFalse(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());

        buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xa4");
        nullableInt32Decoder.continueDecode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatoryPositive() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45 0xa3");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatoryPositiveSplit() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45");
        mandatoryInt32Decoder.decode(buf);
        assertFalse(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());

        buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xa3");
        mandatoryInt32Decoder.continueDecode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalNegative() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x46 0x3a 0xdd");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void optionalNegativeSplit() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x46 0x3a");
        nullableInt32Decoder.decode(buf);
        assertFalse(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());

        buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xdd");
        nullableInt32Decoder.continueDecode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void mandatoryNegative() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7c 0x1b 0x1b 0x9d");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void mandatoryNegativeSplit() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7c 0x1b");
        mandatoryInt32Decoder.decode(buf);
        assertFalse(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());

        buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x1b 0x9d");
        mandatoryInt32Decoder.continueDecode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void optionalMinusOne() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xff");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void mandatoryMinusOne() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0xff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void optionalSignExtensionPositive() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x00 0x00 0x40 0x82");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void mandatorySignExtensionPositive() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x00 0x00 0x40 0x81");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void optionalSignExtensionNegative() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7f 0x3f 0xff");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatorySignExtensionNegative() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x7f 0x3f 0xff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-8193, val);
    }

}


//    ByteBuf buf = Unpooled.buffer();
//    buf.writeByte(0x08);
//    buf.writeByte(0x00);
//    buf.writeByte(0x00);
//    buf.writeByte(0x00);
//    buf.writeByte(0x80);
//    nullableInt32Decoder.decode(buf);
//    assertTrue(nullableInt32Decoder.isReady());
//    assertFalse(nullableInt32Decoder.isOverflow());
//    assertEquals(Integer.MAX_VALUE, nullableInt32Decoder.getValue());
//    assertFalse(buf.isReadable());
//
//    ByteBuf buf = Unpooled.buffer();
//    buf.writeByte(0x08);
//    buf.writeByte(0x00);
//    buf.writeByte(0x00);
//    nullableInt32Decoder.decode(buf);
//    assertFalse(nullableInt32Decoder.isReady());
//    assertFalse(buf.isReadable());
//
//    buf = Unpooled.buffer();
//    buf.writeByte(0x00);
//    buf.writeByte(0x80);
//    nullableInt32Decoder.continueDecode(buf);
//    assertTrue(nullableInt32Decoder.isReady());
//    assertFalse(nullableInt32Decoder.isOverflow());
//    assertEquals(Integer.MAX_VALUE, nullableInt32Decoder.getValue());
//    assertFalse(buf.isReadable());
