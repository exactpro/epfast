package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.decoder.FillBuffer.*;

class TestInt32 {

    private DecodeNullableInt32 nullableInt32Decoder = new DecodeNullableInt32();

    private DecodeMandatoryInt32 mandatoryInt32Decoder = new DecodeMandatoryInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("80"));
        assertTrue(nullableInt32Decoder.isReady());
        assertNull(nullableInt32Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("81"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(0, nullableInt32Decoder.getValue());
    }

    @Test
    void mandatoryZero() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("80"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(0, mandatoryInt32Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test min/max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("08 00 00 00 80"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(Integer.MAX_VALUE, nullableInt32Decoder.getValue());
    }

    @Test
    void testMaxMandatory() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("07 7f 7f 7f ff"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(Integer.MAX_VALUE, mandatoryInt32Decoder.getValue());
    }

    @Test
    void testMinNullable() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("78 00 00 00 80"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(Integer.MIN_VALUE, nullableInt32Decoder.getValue());
    }

    @Test
    void testMinMandatory() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("78 00 00 00 80"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(Integer.MIN_VALUE, mandatoryInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable1() {
        nullableInt32Decoder.decode(fromHex("08 00 00 00 81"));
        assertTrue(nullableInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable2() {
        nullableInt32Decoder.decode(fromHex("08 00 00 00 00 00 00 80"));
        assertTrue(nullableInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory1() {
        mandatoryInt32Decoder.decode(fromHex("08 00 00 00 80"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory2() {
        mandatoryInt32Decoder.decode(fromHex("07 7f 00 7f 7f 7f ff"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt32Decoder.getValue());
    }

    @Test
    void testMinOverflowNullable1() {
        nullableInt32Decoder.decode(fromHex("77 7f 7f 7f ff"));
        assertTrue(nullableInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt32Decoder.getValue());
    }

    @Test
    void testMinOverflowNullable2() {
        nullableInt32Decoder.decode(fromHex("78 00 00 00 00 80"));
        assertTrue(nullableInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt32Decoder.getValue());
    }

    @Test
    void testMinOverflowMandatory1() {
        mandatoryInt32Decoder.decode(fromHex("77 7f 7f 7f ff"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt32Decoder.getValue());
    }

    @Test
    void testMinOverflowMandatory2() {
        mandatoryInt32Decoder.decode(fromHex("78 00 00 00 00 80"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt32Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("39 45 a4"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(942755, nullableInt32Decoder.getValue());
    }

    @Test
    void optionalPositiveSplit() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("39 45"));
        assertFalse(nullableInt32Decoder.isReady());

        nullableInt32Decoder.continueDecode(fromHex("a4"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(942755, nullableInt32Decoder.getValue());
    }

    @Test
    void mandatoryPositive() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("39 45 a3"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(942755, mandatoryInt32Decoder.getValue());
    }

    @Test
    void mandatoryPositiveSplit() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("39 45"));
        assertFalse(mandatoryInt32Decoder.isReady());

        mandatoryInt32Decoder.continueDecode(fromHex("a3"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(942755, mandatoryInt32Decoder.getValue());
    }

    @Test
    void optionalNegative() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("46 3a dd"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(-942755, nullableInt32Decoder.getValue());
    }

    @Test
    void optionalNegativeSplit() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("46 3a"));
        assertFalse(nullableInt32Decoder.isReady());

        nullableInt32Decoder.continueDecode(fromHex("dd"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(-942755, nullableInt32Decoder.getValue());
    }

    @Test
    void mandatoryNegative() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("7c 1b 1b 9d"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(-7942755, mandatoryInt32Decoder.getValue());
    }

    @Test
    void mandatoryNegativeSplit() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("7c 1b"));
        assertFalse(mandatoryInt32Decoder.isReady());

        mandatoryInt32Decoder.continueDecode(fromHex("1b 9d"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(-7942755, mandatoryInt32Decoder.getValue());
    }

    @Test
    void optionalMinusOne() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("ff"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(-1, nullableInt32Decoder.getValue());
    }

    @Test
    void mandatoryMinusOne() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("ff"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(-1, mandatoryInt32Decoder.getValue());
    }

    @Test
    void optionalSignExtensionPositive() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("00 00 40 82"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(8193, nullableInt32Decoder.getValue());
    }

    @Test
    void mandatorySignExtensionPositive() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("00 00 40 81"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(8193, mandatoryInt32Decoder.getValue());
    }

    @Test
    void optionalSignExtensionNegative() throws OverflowException {
        nullableInt32Decoder.decode(fromHex("7f 3f ff"));
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(-8193, nullableInt32Decoder.getValue());
    }

    @Test
    void mandatorySignExtensionNegative() throws OverflowException {
        mandatoryInt32Decoder.decode(fromHex("7f 3f ff"));
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(-8193, mandatoryInt32Decoder.getValue());
    }

    @Test
    void mandatoryNegativeTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("7f 3f ff 7f 3f ff");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(-8193, mandatoryInt32Decoder.getValue());

        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(-8193, mandatoryInt32Decoder.getValue());
    }

    @Test
    void mandatoryPositiveTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("00 00 40 81 00 00 40 81");
        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(8193, mandatoryInt32Decoder.getValue());

        mandatoryInt32Decoder.decode(buf);
        assertTrue(mandatoryInt32Decoder.isReady());
        assertEquals(8193, mandatoryInt32Decoder.getValue());
    }

    @Test
    void optionalNegativeTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("7f 3f ff 7f 3f ff");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(-8193, nullableInt32Decoder.getValue());

        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(-8193, nullableInt32Decoder.getValue());
    }

    @Test
    void optionalPositiveTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("00 00 40 82 00 00 40 82");
        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(8193, nullableInt32Decoder.getValue());

        nullableInt32Decoder.decode(buf);
        assertTrue(nullableInt32Decoder.isReady());
        assertEquals(8193, nullableInt32Decoder.getValue());
    }
}
