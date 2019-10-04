package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeAsciiString implements IDecodeContext {

    StringBuilder value;

    private int readLimit;

    private int readerIndex;

    private boolean zeroPreamble;

    int relativeLength;

    boolean overlong;

    boolean checkOverlong;

    protected boolean ready;

    public void decode(ByteBuf buf) {
        ready = false;
        value = new StringBuilder();
        readerIndex = buf.readerIndex();
        readLimit = buf.readableBytes() + readerIndex;
        if (buf.getByte(readerIndex) == 0) {
            zeroPreamble = true;
        }
        accumulateValue(buf.getByte(readerIndex++));
        while ((readerIndex < readLimit) && !ready) {
            accumulateValue(buf.getByte(readerIndex++));
        }
        buf.readerIndex(readerIndex);
    }

    public void continueDecode(ByteBuf buf) {
        readLimit = buf.readableBytes();
        readerIndex = buf.readerIndex();
        while ((readerIndex < readLimit) && !ready) {
            accumulateValue(buf.getByte(readerIndex++));
        }
        buf.readerIndex(readerIndex);
    }

    public abstract String getValue();

    public boolean isReady() {
        return ready;
    }

    public boolean isOverlong() {
        return overlong;
    }

    public void setCheckOverlong() {
        checkOverlong = true;
    }

    public void clearCheckOverlong() {
        checkOverlong = false;
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
