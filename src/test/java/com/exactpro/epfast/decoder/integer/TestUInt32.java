package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestUInt32 {

    private DecodeNullableUInt32 nullableUInt32Decoder = new DecodeNullableUInt32();

    private DecodeMandatoryUInt32 mandatoryUInt32Decoder = new DecodeMandatoryUInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        nullableUInt32Decoder.decode(buf);
        while (!nullableUInt32Decoder.isReady()) {
            nullableUInt32Decoder.continueDecode(nextBuf);
        }
        Long val = nullableUInt32Decoder.getValue();
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
        nullableUInt32Decoder.decode(buf);
        while (!nullableUInt32Decoder.isReady()) {
            nullableUInt32Decoder.continueDecode(nextBuf);
        }
        long val = nullableUInt32Decoder.getValue();
        assertEquals(0, val);
    }

    @Test
    void mandatoryZero() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        mandatoryUInt32Decoder.decode(buf);
        while (!mandatoryUInt32Decoder.isReady()) {
            mandatoryUInt32Decoder.continueDecode(nextBuf);
        }
        long val = mandatoryUInt32Decoder.getValue();
        assertEquals(0, val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test Min/Max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void testMaxNullable() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x10);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableUInt32Decoder.decode(buf);
        while (!nullableUInt32Decoder.isReady()) {
            nullableUInt32Decoder.continueDecode(nextBuf);
        }
        long val = nullableUInt32Decoder.getValue();
        assertEquals(4294967295L, val);
    }

    @Test
    void testMaxMandatory() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x0f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        mandatoryUInt32Decoder.decode(buf);
        while (!mandatoryUInt32Decoder.isReady()) {
            mandatoryUInt32Decoder.continueDecode(nextBuf);
        }
        long val = mandatoryUInt32Decoder.getValue();
        assertEquals(4294967295L, val);
    }

    @Test
    void testMaxOverflowNullable1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x10);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x81);
        nullableUInt32Decoder.decode(buf);
        while (!nullableUInt32Decoder.isReady()) {
            nullableUInt32Decoder.continueDecode(nextBuf);
        }
        assertTrue(nullableUInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowNullable2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x10);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableUInt32Decoder.decode(buf);
        while (!nullableUInt32Decoder.isReady()) {
            nullableUInt32Decoder.continueDecode(nextBuf);
        }
        assertTrue(nullableUInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x10);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryUInt32Decoder.decode(buf);
        while (!mandatoryUInt32Decoder.isReady()) {
            mandatoryUInt32Decoder.continueDecode(nextBuf);
        }
        assertTrue(mandatoryUInt32Decoder.isOverflow());
    }

    @Test
    void testMaxOverflowMandatory2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x0f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x00);
        buf.writeByte(0xff);
        mandatoryUInt32Decoder.decode(buf);
        while (!mandatoryUInt32Decoder.isReady()) {
            mandatoryUInt32Decoder.continueDecode(nextBuf);
        }
        assertTrue(mandatoryUInt32Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optionalSimpleNumber() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa4);
        nullableUInt32Decoder.decode(buf);
        while (!nullableUInt32Decoder.isReady()) {
            nullableUInt32Decoder.continueDecode(nextBuf);
        }
        long val = nullableUInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optionalSimpleNumber2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x0f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        nullableUInt32Decoder.decode(buf);
        while (!nullableUInt32Decoder.isReady()) {
            nullableUInt32Decoder.continueDecode(nextBuf);
        }
        long val = nullableUInt32Decoder.getValue();
        assertEquals(4294967294L, val);
    }

    @Test
    void mandatorySimpleNumber() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa3);
        mandatoryUInt32Decoder.decode(buf);
        while (!mandatoryUInt32Decoder.isReady()) {
            mandatoryUInt32Decoder.continueDecode(nextBuf);
        }
        long val = mandatoryUInt32Decoder.getValue();
        assertEquals(942755, val);
    }
}
