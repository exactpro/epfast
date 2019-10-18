package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.FillBuffer;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        try {
            assertNull(nullableInt64Decoder.getValue());
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
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(0, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = FillBuffer.fromHex("80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(0, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test min/max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = FillBuffer.fromHex("01 00 00 00 00 00 00 00 00 80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(Long.MAX_VALUE, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = FillBuffer.fromHex("00 7f 7f 7f 7f 7f 7f 7f 7f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(Long.MAX_VALUE, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMinNullable() {
        ByteBuf buf = FillBuffer.fromHex("7f 00 00 00 00 00 00 00 00 80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(Long.MIN_VALUE, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMinMandatory() {
        ByteBuf buf = FillBuffer.fromHex("7f 00 00 00 00 00 00 00 00 80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        
        try {
            assertEquals(Long.MIN_VALUE, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("01 00 00 00 00 00 00 00 00 81");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("01 00 00 00 00 00 00 00 00 00 80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("01 00 00 00 00 00 00 00 00 80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("00 7f 00 7f 7f 7f 7f 7f 7f 7f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @Test
    void testMinOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("77 7f 7f 7f 7f 7f 7f 7f 7f ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt64Decoder.getValue());
    }

    @Test
    void testMinOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("7f 00 00 00 00 00 00 00 00 00 80");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableInt64Decoder.getValue());
    }

    @Test
    void testMinOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("77 7f 7f 7f 7f 7f 7f 7f 7f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt64Decoder.getValue());
    }

    @Test
    void testMinOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("7f 00 00 00 00 00 00 00 00 00 80");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryInt64Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(942755, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalPositiveSplit() {
        ByteBuf buf = FillBuffer.fromHex("39 45");
        nullableInt64Decoder.decode(buf);
        assertFalse(nullableInt64Decoder.isReady());

        buf = FillBuffer.fromHex("a4");
        nullableInt64Decoder.continueDecode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(942755, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryPositive() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(942755, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryPositiveSplit() {
        ByteBuf buf = FillBuffer.fromHex("39 45");
        mandatoryInt64Decoder.decode(buf);
        assertFalse(mandatoryInt64Decoder.isReady());

        buf = FillBuffer.fromHex("a3");
        mandatoryInt64Decoder.continueDecode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(942755, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalNegative() {
        ByteBuf buf = FillBuffer.fromHex("46 3a dd");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(-942755, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalNegativeSplit() {
        ByteBuf buf = FillBuffer.fromHex("46 3a");
        nullableInt64Decoder.decode(buf);
        assertFalse(nullableInt64Decoder.isReady());

        buf = FillBuffer.fromHex("dd");
        nullableInt64Decoder.continueDecode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(-942755, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryNegative() {
        ByteBuf buf = FillBuffer.fromHex("7c 1b 1b 9d");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(-7942755, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryNegativeSplit() {
        ByteBuf buf = FillBuffer.fromHex("7c 1b");
        mandatoryInt64Decoder.decode(buf);
        assertFalse(mandatoryInt64Decoder.isReady());

        buf = FillBuffer.fromHex("1b 9d");
        mandatoryInt64Decoder.continueDecode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(-7942755, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalMinusOne() {
        ByteBuf buf = FillBuffer.fromHex("ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(-1, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryMinusOne() {
        ByteBuf buf = FillBuffer.fromHex("ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(-1, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalSignExtensionPositive() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 82");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(8193, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySignExtensionPositive() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 81");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(8193, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalSignExtensionNegative() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(-8193, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySignExtensionNegative() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(-8193, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryNegativeTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff 7f 3f ff");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(-8193, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }

        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(-8193, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryPositiveTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 81 00 00 40 81");
        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(8193, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }

        mandatoryInt64Decoder.decode(buf);
        assertTrue(mandatoryInt64Decoder.isReady());
        try {
            assertEquals(8193, mandatoryInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalNegativeTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("7f 3f ff 7f 3f ff");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(-8193, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }

        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(-8193, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalPositiveTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("00 00 40 82 00 00 40 82");
        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(8193, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }

        nullableInt64Decoder.decode(buf);
        assertTrue(nullableInt64Decoder.isReady());
        try {
            assertEquals(8193, nullableInt64Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }
}
