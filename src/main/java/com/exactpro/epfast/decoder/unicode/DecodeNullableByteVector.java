package com.exactpro.epfast.decoder.unicode;

import io.netty.buffer.ByteBuf;

public class DecodeNullableByteVector extends DecodeByteVector {

    public void decode(ByteBuf buf) {
        ready = readFirstByte(buf.readByte());
        while (buf.isReadable() && !ready) {
            if (counter < messageLength) {
                val.append((char) buf.readByte());
                counter++;
            }
            if (counter == messageLength) {
                ready = true;
            }
        }
    }

    public void continueDecode(ByteBuf buf) {
        while (buf.isReadable() && !ready) {
            if (counter < messageLength) {
                val.append((char) buf.readByte());
                counter++;
            }
            if (counter == messageLength) {
                ready = true;
            }
        }
    }

    public String getValue() {
        return messageLength == 0 ? null : val.toString();
    }

    boolean readFirstByte(int firstByte) {
        messageLength = firstByte & CLEAR_STOP_BIT_MASK;
        if (messageLength == 0 || messageLength == 1) {
            return true;
        } else {
            messageLength--;
        }
        return false;
    }
}
