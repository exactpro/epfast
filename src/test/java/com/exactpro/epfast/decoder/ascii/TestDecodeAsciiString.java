package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.FillBuffer;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDecodeAsciiString {

    private DecodeNullableAsciiString nullableStringDecoder = new DecodeNullableAsciiString();

    private DecodeMandatoryAsciiString mandatoryStringDecoder = new DecodeMandatoryAsciiString();

    @Test
    void testNull() {
        ByteBuf buf = FillBuffer.fromHex("80");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertNull(nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testOptionalEmptyString() {
        ByteBuf buf = FillBuffer.fromHex("00 80");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertEquals("", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryEmptyString() {
        ByteBuf buf = FillBuffer.fromHex("80");
        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());
        try {
            assertEquals("", mandatoryStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testSimpleString() {
        ByteBuf buf = FillBuffer.fromHex("41 42 c3");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertEquals("ABC", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testZeroByteStringNullable1() {
        ByteBuf buf = FillBuffer.fromHex("00 00 80");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertEquals("\0", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testZeroByteStringNullable2() {
        ByteBuf buf = FillBuffer.fromHex("00 00 00 00 80");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertEquals("\0\0\0", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testZeroByteStringMandatory1() {
        ByteBuf buf = FillBuffer.fromHex("00 00 80");
        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());
        try {
            assertEquals("\0\0", mandatoryStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testZeroByteStringMandatory2() {
        ByteBuf buf = FillBuffer.fromHex("00 00 00 00 80");
        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());
        try {
            assertEquals("\0\0\0\0", mandatoryStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testOverlong1() {
        ByteBuf buf = FillBuffer.fromHex("00 81");
        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());

        mandatoryStringDecoder.setCheckOverlong();
        assertThrows(OverflowException.class, () -> mandatoryStringDecoder.getValue());
        mandatoryStringDecoder.clearCheckOverlong();
        try {
            mandatoryStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testOverlong2() {
        ByteBuf buf = FillBuffer.fromHex("00 00 00 81");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());

        nullableStringDecoder.setCheckOverlong();
        assertThrows(OverflowException.class, () -> nullableStringDecoder.getValue());
        nullableStringDecoder.clearCheckOverlong();
        try {
            nullableStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullableReuse() {
        ByteBuf buf = FillBuffer.fromHex("41 42 c3 42 42 c3 41 44 c3");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertEquals("ABC", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        nullableStringDecoder.decode(buf);
        try {
            assertEquals("BBC", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        nullableStringDecoder.decode(buf);
        try {
            assertEquals("ADC", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testSimpleStringGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("41 42 c3");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertEquals("ABC", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals("ABC", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryEmptyStringGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("80");
        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());
        try {
            assertEquals("", mandatoryStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals("", mandatoryStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testZeroByteStringNullableTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("00 00 80 00 00 00 00 80");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertEquals("\0", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        try {
            assertEquals("\0\0\0", nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testZeroByteStringMandatoryTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("00 00 80 00 00 00 00 80");
        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());
        try {
            assertEquals("\0\0", mandatoryStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());
        try {
            assertEquals("\0\0\0\0", mandatoryStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }
}
