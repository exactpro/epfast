package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
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
        ByteBuf buf = FillBuffer.fromHex("80");
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
        ByteBuf buf = FillBuffer.fromHex("81");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("0"), val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = FillBuffer.fromHex("80");
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
        ByteBuf buf = FillBuffer.fromHex("02 00 00 00 00 00 00 00 00 80");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551615"), val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = FillBuffer.fromHex("01 7f 7f 7f 7f 7f 7f 7f 7f ff");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551615"), val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("02 00 00 00 00 00 00 00 00 81");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertTrue(nullableUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("02 00 00 00 00 00 00 00 00 00 80");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertTrue(nullableUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("02 00 00 00 00 00 00 00 00 80");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertTrue(mandatoryUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("01 7f 7f 7f 7f 00 7f 7f 7f 7f ff");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertTrue(mandatoryUInt64Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber1() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
    }

    @Test
    void optionalSimpleNumber2() {
        ByteBuf buf = FillBuffer.fromHex("01 7f 7f 7f 7f 7f 7f 7f 7f ff");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551614"), val);
    }

    @Test
    void mandatorySimpleNumber1() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
    }

    @Test
    void mandatorySimpleNumber2() {
        ByteBuf buf = FillBuffer.fromHex("01 10 78 20 76 62 2a 62 51 cf");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("10443992354206034127"), val);
    }

    @Test
    void optionalSimpleNumber1GetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
        val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
    }

    @Test
    void mandatorySimpleNumber1GetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
        val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
    }

    @Test
    void optionalSimpleNumbersTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4 01 7f 7f 7f 7f 7f 7f 7f 7f ff");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);

        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverflow());
        val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551614"), val);
    }

    @Test
    void mandatorySimpleNumbersTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3 01 10 78 20 76 62 2a 62 51 cf");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);

        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverflow());
        val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("10443992354206034127"), val);
    }
}
