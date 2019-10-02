package com.exactpro.epfast.decoder.ascii;

import io.netty.buffer.ByteBuf;

public class DecodeNullableAsciiString extends DecodeAsciiString {

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

    public String getValue() {
        if (value.length() == 0 && !overlong) {
            if (relativeLength == 1) {
                return null;
            } else {
                for (int i = 0; i < relativeLength - 2; i++) {
                    value.append("\0");
                }
                return value.toString();
            }
        } else {
            return overlong ? null : value.toString();
        }
    }

    private void accumulateValue(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = oneByte & CLEAR_STOP_BIT_MASK;
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
