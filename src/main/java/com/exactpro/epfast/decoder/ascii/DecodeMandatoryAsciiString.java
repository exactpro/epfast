package com.exactpro.epfast.decoder.ascii;

public class DecodeMandatoryAsciiString extends DecodeNullableAsciiString {

    public String getValue() {
        if (value.length() == 0 && !overlong) {
            for (int i = 0; i < relativeLength - 1; i++) {
                value.append("\0");
            }
            return value.toString();
        } else {
            return overlong ? null : value.toString();
        }
    }
}

