package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeAsciiString implements IDecodeContext {

    StringBuilder value = new StringBuilder();

    int relativeLength;

    private boolean zeroPreamble;

    boolean overlong;

    protected boolean ready;

    public void decode(ByteBuf buf) {
        int oneByte;
        oneByte = buf.readByte();
        if (oneByte == 0) {
            zeroPreamble = true;
        }
        accumulateValue(oneByte);
        while (buf.isReadable() && !ready) {
            accumulateValue(buf.readByte());
        }
    }

    public void continueDecode(ByteBuf buf) {
        while (buf.isReadable() && !ready) {
            accumulateValue(buf.readByte());
        }
    }

    public abstract String getValue();

    public boolean isReady() {
        return ready;
    }

    public boolean isOverlong() {
        return overlong;
    }

    private void accumulateValue(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (zeroPreamble && (oneByte != 0)) {
            overlong = true;
        } else if (oneByte != 0) {
            value.append((char) oneByte);
        }
        relativeLength++;
    }
}
