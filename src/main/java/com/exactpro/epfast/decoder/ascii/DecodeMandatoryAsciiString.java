package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.OverflowException;

public final class DecodeMandatoryAsciiString extends DecodeAsciiString {

    DecodeMandatoryAsciiString() {
        this(false);
    }

    DecodeMandatoryAsciiString(boolean checkOverlong) {
        super(checkOverlong);
    }

    public String getValue() throws OverflowException {
        if (stringBuilder.length() >= MAX_ALLOWED_LENGTH) {
            throw new OverflowException("String is longer than allowed");
        }
        if (zeroCount < stringBuilder.length()) {
            if (zeroPreamble && checkOverlong) {
                throw new OverflowException("String with zero preamble can't contain any value except 0");
            } else {
                return stringBuilder.toString();
            }
        } else if (zeroCount == 1) {
            return "";
        } else {
            stringBuilder.setLength(zeroCount - 1);
            return stringBuilder.toString();
        }
    }
}

