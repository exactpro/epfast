package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestUInt32 {

    private DecodeNullableUInt32 nullableUInt32Decoder = new DecodeNullableUInt32();

    private DecodeMandatoryUInt32 mandatoryUInt32Decoder = new DecodeMandatoryUInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() {
        ByteBuf buf = FillBuffer.fromHex("80");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverflow());
        Long val = nullableUInt32Decoder.getValue();
        assertNull(val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() {
        ByteBuf buf = FillBuffer.fromHex("81");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverflow());
        long val = nullableUInt32Decoder.getValue();
        assertEquals(0, val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = FillBuffer.fromHex("80");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertFalse(mandatoryUInt32Decoder.isOverflow());
        long val = mandatoryUInt32Decoder.getValue();
        assertEquals(0, val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = FillBuffer.fromHex("10 00 00 00 80");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverflow());
        long val = nullableUInt32Decoder.getValue();
        assertEquals(4294967295L, val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = FillBuffer.fromHex("0f 7f 7f 7f ff");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertFalse(mandatoryUInt32Decoder.isOverflow());
        long val = mandatoryUInt32Decoder.getValue();
        assertEquals(4294967295L, val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("10 00 00 00 81");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertTrue(nullableUInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("10 00 00 00 00 00 80");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertTrue(nullableUInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("10 00 00 00 00 80");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertTrue(mandatoryUInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("0f 7f 7f 7f 7f 00 ff");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertTrue(mandatoryUInt32Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverflow());
        long val = nullableUInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalSimpleNumber2() {
        ByteBuf buf = FillBuffer.fromHex("0f 7f 7f 7f ff");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverflow());
        long val = nullableUInt32Decoder.getValue();
        assertEquals(4294967294L, val);
    }

    @Test
    void mandatorySimpleNumber() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertFalse(mandatoryUInt32Decoder.isOverflow());
        long val = mandatoryUInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalSimpleNumberGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverflow());
        long val = nullableUInt32Decoder.getValue();
        assertEquals(942755, val);
        val = nullableUInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatorySimpleNumberGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertFalse(mandatoryUInt32Decoder.isOverflow());
        long val = mandatoryUInt32Decoder.getValue();
        assertEquals(942755, val);
        val = mandatoryUInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalSimpleNumbersTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4 0f 7f 7f 7f ff");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverflow());
        long val = nullableUInt32Decoder.getValue();
        assertEquals(942755, val);

        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverflow());
        val = nullableUInt32Decoder.getValue();
        assertEquals(4294967294L, val);
    }

    @Test
    void mandatorySimpleNumbersTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3 39 45 a3");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertFalse(mandatoryUInt32Decoder.isOverflow());
        long val = mandatoryUInt32Decoder.getValue();
        assertEquals(942755, val);

        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertFalse(mandatoryUInt32Decoder.isOverflow());
        val = mandatoryUInt32Decoder.getValue();
        assertEquals(942755, val);
    }
}
