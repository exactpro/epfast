package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.decoder.FillBuffer.*;

class TestUInt32 {

    private DecodeNullableUInt32 nullableUInt32Decoder = new DecodeNullableUInt32();

    private DecodeMandatoryUInt32 mandatoryUInt32Decoder = new DecodeMandatoryUInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() throws OverflowException {
        nullableUInt32Decoder.decode(fromHex("80"));
        assertTrue(nullableUInt32Decoder.isReady());
        assertNull(nullableUInt32Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() throws OverflowException {
        nullableUInt32Decoder.decode(fromHex("81"));
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(0, nullableUInt32Decoder.getValue());
    }

    @Test
    void mandatoryZero() throws OverflowException {
        mandatoryUInt32Decoder.decode(fromHex("80"));
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(0, mandatoryUInt32Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() throws OverflowException {
        nullableUInt32Decoder.decode(fromHex("10 00 00 00 80"));
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(4294967295L, nullableUInt32Decoder.getValue());
    }

    @Test
    void testMaxMandatory() throws OverflowException {
        mandatoryUInt32Decoder.decode(fromHex("0f 7f 7f 7f ff"));
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(4294967295L, mandatoryUInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable1() {
        nullableUInt32Decoder.decode(fromHex("10 00 00 00 81"));
        assertTrue(nullableUInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableUInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable2() {
        nullableUInt32Decoder.decode(fromHex("10 00 00 00 00 00 80"));
        assertTrue(nullableUInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableUInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory1() {
        mandatoryUInt32Decoder.decode(fromHex("10 00 00 00 00 80"));
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryUInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory2() {
        mandatoryUInt32Decoder.decode(fromHex("0f 7f 7f 7f 7f 00 ff"));
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryUInt32Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber() throws OverflowException {
        nullableUInt32Decoder.decode(fromHex("39 45 a4"));
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(942755, nullableUInt32Decoder.getValue());
    }

    @Test
    void optionalSimpleNumber2() throws OverflowException {
        nullableUInt32Decoder.decode(fromHex("0f 7f 7f 7f ff"));
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(4294967294L, nullableUInt32Decoder.getValue());
    }

    @Test
    void mandatorySimpleNumber() throws OverflowException {
        mandatoryUInt32Decoder.decode(fromHex("39 45 a3"));
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(942755, mandatoryUInt32Decoder.getValue());
    }

    @Test
    void optionalSimpleNumberGetValueTwice() throws OverflowException {
        nullableUInt32Decoder.decode(fromHex("39 45 a4"));
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(942755, nullableUInt32Decoder.getValue());
        assertEquals(942755, nullableUInt32Decoder.getValue());
    }

    @Test
    void mandatorySimpleNumberGetValueTwice() throws OverflowException {
        mandatoryUInt32Decoder.decode(fromHex("39 45 a3"));
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(942755, mandatoryUInt32Decoder.getValue());
        assertEquals(942755, mandatoryUInt32Decoder.getValue());
    }

    @Test
    void optionalSimpleNumbersTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("39 45 a4 0f 7f 7f 7f ff");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(942755, nullableUInt32Decoder.getValue());

        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertEquals(4294967294L, nullableUInt32Decoder.getValue());
    }

    @Test
    void mandatorySimpleNumbersTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("39 45 a3 39 45 a3");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(942755, mandatoryUInt32Decoder.getValue());

        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertEquals(942755, mandatoryUInt32Decoder.getValue());
    }
}
