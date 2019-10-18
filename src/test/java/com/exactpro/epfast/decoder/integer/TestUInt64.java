package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.decoder.FillBuffer.*;

class TestUInt64 {

    private DecodeNullableUInt64 nullableUInt64Decoder = new DecodeNullableUInt64();

    private DecodeMandatoryUInt64 mandatoryUInt64Decoder = new DecodeMandatoryUInt64();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() throws OverflowException {
        nullableUInt64Decoder.decode(fromHex("80"));
        assertTrue(nullableUInt64Decoder.isReady());
        assertNull(nullableUInt64Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() throws OverflowException {
        nullableUInt64Decoder.decode(fromHex("81"));
        assertTrue(nullableUInt64Decoder.isReady());
        assertEquals(new BigInteger("0"), nullableUInt64Decoder.getValue());
    }

    @Test
    void mandatoryZero() throws OverflowException {
        mandatoryUInt64Decoder.decode(fromHex("80"));
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertEquals(new BigInteger("0"), mandatoryUInt64Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() throws OverflowException {
        nullableUInt64Decoder.decode(fromHex("02 00 00 00 00 00 00 00 00 80"));
        assertTrue(nullableUInt64Decoder.isReady());
        assertEquals(new BigInteger("18446744073709551615"), nullableUInt64Decoder.getValue());
    }

    @Test
    void testMaxMandatory() throws OverflowException {
        mandatoryUInt64Decoder.decode(fromHex("01 7f 7f 7f 7f 7f 7f 7f 7f ff"));
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertEquals(new BigInteger("18446744073709551615"), mandatoryUInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable1() {
        nullableUInt64Decoder.decode(fromHex("02 00 00 00 00 00 00 00 00 81"));
        assertTrue(nullableUInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableUInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable2() {
        nullableUInt64Decoder.decode(fromHex("02 00 00 00 00 00 00 00 00 00 80"));
        assertTrue(nullableUInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableUInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory1() {
        mandatoryUInt64Decoder.decode(fromHex("02 00 00 00 00 00 00 00 00 80"));
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryUInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory2() {
        mandatoryUInt64Decoder.decode(fromHex("01 7f 7f 7f 7f 00 7f 7f 7f 7f ff"));
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryUInt64Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber1() throws OverflowException {
        nullableUInt64Decoder.decode(fromHex("39 45 a4"));
        assertTrue(nullableUInt64Decoder.isReady());
        assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
    }

    @Test
    void optionalSimpleNumber2() throws OverflowException {
        nullableUInt64Decoder.decode(fromHex("01 7f 7f 7f 7f 7f 7f 7f 7f ff"));
        assertTrue(nullableUInt64Decoder.isReady());
        assertEquals(new BigInteger("18446744073709551614"), nullableUInt64Decoder.getValue());
    }

    @Test
    void mandatorySimpleNumber1() throws OverflowException {
        mandatoryUInt64Decoder.decode(fromHex("39 45 a3"));
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
    }

    @Test
    void mandatorySimpleNumber2() throws OverflowException {
        mandatoryUInt64Decoder.decode(fromHex("01 10 78 20 76 62 2a 62 51 cf"));
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertEquals(new BigInteger("10443992354206034127"), mandatoryUInt64Decoder.getValue());
    }

    @Test
    void optionalSimpleNumber1GetValueTwice() throws OverflowException {
        nullableUInt64Decoder.decode(fromHex("39 45 a4"));
        assertTrue(nullableUInt64Decoder.isReady());
        assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
        assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
    }

    @Test
    void mandatorySimpleNumber1GetValueTwice() throws OverflowException {
        mandatoryUInt64Decoder.decode(fromHex("39 45 a3"));
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
        assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
    }

    @Test
    void optionalSimpleNumbersTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("39 45 a4 01 7f 7f 7f 7f 7f 7f 7f 7f ff");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());

        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertEquals(new BigInteger("18446744073709551614"), nullableUInt64Decoder.getValue());
    }

    @Test
    void mandatorySimpleNumbersTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("39 45 a3 01 10 78 20 76 62 2a 62 51 cf");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());

        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertEquals(new BigInteger("10443992354206034127"), mandatoryUInt64Decoder.getValue());
    }
}
