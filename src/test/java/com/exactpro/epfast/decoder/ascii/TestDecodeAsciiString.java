package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDecodeAsciiString {

    private DecodeNullableAsciiString nullableStringDecoder = new DecodeNullableAsciiString();

    private DecodeMandatoryAsciiString mandatoryStringDecoder = new DecodeMandatoryAsciiString();

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x80);
        nullableStringDecoder.decode(buf);
        try {
            assertNull(nullableStringDecoder.getValue());
        } catch (OverflowException ex) {
            fail();
        }

    }

    @Test
    void testOptionalEmptyString() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableStringDecoder.decode(buf);
        String val = null;
        try {
            val = nullableStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("", val);
    }

    @Test
    void testMandatoryEmptyString() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x80);
        mandatoryStringDecoder.decode(buf);
        String val = null;
        try {
            val = mandatoryStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("", val);
    }

    @Test
    void testSimpleString() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x41);
        buf.writeByte(0x42);
        buf.writeByte(0xc3);
        nullableStringDecoder.decode(buf);
        String val = null;
        try {
            val = nullableStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("ABC", val);
    }

    @Test
    void testZeroByteStringNullable1() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableStringDecoder.decode(buf);
        String val = null;
        try {
            val = nullableStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("\0", val);
    }

    @Test
    void testZeroByteStringNullable2() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableStringDecoder.decode(buf);
        String val = null;
        try {
            val = nullableStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("\0\0\0", val);
    }

    @Test
    void testZeroByteStringMandatory1() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryStringDecoder.decode(buf);
        String val = null;
        try {
            val = mandatoryStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("\0\0", val);
    }

    @Test
    void testZeroByteStringMandatory2() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryStringDecoder.decode(buf);
        String val = null;
        try {
            val = mandatoryStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("\0\0\0\0", val);
    }

    @Test
    void testOverlong1() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x81);
        mandatoryStringDecoder.decode(buf);
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
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x81);
        nullableStringDecoder.decode(buf);
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
        String val = null;
        try {
            val = nullableStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("ABC", val);
        nullableStringDecoder.decode(buf);
        try {
            val = nullableStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("BBC", val);
        nullableStringDecoder.decode(buf);
        try {
            val = nullableStringDecoder.getValue();
        } catch (OverflowException ex) {
            fail();
        }
        assertEquals("ADC", val);
    }
}
