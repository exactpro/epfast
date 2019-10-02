package com.exactpro.epfast.decoder.decimal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestDecodeDecimal {

    private DecodeNullableDecimal nullableDecimalDecoder = new DecodeNullableDecimal();

    private DecodeMandatoryDecimal mandatoryDecimalDecoder = new DecodeMandatoryDecimal();

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        nullableDecimalDecoder.decode(buf);
        while (!nullableDecimalDecoder.isReady()) {
            nullableDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = nullableDecimalDecoder.getValue();
        assertNull(val);
    }

    @Test
    void testNullablePositive1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x83);
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa3);
        nullableDecimalDecoder.decode(buf);
        while (!nullableDecimalDecoder.isReady()) {
            nullableDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = nullableDecimalDecoder.getValue();
        assertEquals(new BigDecimal("94275500"), val);
    }

    @Test
    void testNullablePositiveSplit() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x83);
        buf.writeByte(0x39);
        nextBuf.writeByte(0x45);
        nextBuf.writeByte(0xa3);
        nullableDecimalDecoder.decode(buf);
        while (!nullableDecimalDecoder.isReady()) {
            nullableDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = nullableDecimalDecoder.getValue();
        assertEquals(new BigDecimal("94275500"), val);
    }

    @Test
    void testNullablePositive() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x82);
        buf.writeByte(0x04);
        buf.writeByte(0x3f);
        buf.writeByte(0x34);
        buf.writeByte(0xde);
        nullableDecimalDecoder.decode(buf);
        while (!nullableDecimalDecoder.isReady()) {
            nullableDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = nullableDecimalDecoder.getValue();
        assertEquals(new BigDecimal("94275500"), val);
    }

    @Test
    void testNullablePositiveSplit2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x82);
        buf.writeByte(0x04);
        buf.writeByte(0x3f);
        buf.writeByte(0x34);
        nextBuf.writeByte(0xde);
        nullableDecimalDecoder.decode(buf);
        while (!nullableDecimalDecoder.isReady()) {
            nullableDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = nullableDecimalDecoder.getValue();
        assertEquals(new BigDecimal("94275500"), val);
    }

    @Test
    void testMandatoryPositive() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x82);
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa3);
        mandatoryDecimalDecoder.decode(buf);
        while (!mandatoryDecimalDecoder.isReady()) {
            mandatoryDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = mandatoryDecimalDecoder.getValue();
        assertEquals(new BigDecimal("94275500"), val);
    }

    @Test
    void testMandatoryPositiveSplit() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x82);
        buf.writeByte(0x39);
        nextBuf.writeByte(0x45);
        nextBuf.writeByte(0xa3);
        mandatoryDecimalDecoder.decode(buf);
        while (!mandatoryDecimalDecoder.isReady()) {
            mandatoryDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = mandatoryDecimalDecoder.getValue();
        assertEquals(new BigDecimal("94275500"), val);
    }

    @Test
    void testMandatoryPositive2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x81);
        buf.writeByte(0x04);
        buf.writeByte(0x3f);
        buf.writeByte(0x34);
        buf.writeByte(0xde);
        mandatoryDecimalDecoder.decode(buf);
        while (!mandatoryDecimalDecoder.isReady()) {
            mandatoryDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = mandatoryDecimalDecoder.getValue();
        assertEquals(new BigDecimal("94275500"), val);
    }

    @Test
    void testMandatoryPositiveSplit2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x81);
        buf.writeByte(0x04);
        buf.writeByte(0x3f);
        buf.writeByte(0x34);
        nextBuf.writeByte(0xde);
        mandatoryDecimalDecoder.decode(buf);
        while (!mandatoryDecimalDecoder.isReady()) {
            mandatoryDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = mandatoryDecimalDecoder.getValue();
        assertEquals(new BigDecimal("94275500"), val);
    }

    @Test
    void testMandatoryPositive3() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0xfe);
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa3);
        mandatoryDecimalDecoder.decode(buf);
        while (!mandatoryDecimalDecoder.isReady()) {
            mandatoryDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = mandatoryDecimalDecoder.getValue();
        assertEquals(new BigDecimal("9427.55"), val);
    }

    @Test
    void testNullablePositive3() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0xfe);
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa3);
        nullableDecimalDecoder.decode(buf);
        while (!nullableDecimalDecoder.isReady()) {
            nullableDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = nullableDecimalDecoder.getValue();
        assertEquals(new BigDecimal("9427.55"), val);
    }

    @Test
    void testNullableNegative() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0xfe);
        buf.writeByte(0x46);
        buf.writeByte(0x3a);
        buf.writeByte(0xdd);
        nullableDecimalDecoder.decode(buf);
        while (!nullableDecimalDecoder.isReady()) {
            nullableDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = nullableDecimalDecoder.getValue();
        assertEquals(new BigDecimal("-9427.55"), val);
    }

    @Test
    void testNullableNegativeSignExtension() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0xfd);
        buf.writeByte(0x7f);
        buf.writeByte(0x3f);
        buf.writeByte(0xff);
        nullableDecimalDecoder.decode(buf);
        while (!nullableDecimalDecoder.isReady()) {
            nullableDecimalDecoder.continueDecode(nextBuf);
        }
        BigDecimal val = nullableDecimalDecoder.getValue();
        assertEquals(new BigDecimal("-8.193"), val);
    }
}
