package com.exactpro.epfast.decoder.unicode;

public class DecodeMandatoryByteVector extends DecodeNullableByteVector {

    boolean readFirstByte(int firstByte) {
        messageLength = firstByte & CLEAR_STOP_BIT_MASK;
        return messageLength == 0;
    }

    public String getValue() {
        return val.toString();
    }
}
