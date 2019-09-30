package com.exactpro.epfast.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestInt32 {

    private DecodeNullableInt32 nullableInt32Decoder = new DecodeNullableInt32();

    private DecodeMandatoryInt32 mandatoryInt32Decoder = new DecodeMandatoryInt32();

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------null value--------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void test_null() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        Integer val = nullableInt32Decoder.getValue();
        assertNull(val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------zero values-------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optional_zero() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x81);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        Integer val = nullableInt32Decoder.getValue();
        assertEquals(0, (int) val);
    }

    @Test
    void mandatory_zero() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x80);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(0, val);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Test min/max values and overflows---------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void test_max_nullable() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x08);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(Integer.MAX_VALUE, val);
    }

    @Test
    void test_max_mandatory() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x07);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(Integer.MAX_VALUE, val);
    }

    @Test
    void test_min_nullable() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x78);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(Integer.MIN_VALUE, val);
    }

    @Test
    void test_min_mandatory() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x78);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(Integer.MIN_VALUE, val);
    }

    @Test
    void test_max_overflow_nullable_1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x08);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x81);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        nullableInt32Decoder.getValue();
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void test_max_overflow_nullable_2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x08);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        nullableInt32Decoder.getValue();
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void test_max_overflow_mandatory_1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x08);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void test_max_overflow_mandatory_2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x07);
        buf.writeByte(0x7f);
        buf.writeByte(0x00);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void test_min_overflow_nullable_1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x77);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        nullableInt32Decoder.getValue();
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void test_min_overflow_nullable_2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x78);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        nullableInt32Decoder.getValue();
        assertTrue(nullableInt32Decoder.isOverflow());
    }

    @Test
    void test_min_overflow_mandatory_1() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x77);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0x7f);
        buf.writeByte(0xff);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    @Test
    void test_min_overflow_mandatory_2() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x78);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x80);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        assertTrue(mandatoryInt32Decoder.isOverflow());
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------Simple numbers----------------------------------------------
    //-----------------------------------------------------------------------------------------------

    @Test
    void optional_positive() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa4);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optional_positive_split() {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        nextBuf.writeByte(0xa4);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatory_positive() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        buf.writeByte(0xa3);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void mandatory_positive_split() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x39);
        buf.writeByte(0x45);
        nextBuf.writeByte(0xa3);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(942755, val);
    }

    @Test
    void optional_negative() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x46);
        buf.writeByte(0x3a);
        buf.writeByte(0xdd);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void optional_negative_split() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x46);
        buf.writeByte(0x3a);
        nextBuf.writeByte(0xdd);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(-942755, val);
    }

    @Test
    void mandatory_negative() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x7c);
        buf.writeByte(0x1b);
        buf.writeByte(0x1b);
        buf.writeByte(0x9d);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void mandatory_negative_split() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x7c);
        buf.writeByte(0x1b);
        nextBuf.writeByte(0x1b);
        nextBuf.writeByte(0x9d);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-7942755, val);
    }

    @Test
    void optional_minus_one() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0xff);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void mandatory_minus_one() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0xff);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-1, val);
    }

    @Test
    void optional_sign_extension_positive() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x40);
        buf.writeByte(0x82);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void mandatory_sign_extension_positive() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x40);
        buf.writeByte(0x81);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(8193, val);
    }

    @Test
    void optional_sign_extension_negative() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x7f);
        buf.writeByte(0x3f);
        buf.writeByte(0xff);
        nullableInt32Decoder.decode(buf);
        while (!nullableInt32Decoder.isReady()) {
            nullableInt32Decoder.continueDecode(nextBuf);
        }
        int val = nullableInt32Decoder.getValue();
        assertEquals(-8193, val);
    }

    @Test
    void mandatory_sign_extension_negative() {
        ByteBuf buf = Unpooled.buffer();
        ByteBuf nextBuf = Unpooled.buffer();
        buf.writeByte(0x7f);
        buf.writeByte(0x3f);
        buf.writeByte(0xff);
        mandatoryInt32Decoder.decode(buf);
        while (!mandatoryInt32Decoder.isReady()) {
            mandatoryInt32Decoder.continueDecode(nextBuf);
        }
        int val = mandatoryInt32Decoder.getValue();
        assertEquals(-8193, val);
    }

}
