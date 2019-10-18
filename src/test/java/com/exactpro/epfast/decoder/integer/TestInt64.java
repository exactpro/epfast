package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.decoder.FillBuffer.*;

class TestInt64 {

    private DecodeNullableInt64 nullableInt64Decoder = new DecodeNullableInt64();

    private DecodeMandatoryInt64 mandatoryInt64Decoder = new DecodeMandatoryInt64();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("80"));
        assertTrue(nullableInt64Decoder.isReady());
        assertNull(nullableInt64Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("81"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(0, nullableInt64Decoder.getValue());
    }

    @Test
    void mandatoryZero() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("80"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(0, mandatoryInt64Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test min/max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("01 00 00 00 00 00 00 00 00 80"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(Long.MAX_VALUE, nullableInt64Decoder.getValue());
    }

    @Test
    void testMaxMandatory() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("00 7f 7f 7f 7f 7f 7f 7f 7f ff"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(Long.MAX_VALUE, mandatoryInt64Decoder.getValue());
    }

    @Test
    void testMinNullable() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("7f 00 00 00 00 00 00 00 00 80"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(Long.MIN_VALUE, nullableInt64Decoder.getValue());
    }

    @Test
    void testMinMandatory() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("7f 00 00 00 00 00 00 00 00 80"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(Long.MIN_VALUE, mandatoryInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable1() {
        nullableInt64Decoder.decode(fromHex("01 00 00 00 00 00 00 00 00 81"));
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable2() {
        nullableInt64Decoder.decode(fromHex("01 00 00 00 00 00 00 00 00 00 80"));
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory1() {
        mandatoryInt64Decoder.decode(fromHex("01 00 00 00 00 00 00 00 00 80"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory2() {
        mandatoryInt64Decoder.decode(fromHex("00 7f 00 7f 7f 7f 7f 7f 7f 7f ff"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @Test
    void testMinOverflowNullable1() {
        nullableInt64Decoder.decode(fromHex("77 7f 7f 7f 7f 7f 7f 7f 7f ff"));
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt64Decoder.getValue());
    }

    @Test
    void testMinOverflowNullable2() {
        nullableInt64Decoder.decode(fromHex("7f 00 00 00 00 00 00 00 00 00 80"));
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt64Decoder.getValue());
    }

    @Test
    void testMinOverflowMandatory1() {
        mandatoryInt64Decoder.decode(fromHex("77 7f 7f 7f 7f 7f 7f 7f 7f ff"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @Test
    void testMinOverflowMandatory2() {
        mandatoryInt64Decoder.decode(fromHex("7f 00 00 00 00 00 00 00 00 00 80"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt64Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("39 45 a4"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(942755, nullableInt64Decoder.getValue());
    }

    @Test
    void optionalPositiveSplit() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("39 45"));
        assertFalse(nullableInt64Decoder.isReady());

        nullableInt64Decoder.continueDecode(fromHex("a4"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(942755, nullableInt64Decoder.getValue());
    }

    @Test
    void mandatoryPositive() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("39 45 a3"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(942755, mandatoryInt64Decoder.getValue());
    }

    @Test
    void mandatoryPositiveSplit() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("39 45"));
        assertFalse(mandatoryInt64Decoder.isReady());

        mandatoryInt64Decoder.continueDecode(fromHex("a3"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(942755, mandatoryInt64Decoder.getValue());
    }

    @Test
    void optionalNegative() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("46 3a dd"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-942755, nullableInt64Decoder.getValue());
    }

    @Test
    void optionalNegativeSplit() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("46 3a"));
        assertFalse(nullableInt64Decoder.isReady());

        nullableInt64Decoder.continueDecode(fromHex("dd"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-942755, nullableInt64Decoder.getValue());
    }

    @Test
    void mandatoryNegative() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("7c 1b 1b 9d"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-7942755, mandatoryInt64Decoder.getValue());
    }

    @Test
    void mandatoryNegativeSplit() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("7c 1b"));
        assertFalse(mandatoryInt64Decoder.isReady());

        mandatoryInt64Decoder.continueDecode(fromHex("1b 9d"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-7942755, mandatoryInt64Decoder.getValue());
    }

    @Test
    void optionalMinusOne() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("ff"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-1, nullableInt64Decoder.getValue());
    }

    @Test
    void mandatoryMinusOne() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("ff"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-1, mandatoryInt64Decoder.getValue());
    }

    @Test
    void optionalSignExtensionPositive() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("00 00 40 82"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(8193, nullableInt64Decoder.getValue());
    }

    @Test
    void mandatorySignExtensionPositive() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("00 00 40 81"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(8193, mandatoryInt64Decoder.getValue());
    }

    @Test
    void optionalSignExtensionNegative() throws OverflowException {
        nullableInt64Decoder.decode(fromHex("7f 3f ff"));
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-8193, nullableInt64Decoder.getValue());
    }

    @Test
    void mandatorySignExtensionNegative() throws OverflowException {
        mandatoryInt64Decoder.decode(fromHex("7f 3f ff"));
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-8193, mandatoryInt64Decoder.getValue());
    }

    @Test
    void mandatoryNegativeTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("7f 3f ff 7f 3f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-8193, mandatoryInt64Decoder.getValue());

        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(-8193, mandatoryInt64Decoder.getValue());
    }

    @Test
    void mandatoryPositiveTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("00 00 40 81 00 00 40 81");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(8193, mandatoryInt64Decoder.getValue());

        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertEquals(8193, mandatoryInt64Decoder.getValue());
    }

    @Test
    void optionalNegativeTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("7f 3f ff 7f 3f ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-8193, nullableInt64Decoder.getValue());

        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(-8193, nullableInt64Decoder.getValue());
    }

    @Test
    void optionalPositiveTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("00 00 40 82 00 00 40 82");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(8193, nullableInt64Decoder.getValue());

        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertEquals(8193, nullableInt64Decoder.getValue());
    }
}
