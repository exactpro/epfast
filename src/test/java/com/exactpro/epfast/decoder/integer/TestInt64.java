package com.exactpro.epfast.decoder.integer;

import com.exactpro.junit5.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.junit5.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestInt64 {

    private DecodeNullableInt64 nullableInt64Decoder = new DecodeNullableInt64();

    private DecodeMandatoryInt64 mandatoryInt64Decoder = new DecodeMandatoryInt64();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() throws IOException {
        withByteBuf("80", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertNull(nullableInt64Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() throws IOException {
        withByteBuf("81", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(0, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryZero() throws IOException {
        withByteBuf("80", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(0, mandatoryInt64Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test min/max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() throws IOException {
        withByteBuf("01 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(Long.MAX_VALUE, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxMandatory() throws IOException {
        withByteBuf("00 7f 7f 7f 7f 7f 7f 7f 7f ff", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(Long.MAX_VALUE, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void testMinNullable() throws IOException {
        withByteBuf("7f 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(Long.MIN_VALUE, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void testMinMandatory() throws IOException {
        withByteBuf("7f 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(Long.MIN_VALUE, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowNullable1() throws IOException {
        withByteBuf("01 00 00 00 00 00 00 00 00 81", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertThrows(IOException.class, () -> nullableInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowNullable2() throws IOException {
        withByteBuf("01 00 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertThrows(IOException.class, () -> nullableInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowMandatory1() throws IOException {
        withByteBuf("01 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowMandatory2() throws IOException {
        withByteBuf("00 7f 00 7f 7f 7f 7f 7f 7f 7f ff", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void testMinOverflowNullable1() throws IOException {
        withByteBuf("77 7f 7f 7f 7f 7f 7f 7f 7f ff", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertThrows(IOException.class, () -> nullableInt64Decoder.getValue());
        });
    }

    @Test
    void testMinOverflowNullable2() throws IOException {
        withByteBuf("7f 00 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertThrows(IOException.class, () -> nullableInt64Decoder.getValue());
        });
    }

    @Test
    void testMinOverflowMandatory1() throws IOException {
        withByteBuf("77 7f 7f 7f 7f 7f 7f 7f 7f ff", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void testMinOverflowMandatory2() throws IOException {
        withByteBuf("7f 00 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryInt64Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalPositive() throws IOException {
        withByteBuf("39 45 a4", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(942755, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryPositive() throws IOException {
        withByteBuf("39 45 a3", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(942755, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void optionalNegative() throws IOException {
        withByteBuf("46 3a dd", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(-942755, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryNegative() throws IOException {
        withByteBuf("7c 1b 1b 9d", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(-7942755, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void optionalMinusOne() throws IOException {
        withByteBuf("ff", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(-1, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryMinusOne() throws IOException {
        withByteBuf("ff", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(-1, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void optionalSignExtensionPositive() throws IOException {
        withByteBuf("00 00 40 82", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(8193, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void mandatorySignExtensionPositive() throws IOException {
        withByteBuf("00 00 40 81", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(8193, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void optionalSignExtensionNegative() throws IOException {
        withByteBuf("7f 3f ff", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(-8193, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void mandatorySignExtensionNegative() throws IOException {
        withByteBuf("7f 3f ff", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(-8193, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryNegativeTwoValuesInRow() throws IOException {
        withByteBuf("7f 3f ff 7f 3f ff", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(-8193, mandatoryInt64Decoder.getValue());

            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(-8193, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryPositiveTwoValuesInRow() throws IOException {
        withByteBuf("00 00 40 81 00 00 40 81", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(8193, mandatoryInt64Decoder.getValue());

            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertEquals(8193, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void optionalNegativeTwoValuesInRow() throws IOException {
        withByteBuf("7f 3f ff 7f 3f ff", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(-8193, nullableInt64Decoder.getValue());

            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(-8193, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void optionalPositiveTwoValuesInRow() throws IOException {
        withByteBuf("00 00 40 82 00 00 40 82", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(8193, nullableInt64Decoder.getValue());

            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertEquals(8193, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryOverlong() throws IOException {
        withByteBuf("00 39 45 a4", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertTrue(mandatoryInt64Decoder.isOverlong());
            assertEquals(942756, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryNotOverlong() throws IOException {
        withByteBuf("00 40 81", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertFalse(mandatoryInt64Decoder.isOverlong());
            assertEquals(8193, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryOverlongNegative() throws IOException {
        withByteBuf("7f 7c 1b 1b 9d", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertTrue(mandatoryInt64Decoder.isOverlong());
            assertEquals(-7942755, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryNotOverlongNegative() throws IOException {
        withByteBuf("7f 3f ff", buffers -> {
            decode(mandatoryInt64Decoder, buffers);
            assertTrue(mandatoryInt64Decoder.isReady());
            assertFalse(mandatoryInt64Decoder.isOverlong());
            assertEquals(-8193, mandatoryInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("39 45 a4"));
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverlong());
        assertEquals(942756, mandatoryInt64Decoder.getValue());
    }

    @Test
    void mandatoryOverlongSplitNegative() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("7f"));
        buffers.add(ByteBufUtils.fromHex("7c 1b 1b 9d"));
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertTrue(mandatoryInt64Decoder.isOverlong());
        assertEquals(-7942755, mandatoryInt64Decoder.getValue());
    }

    @Test
    void mandatoryNotOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("40 81"));
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverlong());
        assertEquals(8193, mandatoryInt64Decoder.getValue());
    }

    @Test
    void mandatoryNotOverlongNegativeSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("7f"));
        buffers.add(ByteBufUtils.fromHex("3f ff"));
        decode(mandatoryInt64Decoder, buffers);
        assertTrue(mandatoryInt64Decoder.isReady());
        assertFalse(mandatoryInt64Decoder.isOverlong());
        assertEquals(-8193, mandatoryInt64Decoder.getValue());
    }

    @Test
    void nullableOverlong() throws IOException {
        withByteBuf("00 39 45 a4", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertTrue(nullableInt64Decoder.isOverlong());
            assertEquals(942755, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void nullableNotOverlong() throws IOException {
        withByteBuf("00 40 81", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertFalse(nullableInt64Decoder.isOverlong());
            assertEquals(8192, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void nullableOverlongNegative() throws IOException {
        withByteBuf("7f 7c 1b 1b 9d", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertTrue(nullableInt64Decoder.isOverlong());
            assertEquals(-7942755, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void nullableNotOverlongNegative() throws IOException {
        withByteBuf("7f 3f ff", buffers -> {
            decode(nullableInt64Decoder, buffers);
            assertTrue(nullableInt64Decoder.isReady());
            assertFalse(nullableInt64Decoder.isOverlong());
            assertEquals(-8193, nullableInt64Decoder.getValue());
        });
    }

    @Test
    void nullableOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("39 45 a4"));
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverlong());
        assertEquals(942755, nullableInt64Decoder.getValue());
    }

    @Test
    void nullableOverlongSplitNegative() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("7f"));
        buffers.add(ByteBufUtils.fromHex("7c 1b 1b 9d"));
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertTrue(nullableInt64Decoder.isOverlong());
        assertEquals(-7942755, nullableInt64Decoder.getValue());
    }

    @Test
    void nullableNotOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("40 81"));
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverlong());
        assertEquals(8192, nullableInt64Decoder.getValue());
    }

    @Test
    void nullableNotOverlongNegativeSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("7f"));
        buffers.add(ByteBufUtils.fromHex("3f ff"));
        decode(nullableInt64Decoder, buffers);
        assertTrue(nullableInt64Decoder.isReady());
        assertFalse(nullableInt64Decoder.isOverlong());
        assertEquals(-8193, nullableInt64Decoder.getValue());
    }
}
