package com.exactpro.epfast.decoder.ascii;

public class DecodeNullableAsciiString extends DecodeAsciiString {

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
}
