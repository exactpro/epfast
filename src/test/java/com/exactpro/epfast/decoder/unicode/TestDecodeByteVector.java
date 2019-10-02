package com.exactpro.epfast.decoder.unicode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestDecodeByteVector {

    private DecodeNullableByteVector nullableByteVectorDecoder = new DecodeNullableByteVector();

    private DecodeMandatoryByteVector mandatoryByteVectorDecoder = new DecodeMandatoryByteVector();

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        nullableByteVectorDecoder.decode(buf);
        while (!nullableByteVectorDecoder.isReady()) {
            nullableByteVectorDecoder.continueDecode(nextBuf);
        }
        String val = nullableByteVectorDecoder.getValue();
        assertNull(val);
    }

    @Test
    void testNullableZeroLen() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x81);
        nullableByteVectorDecoder.decode(buf);
        while (!nullableByteVectorDecoder.isReady()) {
            nullableByteVectorDecoder.continueDecode(nextBuf);
        }
        String val = nullableByteVectorDecoder.getValue();
        assertEquals("", val);
    }

    @Test
    void testMandatoryZeroLen() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        mandatoryByteVectorDecoder.decode(buf);
        while (!mandatoryByteVectorDecoder.isReady()) {
            mandatoryByteVectorDecoder.continueDecode(nextBuf);
        }
        String val = mandatoryByteVectorDecoder.getValue();
        assertEquals("", val);
    }

    @Test
    void testSimpleNullableVector() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x87);
        buf.writeByte(0x41);
        buf.writeByte(0x42);
        buf.writeByte(0x42);
        buf.writeByte(0x43);
        buf.writeByte(0x44);
        buf.writeByte(0x45);
        nullableByteVectorDecoder.decode(buf);
        while (!nullableByteVectorDecoder.isReady()) {
            nullableByteVectorDecoder.continueDecode(nextBuf);
        }
        String val = nullableByteVectorDecoder.getValue();
        assertEquals("ABBCDE", val);
    }

    @Test
    void testSimpleMandatoryVector() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x86);
        buf.writeByte(0x41);
        buf.writeByte(0x42);
        buf.writeByte(0x42);
        buf.writeByte(0x43);
        buf.writeByte(0x44);
        buf.writeByte(0x45);
        mandatoryByteVectorDecoder.decode(buf);
        while (!mandatoryByteVectorDecoder.isReady()) {
            mandatoryByteVectorDecoder.continueDecode(nextBuf);
        }
        String val = mandatoryByteVectorDecoder.getValue();
        assertEquals("ABBCDE", val);
    }

    @Test
    void testNullableSplitVector() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x87);
        buf.writeByte(0x41);
        buf.writeByte(0x42);
        buf.writeByte(0x42);
        nextBuf.writeByte(0x43);
        nextBuf.writeByte(0x44);
        nextBuf.writeByte(0x45);
        nullableByteVectorDecoder.decode(buf);
        while (!nullableByteVectorDecoder.isReady()) {
            nullableByteVectorDecoder.continueDecode(nextBuf);
        }
        String val = nullableByteVectorDecoder.getValue();
        assertEquals("ABBCDE", val);
    }

    @Test
    void testMandatorySplitVector() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x86);
        buf.writeByte(0x41);
        buf.writeByte(0x42);
        buf.writeByte(0x42);
        nextBuf.writeByte(0x43);
        nextBuf.writeByte(0x44);
        nextBuf.writeByte(0x45);
        mandatoryByteVectorDecoder.decode(buf);
        while (!mandatoryByteVectorDecoder.isReady()) {
            mandatoryByteVectorDecoder.continueDecode(nextBuf);
        }
        String val = mandatoryByteVectorDecoder.getValue();
        assertEquals("ABBCDE", val);
    }
}
