package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.decoder.FillBuffer.*;

class TestDecodeAsciiString {

    private DecodeNullableAsciiString nullableStringDecoder = new DecodeNullableAsciiString();

    private DecodeMandatoryAsciiString mandatoryStringDecoder = new DecodeMandatoryAsciiString();

    @Test
    void testNull() throws OverflowException {
        nullableStringDecoder.decode(fromHex("80"));
        assertTrue(nullableStringDecoder.isReady());
        assertNull(nullableStringDecoder.getValue());
    }

    @Test
    void testOptionalEmptyString() throws OverflowException {
        nullableStringDecoder.decode(fromHex("00 80"));
        assertTrue(nullableStringDecoder.isReady());
        assertEquals("", nullableStringDecoder.getValue());
    }

    @Test
    void testMandatoryEmptyString() throws OverflowException {
        mandatoryStringDecoder.decode(fromHex("80"));
        assertTrue(mandatoryStringDecoder.isReady());
        assertEquals("", mandatoryStringDecoder.getValue());
    }

    @Test
    void testSimpleString() throws OverflowException {
        nullableStringDecoder.decode(fromHex("41 42 c3"));
        assertTrue(nullableStringDecoder.isReady());
        assertEquals("ABC", nullableStringDecoder.getValue());
    }

    @Test
    void testZeroByteStringNullable1() throws OverflowException {
        nullableStringDecoder.decode(fromHex("00 00 80"));
        assertTrue(nullableStringDecoder.isReady());
        assertEquals("\0", nullableStringDecoder.getValue());
    }

    @Test
    void testZeroByteStringNullable2() throws OverflowException {
        nullableStringDecoder.decode(fromHex("00 00 00 00 80"));
        assertTrue(nullableStringDecoder.isReady());
        assertEquals("\0\0\0", nullableStringDecoder.getValue());
    }

    @Test
    void testZeroByteStringMandatory1() throws OverflowException {
        mandatoryStringDecoder.decode(fromHex("00 00 80"));
        assertTrue(mandatoryStringDecoder.isReady());
        assertEquals("\0\0", mandatoryStringDecoder.getValue());
    }

    @Test
    void testZeroByteStringMandatory2() throws OverflowException {
        mandatoryStringDecoder.decode(fromHex("00 00 00 00 80"));
        assertTrue(mandatoryStringDecoder.isReady());
        assertEquals("\0\0\0\0", mandatoryStringDecoder.getValue());
    }

    @Test
    void testOverlong1() throws OverflowException {
        mandatoryStringDecoder.decode(fromHex("00 81"));
        assertTrue(mandatoryStringDecoder.isReady());

        mandatoryStringDecoder.setCheckOverlong();
        assertThrows(OverflowException.class, () -> mandatoryStringDecoder.getValue());
        mandatoryStringDecoder.clearCheckOverlong();
        mandatoryStringDecoder.getValue();
    }

    @Test
    void testOverlong2() throws OverflowException {
        nullableStringDecoder.decode(fromHex("00 00 00 81"));
        assertTrue(nullableStringDecoder.isReady());

        nullableStringDecoder.setCheckOverlong();
        assertThrows(OverflowException.class, () -> nullableStringDecoder.getValue());
        nullableStringDecoder.clearCheckOverlong();
        nullableStringDecoder.getValue();
    }

    @Test
    void testNullableReuse() throws OverflowException {
        ByteBuf buf = fromHex("41 42 c3 42 42 c3 41 44 c3");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        assertEquals("ABC", nullableStringDecoder.getValue());
        nullableStringDecoder.decode(buf);
        assertEquals("BBC", nullableStringDecoder.getValue());
        nullableStringDecoder.decode(buf);
        assertEquals("ADC", nullableStringDecoder.getValue());
    }

    @Test
    void testSimpleStringGetValueTwice() throws OverflowException {
        nullableStringDecoder.decode(fromHex("41 42 c3"));
        assertTrue(nullableStringDecoder.isReady());
        assertEquals("ABC", nullableStringDecoder.getValue());
        assertEquals("ABC", nullableStringDecoder.getValue());
    }

    @Test
    void testMandatoryEmptyStringGetValueTwice() throws OverflowException {
        mandatoryStringDecoder.decode(fromHex("80"));
        assertTrue(mandatoryStringDecoder.isReady());
        assertEquals("", mandatoryStringDecoder.getValue());
        assertEquals("", mandatoryStringDecoder.getValue());
    }

    @Test
    void testZeroByteStringNullableTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("00 00 80 00 00 00 00 80");
        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        assertEquals("\0", nullableStringDecoder.getValue());

        nullableStringDecoder.decode(buf);
        assertTrue(nullableStringDecoder.isReady());
        assertEquals("\0\0\0", nullableStringDecoder.getValue());
    }

    @Test
    void testZeroByteStringMandatoryTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("00 00 80 00 00 00 00 80");
        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());
        assertEquals("\0\0", mandatoryStringDecoder.getValue());

        mandatoryStringDecoder.decode(buf);
        assertTrue(mandatoryStringDecoder.isReady());
        assertEquals("\0\0\0\0", mandatoryStringDecoder.getValue());
    }
}
