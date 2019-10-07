package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.OverflowException;

public class DecodeNullableAsciiString extends DecodeAsciiString {

    public String getValue() throws OverflowException {
        if (zeroCount < value.length()) {
            if (zeroPreamble && checkOverlong) {
                throw new OverflowException("String with zero preamble can't contain any value except 0");
            } else {
                return value.toString();
            }
        } else if (zeroCount == 1) {
            return null;
        } else if (zeroCount == 2) {
            return "";
        } else {
            value = new StringBuilder();
            for (int i = 0; i < zeroCount - 2; i++) {
                value.append("\0");
            }
            return value.toString();
        }
    }
}
