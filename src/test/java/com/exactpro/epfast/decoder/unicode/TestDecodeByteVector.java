package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class TestDecodeByteVector {

    private DecodeNullableByteVector nullableByteVectorDecoder = new DecodeNullableByteVector();

    private DecodeMandatoryByteVector mandatoryByteVectorDecoder = new DecodeMandatoryByteVector();

    @Test
    void testNull() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x80);
        nullableByteVectorDecoder.decode(buf);
        System.out.println();
        try {
            assertNull(nullableByteVectorDecoder.getValue());
        }catch (OverflowException ex){
            System.out.println(ex.getMessage());
        }

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
        String val = "";
        try {
            val = new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8);
        }catch (OverflowException ex){
            System.out.println(ex.getMessage());
        }
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
        String val = "";
        try {
            val = new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8);
        }catch (OverflowException ex){
            System.out.println(ex.getMessage());
        }
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
        String val = "";
        try {
            val = new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8);
        }catch (OverflowException ex){
            System.out.println(ex.getMessage());
        }
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
        String val = "";
        try {
            val = new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8);
        }catch (OverflowException ex){
            System.out.println(ex.getMessage());
        };
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
        String val = "";
        try {
            val = new String(nullableByteVectorDecoder.getValue(), StandardCharsets.UTF_8);
        }catch (OverflowException ex){
            System.out.println(ex.getMessage());
        }
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
        String val = "";
        try {
            val = new String(mandatoryByteVectorDecoder.getValue(), StandardCharsets.UTF_8);
        }catch (OverflowException ex){
            System.out.println(ex.getMessage());
        }
        assertEquals("ABBCDE", val);
    }

    @Test
    void testOverflow() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x10);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x00);
        buf.writeByte(0x81);
        buf.writeByte(0x41);
        buf.writeByte(0x42);
        buf.writeByte(0x42);
        nullableByteVectorDecoder.decode(buf);
        assertThrows(OverflowException.class, () -> nullableByteVectorDecoder.getValue());
    }
}
