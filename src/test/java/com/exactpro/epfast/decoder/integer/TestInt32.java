package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestInt32 {

    private DecodeNullableInt32 nullableInt32Decoder = new DecodeNullableInt32();

    private DecodeMandatoryInt32 mandatoryInt32Decoder = new DecodeMandatoryInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() {
        ByteBuf buf = FillBuffer.fromHex("80");
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
        ByteBuf buf = FillBuffer.fromHex("81");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        Integer val = nullableInt32Decoder.getValue();
        assertEquals(0, val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = FillBuffer.fromHex("80");
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
        ByteBuf buf = FillBuffer.fromHex("08 00 00 00 80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(Integer.MAX_VALUE, val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = FillBuffer.fromHex("07 7f 7f 7f ff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(Integer.MAX_VALUE, val);
    }

    @Test
    void testMinNullable() {
        ByteBuf buf = FillBuffer.fromHex("78 00 00 00 80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(Integer.MIN_VALUE, val);
    }

    @Test
    void testMinMandatory() {
        ByteBuf buf = FillBuffer.fromHex("78 00 00 00 80");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(Integer.MIN_VALUE, val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("08 00 00 00 81");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("08 00 00 00 00 00 00 80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("08 00 00 00 80");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("07 7f 00 7f 7f 7f ff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void testMinOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("77 7f 7f 7f ff");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void testMinOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("78 00 00 00 00 80");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void testMinOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("77 7f 7f 7f ff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void testMinOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("78 00 00 00 00 80");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalPositiveSplit() {

        ByteBuf buf = FillBuffer.fromHex("39 45");
        nullableInt32Decoder.decode(buf);
        assertFalse(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());

        buf = FillBuffer.fromHex("a4");
        nullableInt32Decoder.continueDecode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatoryPositive() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatoryPositiveSplit() {
        ByteBuf buf = FillBuffer.fromHex("39 45");
        mandatoryInt32Decoder.decode(buf);
        assertFalse(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());

        buf = FillBuffer.fromHex("a3");
        mandatoryInt32Decoder.continueDecode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalNegative() {
        ByteBuf buf = FillBuffer.fromHex("46 3a dd");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void optionalNegativeSplit() {
        ByteBuf buf = FillBuffer.fromHex("46 3a");
        nullableInt32Decoder.decode(buf);
        assertFalse(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());

        buf = FillBuffer.fromHex("dd");
        nullableInt32Decoder.continueDecode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void mandatoryNegative() {
        ByteBuf buf = FillBuffer.fromHex("7c 1b 1b 9d");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void mandatoryNegativeSplit() {
        ByteBuf buf = FillBuffer.fromHex("7c 1b");
        mandatoryInt32Decoder.decode(buf);
        assertFalse(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());

        buf = FillBuffer.fromHex("1b 9d");
        mandatoryInt32Decoder.continueDecode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void optionalMinusOne() {
        ByteBuf buf = FillBuffer.fromHex("ff");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void mandatoryMinusOne() {
        ByteBuf buf = FillBuffer.fromHex("ff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void optionalSignExtensionPositive() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 82");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void mandatorySignExtensionPositive() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 81");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void optionalSignExtensionNegative() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatorySignExtensionNegative() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatoryNegativeTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff 7f 3f ff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-8193, val);

        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        val = mandatoryInt32Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatoryPositiveTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 81 00 00 40 81");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(8193, val);

        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertFalse(mandatoryInt32Decoder.isOverflow());
        val = mandatoryInt32Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void optionalNegativeTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff 7f 3f ff");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(-8193, val);

        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        val = nullableInt32Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void optionalPositiveTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 82 00 00 40 82");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        int val = nullableInt32Decoder.getValue();
        assertEquals(8193, val);

        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertFalse(nullableInt32Decoder.isOverflow());
        val = nullableInt32Decoder.getValue();
        assertEquals(8193, val);
    }
}
