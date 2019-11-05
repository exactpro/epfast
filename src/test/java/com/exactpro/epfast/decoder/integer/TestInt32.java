package com.exactpro.epfast.decoder.integer;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestInt32 {

    private DecodeNullableInt32 nullableInt32Decoder = new DecodeNullableInt32();

    private DecodeMandatoryInt32 mandatoryInt32Decoder = new DecodeMandatoryInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() throws IOException {
        withByteBuf("80", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertNull(nullableInt32Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() throws IOException {
        withByteBuf("81", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(0, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryZero() throws IOException {
        withByteBuf("80", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(0, mandatoryInt32Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test min/max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() throws IOException {
        withByteBuf("08 00 00 00 80", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(Integer.MAX_VALUE, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxMandatory() throws IOException {
        withByteBuf("07 7f 7f 7f ff", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(Integer.MAX_VALUE, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void testMinNullable() throws IOException {
        withByteBuf("78 00 00 00 80", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(Integer.MIN_VALUE, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void testMinMandatory() throws IOException {
        withByteBuf("78 00 00 00 80", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(Integer.MIN_VALUE, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowNullable1() throws IOException {
        withByteBuf("08 00 00 00 81", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertThrows(IOException.class, () -> nullableInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowNullable2() throws IOException {
        withByteBuf("08 00 00 00 00 00 00 80", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertThrows(IOException.class, () -> nullableInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowMandatory1() throws IOException {
        withByteBuf("08 00 00 00 80", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowMandatory2() throws IOException {
        withByteBuf("07 7f 00 7f 7f 7f ff", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void testMinOverflowNullable1() throws IOException {
        withByteBuf("77 7f 7f 7f ff", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertThrows(IOException.class, () -> nullableInt32Decoder.getValue());
        });
    }

    @Test
    void testMinOverflowNullable2() throws IOException {
        withByteBuf("78 00 00 00 00 80", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertThrows(IOException.class, () -> nullableInt32Decoder.getValue());
        });
    }

    @Test
    void testMinOverflowMandatory1() throws IOException {
        withByteBuf("77 7f 7f 7f ff", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void testMinOverflowMandatory2() throws IOException {
        withByteBuf("78 00 00 00 00 80", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryInt32Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() throws IOException {
        withByteBuf("39 45 a4", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(942755, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryPositive() throws IOException {
        withByteBuf("39 45 a3", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(942755, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void optionalNegative() throws IOException {
        withByteBuf("46 3a dd", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(-942755, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryNegative() throws IOException {
        withByteBuf("7c 1b 1b 9d", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(-7942755, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void optionalMinusOne() throws IOException {
        withByteBuf("ff", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(-1, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryMinusOne() throws IOException {
        withByteBuf("ff", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(-1, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void optionalSignExtensionPositive() throws IOException {
        withByteBuf("00 00 40 82", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(8193, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void mandatorySignExtensionPositive() throws IOException {
        withByteBuf("00 00 40 81", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(8193, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void optionalSignExtensionNegative() throws IOException {
        withByteBuf("7f 3f ff", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(-8193, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void mandatorySignExtensionNegative() throws IOException {
        withByteBuf("7f 3f ff", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(-8193, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryNegativeTwoValuesInRow() throws IOException {
        withByteBuf("7f 3f ff 7f 3f ff", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(-8193, mandatoryInt32Decoder.getValue());

            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(-8193, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryPositiveTwoValuesInRow() throws IOException {
        withByteBuf("00 00 40 81 00 00 40 81", buffers -> {
            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(8193, mandatoryInt32Decoder.getValue());

            decode(mandatoryInt32Decoder, buffers);
            assertTrue(mandatoryInt32Decoder.isReady());
            assertEquals(8193, mandatoryInt32Decoder.getValue());
        });
    }

    @Test
    void optionalNegativeTwoValuesInRow() throws IOException {
        withByteBuf("7f 3f ff 7f 3f ff", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(-8193, nullableInt32Decoder.getValue());

            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(-8193, nullableInt32Decoder.getValue());
        });
    }

    @Test
    void optionalPositiveTwoValuesInRow() throws IOException {
        withByteBuf("00 00 40 82 00 00 40 82", buffers -> {
            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(8193, nullableInt32Decoder.getValue());

            decode(nullableInt32Decoder, buffers);
            assertTrue(nullableInt32Decoder.isReady());
            assertEquals(8193, nullableInt32Decoder.getValue());
        });
    }
}
