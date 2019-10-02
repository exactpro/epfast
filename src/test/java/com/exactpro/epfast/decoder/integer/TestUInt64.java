package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestUInt64 {

    private DecodeNullableUInt64 nullableUInt64Decoder = new DecodeNullableUInt64();

    private DecodeMandatoryUInt64 mandatoryUInt64Decoder = new DecodeMandatoryUInt64();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        nullableUInt64Decoder.decode(buf);
        while (!nullableUInt64Decoder.isReady()) {
            nullableUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = nullableUInt64Decoder.getValue();
        assertNull(val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalZero() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x81);
        nullableUInt64Decoder.decode(buf);
        while (!nullableUInt64Decoder.isReady()) {
            nullableUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("0"), val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        mandatoryUInt64Decoder.decode(buf);
        while (!mandatoryUInt64Decoder.isReady()) {
            mandatoryUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("0"), val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x02);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableUInt64Decoder.decode(buf);
        while (!nullableUInt64Decoder.isReady()) {
            nullableUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551615"), val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x01);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        mandatoryUInt64Decoder.decode(buf);
        while (!mandatoryUInt64Decoder.isReady()) {
            mandatoryUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551615"), val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x02);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x81);
        nullableUInt64Decoder.decode(buf);
        while (!nullableUInt64Decoder.isReady()) {
            nullableUInt64Decoder.continueDecode(nextBuf);
        }
        assertTrue(nullableUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x02);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableUInt64Decoder.decode(buf);
        while (!nullableUInt64Decoder.isReady()) {
            nullableUInt64Decoder.continueDecode(nextBuf);
        }
        assertTrue(nullableUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x02);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryUInt64Decoder.decode(buf);
        while (!mandatoryUInt64Decoder.isReady()) {
            mandatoryUInt64Decoder.continueDecode(nextBuf);
        }
        assertTrue(mandatoryUInt64Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x01);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x00);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        mandatoryUInt64Decoder.decode(buf);
        while (!mandatoryUInt64Decoder.isReady()) {
            mandatoryUInt64Decoder.continueDecode(nextBuf);
        }
        assertTrue(mandatoryUInt64Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa4);
        nullableUInt64Decoder.decode(buf);
        while (!nullableUInt64Decoder.isReady()) {
            nullableUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
    }

    @Test
    void optionalSimpleNumber2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x01);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        nullableUInt64Decoder.decode(buf);
        while (!nullableUInt64Decoder.isReady()) {
            nullableUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = nullableUInt64Decoder.getValue();
        assertEquals(new BigInteger("18446744073709551614"), val);
    }

    @Test
    void mandatorySimpleNumber1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa3);
        mandatoryUInt64Decoder.decode(buf);
        while (!mandatoryUInt64Decoder.isReady()) {
            mandatoryUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("942755"), val);
    }

    @Test
    void mandatorySimpleNumber2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x01);
        buf.writeByte(0x10);
        buf.writeByte(0x78);
        buf.writeByte(0x20);
        buf.writeByte(0x76);
        buf.writeByte(0x62);
        buf.writeByte(0x2a);
        buf.writeByte(0x62);
        buf.writeByte(0x51);
        buf.writeByte(0xcf);
        mandatoryUInt64Decoder.decode(buf);
        while (!mandatoryUInt64Decoder.isReady()) {
            mandatoryUInt64Decoder.continueDecode(nextBuf);
        }
        BigInteger val = mandatoryUInt64Decoder.getValue();
        assertEquals(new BigInteger("10443992354206034127"), val);
    }
}
