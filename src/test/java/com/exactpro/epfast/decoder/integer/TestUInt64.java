package com.exactpro.epfast.decoder.integer;

import com.exactpro.junit5.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.junit5.ByteBufUtils.*;
import static com.exactpro.epfast.DecoderUtils.*;

class TestUInt64 {

    private DecodeNullableUInt64 nullableUInt64Decoder = new DecodeNullableUInt64();

    private DecodeMandatoryUInt64 mandatoryUInt64Decoder = new DecodeMandatoryUInt64();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() throws IOException {
        withByteBuf("80", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertNull(nullableUInt64Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() throws IOException {
        withByteBuf("81", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertEquals(new BigInteger("0"), nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryZero() throws IOException {
        withByteBuf("80", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertEquals(new BigInteger("0"), mandatoryUInt64Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() throws IOException {
        withByteBuf("02 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertEquals(new BigInteger("18446744073709551615"), nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxMandatory() throws IOException {
        withByteBuf("01 7f 7f 7f 7f 7f 7f 7f 7f ff", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertEquals(new BigInteger("18446744073709551615"), mandatoryUInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowNullable1() throws IOException {
        withByteBuf("02 00 00 00 00 00 00 00 00 81", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertThrows(IOException.class, () -> nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowNullable2() throws IOException {
        withByteBuf("02 00 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertThrows(IOException.class, () -> nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowMandatory1() throws IOException {
        withByteBuf("02 00 00 00 00 00 00 00 00 80", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryUInt64Decoder.getValue());
        });
    }

    @Test
    void testMaxOverflowMandatory2() throws IOException {
        withByteBuf("01 7f 7f 7f 7f 00 7f 7f 7f 7f ff", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertThrows(IOException.class, () -> mandatoryUInt64Decoder.getValue());
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber1() throws IOException {
        withByteBuf("39 45 a4", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void optionalSimpleNumber2() throws IOException {
        withByteBuf("01 7f 7f 7f 7f 7f 7f 7f 7f ff", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertEquals(new BigInteger("18446744073709551614"), nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void mandatorySimpleNumber1() throws IOException {
        withByteBuf("39 45 a3", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
        });
    }

    @Test
    void mandatorySimpleNumber2() throws IOException {
        withByteBuf("01 10 78 20 76 62 2a 62 51 cf", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertEquals(new BigInteger("10443992354206034127"), mandatoryUInt64Decoder.getValue());
        });
    }

    @Test
    void optionalSimpleNumber1GetValueTwice() throws IOException {
        withByteBuf("39 45 a4", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void mandatorySimpleNumber1GetValueTwice() throws IOException {
        withByteBuf("39 45 a3", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
            assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());
        });
    }

    @Test
    void optionalSimpleNumbersTwoValuesInRow() throws IOException {
        withByteBuf("39 45 a4 01 7f 7f 7f 7f 7f 7f 7f 7f ff", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());

            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertEquals(new BigInteger("18446744073709551614"), nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void mandatorySimpleNumbersTwoValuesInRow() throws IOException {
        withByteBuf("39 45 a3 01 10 78 20 76 62 2a 62 51 cf", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertEquals(new BigInteger("942755"), mandatoryUInt64Decoder.getValue());

            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertEquals(new BigInteger("10443992354206034127"), mandatoryUInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryOverlong() throws IOException {
        withByteBuf("00 39 45 a4", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertTrue(mandatoryUInt64Decoder.isOverlong());
            assertEquals(new BigInteger("942756"), mandatoryUInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryNotOverlong() throws IOException {
        withByteBuf("00 40 81", buffers -> {
            decode(mandatoryUInt64Decoder, buffers);
            assertTrue(mandatoryUInt64Decoder.isReady());
            assertFalse(mandatoryUInt64Decoder.isOverlong());
            assertEquals(new BigInteger("8193"), mandatoryUInt64Decoder.getValue());
        });
    }

    @Test
    void mandatoryOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("39 45 a4"));
        decode(mandatoryUInt64Decoder, buffers);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertTrue(mandatoryUInt64Decoder.isOverlong());
        assertEquals(new BigInteger("942756"), mandatoryUInt64Decoder.getValue());
    }

    @Test
    void mandatoryNotOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("40 81"));
        decode(mandatoryUInt64Decoder, buffers);
        assertTrue(mandatoryUInt64Decoder.isReady());
        assertFalse(mandatoryUInt64Decoder.isOverlong());
        assertEquals(new BigInteger("8193"), mandatoryUInt64Decoder.getValue());
    }

    @Test
    void nullableOverlong() throws IOException {
        withByteBuf("00 39 45 a4", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertTrue(nullableUInt64Decoder.isOverlong());
            assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void nullableNotOverlong() throws IOException {
        withByteBuf("00 40 81", buffers -> {
            decode(nullableUInt64Decoder, buffers);
            assertTrue(nullableUInt64Decoder.isReady());
            assertFalse(nullableUInt64Decoder.isOverlong());
            assertEquals(new BigInteger("8192"), nullableUInt64Decoder.getValue());
        });
    }

    @Test
    void nullableOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("39 45 a4"));
        decode(nullableUInt64Decoder, buffers);
        assertTrue(nullableUInt64Decoder.isReady());
        assertTrue(nullableUInt64Decoder.isOverlong());
        assertEquals(new BigInteger("942755"), nullableUInt64Decoder.getValue());
    }

    @Test
    void nullableNotOverlongSplit() throws IOException {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        buffers.add(ByteBufUtils.fromHex("00"));
        buffers.add(ByteBufUtils.fromHex("40 81"));
        decode(nullableUInt64Decoder, buffers);
        assertTrue(nullableUInt64Decoder.isReady());
        assertFalse(nullableUInt64Decoder.isOverlong());
        assertEquals(new BigInteger("8192"), nullableUInt64Decoder.getValue());
    }
}
