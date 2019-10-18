package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

public abstract class DecodeAsciiString implements IDecodeContext {

    StringBuilder value;

    private boolean ready;

    boolean zeroPreamble;

    boolean checkOverlong;

    int zeroCount;

    public void decode(ByteBuf buf) {
        reset();
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
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
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        while ((readerIndex < readLimit) && !ready) {
            accumulateValue(buf.getByte(readerIndex++));
        }
        buf.readerIndex(readerIndex);
    }

    public abstract String getValue() throws OverflowException;

    public boolean isReady() {
        return ready;
    }

    void setCheckOverlong() {
        checkOverlong = true;
    }

    void clearCheckOverlong() {
        checkOverlong = false;
    }

    private void accumulateValue(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (oneByte == 0) {
            zeroCount++;
        }
        value.append((char) oneByte);
    }

    public final void reset() {
        value = new StringBuilder();
        ready = false;
        zeroCount = 0;
        zeroPreamble = false;
    }
}
