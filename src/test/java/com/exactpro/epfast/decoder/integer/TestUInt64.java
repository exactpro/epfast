package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestUInt64 {

    private DecodeNullableUInt64 nullableUInt64Decoder = new DecodeNullableUInt64();

    private DecodeMandatoryUInt64 mandatoryUInt64Decoder = new DecodeMandatoryUInt64();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x80");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertNull(val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x81");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("0"), val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x80");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("0"), val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x02 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551615"), val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x01 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0xff");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551615"), val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x02 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x81");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertTrue(nullableUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x02 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertTrue(nullableUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x02 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x80");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertTrue(mandatoryUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x01 0x7f 0x7f 0x7f 0x7f 0x00 0x7f 0x7f 0x7f 0x7f 0xff");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertTrue(mandatoryUInt64Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45 0xa4");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
    }

    @Test
    void optionalSimpleNumber2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x01 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0x7f 0xff");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551614"), val);
    }

    @Test
    void mandatorySimpleNumber1() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x39 0x45 0xa3");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
    }

    @Test
    void mandatorySimpleNumber2() {
        ByteBuf buf = Unpooled.buffer();
        FillBuffer.fillBuffer(buf, "0x01 0x10 0x78 0x20 0x76 0x62 0x2a 0x62 0x51 0xcf");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("10443992354206034127"), val);
    }
}
