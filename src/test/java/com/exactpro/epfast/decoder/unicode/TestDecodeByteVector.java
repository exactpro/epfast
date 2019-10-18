package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static com.exactpro.epfast.decoder.FillBuffer.*;

class TestDecodeByteVector {

    private DecodeNullableByteVector nullableByteVectorDecoder = new DecodeNullableByteVector();

    private DecodeMandatoryByteVector mandatoryByteVectorDecoder = new DecodeMandatoryByteVector();

    @Test
    void testNull() throws OverflowException {
        nullableByteVectorDecoder.decode(fromHex("80"));
        assertTrue(nullableByteVectorDecoder.isReady());
        assertNull(nullableByteVectorDecoder.getValue());
    }

    @Test
    void testNullableZeroLen() throws OverflowException {
        nullableByteVectorDecoder.decode(fromHex("81"));
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testMandatoryZeroLen() throws OverflowException {
        mandatoryByteVectorDecoder.decode(fromHex("80"));
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testSimpleNullableVector() throws OverflowException {
        nullableByteVectorDecoder.decode(fromHex("87 41 42 42 43 44 45"));
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testSimpleMandatoryVector() throws OverflowException {
        mandatoryByteVectorDecoder.decode(fromHex("86 41 42 42 43 44 45"));
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testNullableSplitVector() throws OverflowException {
        nullableByteVectorDecoder.decode(fromHex("87 41 42 42"));
        assertFalse(nullableByteVectorDecoder.isReady());

        nullableByteVectorDecoder.continueDecode(fromHex("43 44 45"));
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testMandatorySplitVector() throws OverflowException {
        mandatoryByteVectorDecoder.decode(fromHex("86 41 42 42"));
        assertFalse(mandatoryByteVectorDecoder.isReady());

        mandatoryByteVectorDecoder.continueDecode(fromHex("43 44 45"));
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testNullableZeroLenGetValueTwice() throws OverflowException {
        nullableByteVectorDecoder.decode(fromHex("81"));
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testMandatoryZeroLenGetValueTwice() throws OverflowException {
        mandatoryByteVectorDecoder.decode(fromHex("80"));
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testSimpleNullableVectorTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("87 41 42 42 43 44 45 81");
        nullableByteVectorDecoder.decode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));

        nullableByteVectorDecoder.decode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void testSimpleMandatoryVectorTwoValuesInRow() throws OverflowException {
        ByteBuf buf = fromHex("86 41 42 42 43 44 45 80");
        mandatoryByteVectorDecoder.decode(buf);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));

        mandatoryByteVectorDecoder.decode(buf);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
    }
}
