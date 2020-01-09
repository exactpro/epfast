package com.exactpro.epfast.decoder.presencemap;

import com.exactpro.junit5.WithByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.Collection;

import static com.exactpro.epfast.DecoderUtils.decode;
import static org.junit.jupiter.api.Assertions.*;

class TestDecodePresenceMap {

    private DecodePresenceMap presenceMapDecoder = new DecodePresenceMap();

    @WithByteBuf("95") //0b10010101
    void testSingleByte(Collection<ByteBuf> buffers) {
        decode(presenceMapDecoder, buffers);
        PresenceMap presenceMap = presenceMapDecoder.getValue();
        assertTrue(presenceMap.getValue(0));
        assertFalse(presenceMap.getValue(1));
        assertTrue(presenceMap.getValue(2));
        assertFalse(presenceMap.getValue(3));
        assertTrue(presenceMap.getValue(4));
        assertFalse(presenceMap.getValue(5));
        assertFalse(presenceMap.getValue(6));
    }

    @WithByteBuf("15 15 00 00 00 80")
    void testOverlong(Collection<ByteBuf> buffers) {
        decode(presenceMapDecoder, buffers);
        assertTrue(presenceMapDecoder.isReady());
        PresenceMap presenceMap = presenceMapDecoder.getValue();
        assertTrue(presenceMap.getValue(0));
        assertFalse(presenceMap.getValue(1));
        assertTrue(presenceMap.getValue(2));
        assertFalse(presenceMap.getValue(3));
        assertTrue(presenceMap.getValue(4));
        assertFalse(presenceMap.getValue(5));
        assertFalse(presenceMap.getValue(6));
        assertTrue(presenceMapDecoder.isOverlong());
    }

    @WithByteBuf("15 15 00 00 00 82")
    void testTruncateWhenNotOverlong(Collection<ByteBuf> buffers) {
        decode(presenceMapDecoder, buffers);
        assertTrue(presenceMapDecoder.isReady());
        PresenceMap presenceMap = presenceMapDecoder.getValue();
        assertTrue(presenceMap.getValue(0));
        assertFalse(presenceMap.getValue(1));
        assertTrue(presenceMap.getValue(2));
        assertFalse(presenceMap.getValue(3));
        assertTrue(presenceMap.getValue(4));
        assertFalse(presenceMap.getValue(5));
        assertFalse(presenceMap.getValue(6));
        assertFalse(presenceMapDecoder.isOverlong());
    }
}
