package com.exactpro.epfast.decoder.ascii;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestDecodeAsciiString {

    private DecodeNullableAsciiString nullableStringDecoder = new DecodeNullableAsciiString();

    private DecodeMandatoryAsciiString mandatoryStringDecoder = new DecodeMandatoryAsciiString();

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        nullableStringDecoder.decode(buf);
        while (!nullableStringDecoder.isReady()) {
            nullableStringDecoder.continueDecode(nextBuf);
        }
        String val = nullableStringDecoder.getValue();
        assertNull(val);
    }

    @Test
    void testOptionalEmptyString() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableStringDecoder.decode(buf);
        while (!nullableStringDecoder.isReady()) {
            nullableStringDecoder.continueDecode(nextBuf);
        }
        String val = nullableStringDecoder.getValue();
        assertEquals("", val);
    }

    @Test
    void testMandatoryEmptyString() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        mandatoryStringDecoder.decode(buf);
        while (!mandatoryStringDecoder.isReady()) {
            mandatoryStringDecoder.continueDecode(nextBuf);
        }
        String val = mandatoryStringDecoder.getValue();
        assertEquals("", val);
    }

    @Test
    void testSimpleString() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x41);
        buf.writeByte(0x42);
        buf.writeByte(0xc3);
        nullableStringDecoder.decode(buf);
        while (!nullableStringDecoder.isReady()) {
            nullableStringDecoder.continueDecode(nextBuf);
        }
        String val = nullableStringDecoder.getValue();
        assertEquals("ABC", val);
    }

    @Test
    void testZeroByteStringNullable1() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableStringDecoder.decode(buf);
        while (!nullableStringDecoder.isReady()) {
            nullableStringDecoder.continueDecode(nextBuf);
        }
        String val = nullableStringDecoder.getValue();
        assertEquals("\0", val);
    }

    @Test
    void testZeroByteStringNullable2() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableStringDecoder.decode(buf);
        while (!nullableStringDecoder.isReady()) {
            nullableStringDecoder.continueDecode(nextBuf);
        }
        String val = nullableStringDecoder.getValue();
        assertEquals("\0\0\0", val);
    }

    @Test
    void testZeroByteStringMandatory1() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryStringDecoder.decode(buf);
        while (!mandatoryStringDecoder.isReady()) {
            mandatoryStringDecoder.continueDecode(nextBuf);
        }
        String val = mandatoryStringDecoder.getValue();
        assertEquals("\0\0", val);
    }

    @Test
    void testZeroByteStringMandatory2() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryStringDecoder.decode(buf);
        while (!mandatoryStringDecoder.isReady()) {
            mandatoryStringDecoder.continueDecode(nextBuf);
        }
        String val = mandatoryStringDecoder.getValue();
        assertEquals("\0\0\0\0", val);
    }

    @Test
    void testOverlong1() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x81);
        mandatoryStringDecoder.decode(buf);
        while (!mandatoryStringDecoder.isReady()) {
            mandatoryStringDecoder.continueDecode(nextBuf);
        }
        String val = mandatoryStringDecoder.getValue();
        assertTrue(mandatoryStringDecoder.isOverlong());
        assertNull(val);
    }

    @Test
    void testOverlong2() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x81);
        nullableStringDecoder.decode(buf);
        while (!nullableStringDecoder.isReady()) {
            nullableStringDecoder.continueDecode(nextBuf);
        }
        String val = nullableStringDecoder.getValue();
        assertTrue(nullableStringDecoder.isOverlong());
        assertNull(val);
    }

    @Test
    void testNullableReuse(){
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x41);
        buf.writeByte(0x42);
        buf.writeByte(0xc3);
        buf.writeByte(0x42);
        buf.writeByte(0x42);
        buf.writeByte(0xc3);
        buf.writeByte(0x41);
        buf.writeByte(0x44);
        buf.writeByte(0xc3);
        nullableStringDecoder.decode(buf);
        String val = nullableStringDecoder.getValue();
        assertEquals("ABC", val);
        nullableStringDecoder.decode(buf);
        val = nullableStringDecoder.getValue();
        assertEquals("BBC", val);
        nullableStringDecoder.decode(buf);
        val = nullableStringDecoder.getValue();
        assertEquals("ADC", val);
    }
}
