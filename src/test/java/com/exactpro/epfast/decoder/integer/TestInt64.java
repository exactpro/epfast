package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
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
        ByteBuf buf = FillBuffer.fromHex("80");
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
        ByteBuf buf = FillBuffer.fromHex("81");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        Long val = nullableInt64Decoder.getValue();
        assertEquals(0, val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = FillBuffer.fromHex("80");
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
        ByteBuf buf = FillBuffer.fromHex("01 00 00 00 00 00 00 00 00 80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(Long.MAX_VALUE, val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = FillBuffer.fromHex("00 7f 7f 7f 7f 7f 7f 7f 7f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(Long.MAX_VALUE, val);
    }

    @Test
    void testMinNullable() {
        ByteBuf buf = FillBuffer.fromHex("7f 00 00 00 00 00 00 00 00 80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(Long.MIN_VALUE, val);
    }

    @Test
    void testMinMandatory() {
        ByteBuf buf = FillBuffer.fromHex("7f 00 00 00 00 00 00 00 00 80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(Long.MIN_VALUE, val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("01 00 00 00 00 00 00 00 00 81");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("01 00 00 00 00 00 00 00 00 00 80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("01 00 00 00 00 00 00 00 00 80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("00 7f 00 7f 7f 7f 7f 7f 7f 7f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverflow());
    }

    @Test
    void testMinOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("77 7f 7f 7f 7f 7f 7f 7f 7f ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverflow());
    }

    @Test
    void testMinOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("7f 00 00 00 00 00 00 00 00 00 80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverflow());
    }

    @Test
    void testMinOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("77 7f 7f 7f 7f 7f 7f 7f 7f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverflow());
    }

    @Test
    void testMinOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("7f 00 00 00 00 00 00 00 00 00 80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalPositiveSplit() {
        ByteBuf buf = FillBuffer.fromHex("39 45");
        nullableInt64Decoder.decode(buf);
        assertFalse(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());

        buf = FillBuffer.fromHex("a4");
        nullableInt64Decoder.continueDecode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatoryPositive() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatoryPositiveSplit() {
        ByteBuf buf = FillBuffer.fromHex("39 45");
        mandatoryInt64Decoder.decode(buf);
        assertFalse(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());

        buf = FillBuffer.fromHex("a3");
        mandatoryInt64Decoder.continueDecode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalNegative() {
        ByteBuf buf = FillBuffer.fromHex("46 3a dd");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void optionalNegativeSplit() {
        ByteBuf buf = FillBuffer.fromHex("46 3a");
        nullableInt64Decoder.decode(buf);
        assertFalse(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());

        buf = FillBuffer.fromHex("dd");
        nullableInt64Decoder.continueDecode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void mandatoryNegative() {
        ByteBuf buf = FillBuffer.fromHex("7c 1b 1b 9d");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void mandatoryNegativeSplit() {
        ByteBuf buf = FillBuffer.fromHex("7c 1b");
        mandatoryInt64Decoder.decode(buf);
        assertFalse(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());

        buf = FillBuffer.fromHex("1b 9d");
        mandatoryInt64Decoder.continueDecode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void optionalMinusOne() {
        ByteBuf buf = FillBuffer.fromHex("ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void mandatoryMinusOne() {
        ByteBuf buf = FillBuffer.fromHex("ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void optionalSignExtensionPositive() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 82");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void mandatorySignExtensionPositive() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 81");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void optionalSignExtensionNegative() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatorySignExtensionNegative() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatoryNegativeTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff 7f 3f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(-8193, val);

        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        val = mandatoryInt64Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatoryPositiveTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 81 00 00 40 81");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        long val = mandatoryInt64Decoder.getValue();
        assertEquals(8193, val);

        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverflow());
        val = mandatoryInt64Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void optionalNegativeTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff 7f 3f ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(-8193, val);

        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        val = nullableInt64Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void optionalPositiveTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 82 00 00 40 82");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        long val = nullableInt64Decoder.getValue();
        assertEquals(8193, val);

        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverflow());
        val = nullableInt64Decoder.getValue();
        assertEquals(8193, val);
    }
}
