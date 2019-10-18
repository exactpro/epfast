package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.FillBuffer;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        try {
            assertNull(nullableUInt32Decoder.getValue());
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
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        try {
            assertEquals(0, nullableUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = FillBuffer.fromHex("80");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        try {
            assertEquals(0, mandatoryUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = FillBuffer.fromHex("10 00 00 00 80");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        try {
            assertEquals(4294967295L, nullableUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = FillBuffer.fromHex("0f 7f 7f 7f ff");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        try {
            assertEquals(4294967295L, mandatoryUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = FillBuffer.fromHex("10 00 00 00 81");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableUInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = FillBuffer.fromHex("10 00 00 00 00 00 80");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> nullableUInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("10 00 00 00 00 80");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryUInt32Decoder.getValue());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("0f 7f 7f 7f 7f 00 ff");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertThrows(OverflowException.class, () -> mandatoryUInt32Decoder.getValue());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        try {
            assertEquals(942755, nullableUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalSimpleNumber2() {
        ByteBuf buf = FillBuffer.fromHex("0f 7f 7f 7f ff");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        try {
            assertEquals(4294967294L, nullableUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySimpleNumber() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        try {
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalSimpleNumberGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        try {
            assertEquals(942755, nullableUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals(942755, nullableUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySimpleNumberGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        try {
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void optionalSimpleNumbersTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a4 0f 7f 7f 7f ff");
        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        try {
            assertEquals(942755, nullableUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }

        nullableUInt32Decoder.decode(buf);
        assertTrue(nullableUInt32Decoder.isReady());
        try {
            assertEquals(4294967294L, nullableUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void mandatorySimpleNumbersTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("39 45 a3 39 45 a3");
        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        try {
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }

        mandatoryUInt32Decoder.decode(buf);
        assertTrue(mandatoryUInt32Decoder.isReady());
        try {
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }
}
