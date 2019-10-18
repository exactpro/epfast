package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.FillBuffer;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class TestDecodeByteVector {

    private DecodeNullableByteVector nullableByteVectorDecoder = new DecodeNullableByteVector();

    private DecodeMandatoryByteVector mandatoryByteVectorDecoder = new DecodeMandatoryByteVector();

    @Test
    void testNull() {
        ByteBuf buf = FillBuffer.fromHex("80");
        nullableByteVectorDecoder.decode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        try {
            assertNull(nullableByteVectorDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullableZeroLen() {
        ByteBuf buf = FillBuffer.fromHex("81");
        nullableByteVectorDecoder.decode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        try {
            assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryZeroLen() {
        ByteBuf buf = FillBuffer.fromHex("80");
        mandatoryByteVectorDecoder.decode(buf);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        try {
            assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testSimpleNullableVector() {
        ByteBuf buf = FillBuffer.fromHex("87 41 42 42 43 44 45");
        nullableByteVectorDecoder.decode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        try {
            assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testSimpleMandatoryVector() {
        ByteBuf buf = FillBuffer.fromHex("86 41 42 42 43 44 45");
        mandatoryByteVectorDecoder.decode(buf);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        try {
            assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullableSplitVector() {
        ByteBuf buf = FillBuffer.fromHex("87 41 42 42");
        nullableByteVectorDecoder.decode(buf);
        assertFalse(nullableByteVectorDecoder.isReady());

        buf = FillBuffer.fromHex("43 44 45");
        nullableByteVectorDecoder.continueDecode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        try {
            assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatorySplitVector() {
        ByteBuf buf = FillBuffer.fromHex("86 41 42 42");
        mandatoryByteVectorDecoder.decode(buf);
        assertFalse(mandatoryByteVectorDecoder.isReady());

        buf = FillBuffer.fromHex("43 44 45");
        mandatoryByteVectorDecoder.continueDecode(buf);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        try {
            assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testNullableZeroLenGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("81");
        nullableByteVectorDecoder.decode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        try {
            assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testMandatoryZeroLenGetValueTwice() {
        ByteBuf buf = FillBuffer.fromHex("80");
        mandatoryByteVectorDecoder.decode(buf);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        try {
            assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
        try {
            assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testSimpleNullableVectorTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("87 41 42 42 43 44 45 81");
        nullableByteVectorDecoder.decode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        try {
            assertEquals("ABBCDE", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
        nullableByteVectorDecoder.decode(buf);
        assertTrue(nullableByteVectorDecoder.isReady());
        try {
            assertEquals("", new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }

    @Test
    void testSimpleMandatoryVectorTwoValuesInRow() {
        ByteBuf buf = FillBuffer.fromHex("86 41 42 42 43 44 45 80");
        mandatoryByteVectorDecoder.decode(buf);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        try {
            assertEquals("ABBCDE", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
        mandatoryByteVectorDecoder.decode(buf);
        assertTrue(mandatoryByteVectorDecoder.isReady());
        try {
            assertEquals("", new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8));
        } catch (OverflowException ex) {
            fail();
        }
    }
}
