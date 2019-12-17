package com.exactpro.epfast.decoder.integer;

import com.exactpro.junit5.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.junit5.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestUInt32 {

    private DecodeNullableUInt32 nullableUInt32Decoder = new DecodeNullableUInt32();

    private DecodeMandatoryUInt32 mandatoryUInt32Decoder = new DecodeMandatoryUInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() throws IOException {
        withByteBuf("80", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertNull(nullableUInt32Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() throws IOException {
        withByteBuf("81", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertEquals(0, nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryZero() throws IOException {
        withByteBuf("80", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertEquals(0, mandatoryUInt32Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() throws IOException {
        withByteBuf("10 00 00 00 80", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertEquals(4294967295L, nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxMandatory() throws IOException {
        withByteBuf("0f 7f 7f 7f ff", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertEquals(4294967295L, mandatoryUInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowNullable1() throws IOException {
        withByteBuf("10 00 00 00 81", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertThrows(IOException.class, () -> nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowNullable2() throws IOException {
        withByteBuf("10 00 00 00 00 00 80", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertThrows(IOException.class, () -> nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowMandatory1() throws IOException {
        withByteBuf("10 00 00 00 00 80", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryUInt32Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowMandatory2() throws IOException {
        withByteBuf("0f 7f 7f 7f 7f 00 ff", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryUInt32Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber() throws IOException {
        withByteBuf("39 45 a4", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertEquals(942755, nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void optionalSimpleNumber2() throws IOException {
        withByteBuf("0f 7f 7f 7f ff", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertEquals(4294967294L, nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void mandatorySimpleNumber() throws IOException {
        withByteBuf("39 45 a3", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
        });
    }

    @Test
    void optionalSimpleNumberGetValueTwice() throws IOException {
        withByteBuf("39 45 a4", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertEquals(942755, nullableUInt32Decoder.getValue());
            assertEquals(942755, nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void mandatorySimpleNumberGetValueTwice() throws IOException {
        withByteBuf("39 45 a3", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
        });
    }

    @Test
    void optionalSimpleNumbersTwoValuesInRow() throws IOException {
        withByteBuf("39 45 a4 0f 7f 7f 7f ff", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertEquals(942755, nullableUInt32Decoder.getValue());

            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertEquals(4294967294L, nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void mandatorySimpleNumbersTwoValuesInRow() throws IOException {
        withByteBuf("39 45 a3 39 45 a3", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertEquals(942755, mandatoryUInt32Decoder.getValue());

            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertEquals(942755, mandatoryUInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryOverlong() throws IOException {
        withByteBuf("00 39 45 a4", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertTrue(mandatoryUInt32Decoder.isOverlong());
            assertEquals(942756, mandatoryUInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryNotOverlong() throws IOException {
        withByteBuf("00 40 81", buffers -> {
            decode(mandatoryUInt32Decoder, buffers);
            assertTrue(mandatoryUInt32Decoder.isReady());
            assertFalse(mandatoryUInt32Decoder.isOverlong());
            assertEquals(8193, mandatoryUInt32Decoder.getValue());
        });
    }

    @Test
    void mandatoryOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("39 45 a4"));
        decode(mandatoryUInt32Decoder, buffers);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertTrue(mandatoryUInt32Decoder.isOverlong());
        assertEquals(942756, mandatoryUInt32Decoder.getValue());
    }

    @Test
    void mandatoryNotOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("40 81"));
        decode(mandatoryUInt32Decoder, buffers);
        assertTrue(mandatoryUInt32Decoder.isReady());
        assertFalse(mandatoryUInt32Decoder.isOverlong());
        assertEquals(8193, mandatoryUInt32Decoder.getValue());
    }

    @Test
    void nullableOverlong() throws IOException {
        withByteBuf("00 39 45 a4", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertTrue(nullableUInt32Decoder.isOverlong());
            assertEquals(942755, nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void nullableNotOverlong() throws IOException {
        withByteBuf("00 40 81", buffers -> {
            decode(nullableUInt32Decoder, buffers);
            assertTrue(nullableUInt32Decoder.isReady());
            assertFalse(nullableUInt32Decoder.isOverlong());
            assertEquals(8192, nullableUInt32Decoder.getValue());
        });
    }

    @Test
    void nullableOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("39 45 a4"));
        decode(nullableUInt32Decoder, buffers);
        assertTrue(nullableUInt32Decoder.isReady());
        assertTrue(nullableUInt32Decoder.isOverlong());
        assertEquals(942755, nullableUInt32Decoder.getValue());
    }

    @Test
    void nullableNotOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("40 81"));
        decode(nullableUInt32Decoder, buffers);
        assertTrue(nullableUInt32Decoder.isReady());
        assertFalse(nullableUInt32Decoder.isOverlong());
        assertEquals(8192, nullableUInt32Decoder.getValue());
    }
}
