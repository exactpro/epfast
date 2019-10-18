package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.FillBuffer;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

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
        try {
            assertNull(nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() {
        ByteBuf buf = FillBuffer.fromHex("81");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("0"), nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = FillBuffer.fromHex("80");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("0"), mandatoryUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = FillBuffer.fromHex("02 00 00 00 00 00 00 00 00 80");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("18446744073709551615"), nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = FillBuffer.fromHex("01 7f 7f 7f 7f 7f 7f 7f 7f ff");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("18446744073709551615"), mandatoryUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("02 00 00 00 00 00 00 00 00 81");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableUInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("02 00 00 00 00 00 00 00 00 00 80");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableUInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("02 00 00 00 00 00 00 00 00 80");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryUInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("01 7f 7f 7f 7f 00 7f 7f 7f 7f ff");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryUInt64Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber1() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalSimpleNumber2() {
        ByteBuf buf = FillBuffer.fromHex("01 7f 7f 7f 7f 7f 7f 7f 7f ff");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("18446744073709551614"), nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySimpleNumber1() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySimpleNumber2() {
        ByteBuf buf = FillBuffer.fromHex("01 10 78 20 76 62 2a 62 51 cf");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("10443992354206034127"), mandatoryUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalSimpleNumber1GetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySimpleNumber1GetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalSimpleNumbersTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4 01 7f 7f 7f 7f 7f 7f 7f 7f ff");
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        nullableUInt64Decoder.decode(buf);
        assertTrue(nullableUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("18446744073709551614"), nullableUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySimpleNumbersTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3 01 10 78 20 76 62 2a 62 51 cf");
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        mandatoryUInt64Decoder.decode(buf);
        assertTrue(mandatoryUInt64Decoder.isReady());
        try {
            assertEquals(new BigInteger("10443992354206034127"), mandatoryUInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }
}
