package com.exactpro.epfast.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class TypeConverter {

    // Decimal conversions

    public int decimalToInt(BigDecimal decimalValue) {
        try {
            return decimalValue.intValueExact();
        } catch (ArithmeticException ex) {
            throw cantConvertException(decimalValue.toPlainString(), int.class, ex);
        }
    }

    public long decimalToLong(BigDecimal decimalValue) {
        try {
            return decimalValue.longValueExact();
        } catch (ArithmeticException ex) {
            throw cantConvertException(decimalValue.toPlainString(), long.class, ex);
        }
    }

    public BigInteger decimalToBigInteger(BigDecimal decimalValue) {
        try {
            return decimalValue.toBigIntegerExact();
        } catch (ArithmeticException ex) {
            throw cantConvertException(decimalValue.toPlainString(), BigInteger.class, ex);
        }
    }

    public String decimalToString(BigDecimal decimalValue) {
        return decimalValue.toPlainString();
    }

    // int32 conversions

    public long int32ToLong(int int32Value) {
        return int32Value;
    }

    public BigDecimal int32ToBigDecimal(int int32Value) {
        return BigDecimal.valueOf(int32Value);
    }

    public BigInteger int32ToBigInteger(int int32Value) {
        return BigInteger.valueOf(int32Value);
    }

    public String int32ToString(int int32Value) {
        return String.valueOf(int32Value);
    }

    // uint32 conversions

    public int uint32ToInt(int uint32Value) {
        if (uint32Value < 0) {
            throw overflowException(Integer.toUnsignedString(uint32Value), int.class);
        }
        return uint32Value;
    }

    public long uint32ToLong(int uint32Value) {
        return Integer.toUnsignedLong(uint32Value);
    }

    public BigDecimal uint32ToBigDecimal(int uint32Value) {
        return BigDecimal.valueOf(Integer.toUnsignedLong(uint32Value));
    }

    public BigInteger uint32ToBigInteger(int uint32Value) {
        return BigInteger.valueOf(Integer.toUnsignedLong(uint32Value));
    }

    public String uint32ToString(int uint32Value) {
        return Integer.toUnsignedString(uint32Value);
    }

    // int64 conversions

    public int int64ToInt(long int64Value) {
        int int32Value = (int) int64Value;
        if (int32Value != int64Value) {
            throw overflowException(Long.toString(int64Value), int.class);
        }
        return int32Value;
    }

    public BigDecimal int64ToBigDecimal(long int64Value) {
        return BigDecimal.valueOf(int64Value);
    }

    public BigInteger int64ToBigInteger(long int64Value) {
        return BigInteger.valueOf(int64Value);
    }

    public String int64ToString(long int64Value) {
        return Long.toString(int64Value);
    }

    // uint64 conversions

    private byte[] longBytes = new byte[Long.BYTES];

    public int uint64ToInt(long uint64Value) {
        if (uint64Value < 0) {
            throw overflowException(Long.toUnsignedString(uint64Value), int.class);
        }
        return int64ToInt(uint64Value);
    }

    public long uint64ToLong(long uint64Value) {
        if (uint64Value < 0) {
            throw overflowException(Long.toUnsignedString(uint64Value), long.class);
        }
        return uint64Value;
    }

    public BigDecimal uint64ToBigDecimal(long uint64Value) {
        if (uint64Value >= 0) {
            return BigDecimal.valueOf(uint64Value);
        }
        // TODO An integer can be converted to decimal if it can be represented as a scaled number with an exponent in
        //      the range [-63 … -63] and an int64 mantissa. Otherwise it is a reportable error [ERR R1].
        return new BigDecimal(uint64ToBigInteger(uint64Value));
    }

    public BigInteger uint64ToBigInteger(long uint64Value) {
        if (uint64Value >= 0) {
            return BigInteger.valueOf(uint64Value);
        }
        for (int index = Long.BYTES; index > 0; ) {
            longBytes[--index] = (byte) uint64Value;
            uint64Value >>>= 8;
        }
        return new BigInteger(1, longBytes);
    }

    public String uint64ToString(long uint64Value) {
        return Long.toUnsignedString(uint64Value);
    }

    // String conversions

    public int stringToInt(String stringValue) {
        stringValue = stringValue.trim();
        try {
            return Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            throw cantParseException(stringValue, int.class, e);
        }
    }

    public long stringToLong(String stringValue) {
        stringValue = stringValue.trim();
        try {
            return Long.parseLong(stringValue);
        } catch (NumberFormatException e) {
            throw cantParseException(stringValue, long.class, e);
        }

    }

    public BigDecimal stringToBigDecimal(String stringValue) {
        stringValue = stringValue.trim();
        // TODO It is a reportable error [ERR R1] if the conversion would result in an exponent less than -63
        //      or greater than 63 or if the mantissa does not fit in the range of an int64.
        try {
            return new BigDecimal(stringValue);
        } catch (NumberFormatException e) {
            throw cantParseException(stringValue, BigDecimal.class, e);
        }
    }

    public BigInteger stringToBigInteger(String stringValue) {
        stringValue = stringValue.trim();
        // XXX Is it needed to perform any size checks? In theory it shouldn't be larger than (u)int64.
        try {
            return new BigInteger(stringValue);
        } catch (NumberFormatException e) {
            throw cantParseException(stringValue, BigInteger.class, e);
        }
    }

    private static class StringToVector {
        private String string;

        private int stringIndex;

        public byte[] convert(String string) {
            int numDigits = getNumNonWhiteSpaceCharacters(string);
            if (numDigits % 2 != 0) {
                throw cantParseException(string, byte[].class);
            }
            this.string = string;
            this.stringIndex = 0;

            int vectorLength = numDigits / 2;
            byte[] vector = new byte[vectorLength];
            int vectorIndex = 0;
            while (vectorIndex < vectorLength) {
                vector[vectorIndex++] = (byte) ((nextHexDigit() << 4) | nextHexDigit());
            }
            return vector;
        }

        private int nextHexDigit() {
            char character;
            do {
                character = string.charAt(stringIndex++);
            } while (Character.isWhitespace(character));
            int digit = Character.digit(character, 16);
            if (digit < 0) {
                throw cantParseException(string, byte[].class);
            }
            return digit;
        }

        private static int getNumNonWhiteSpaceCharacters(String string) {
            int numDigits = 0;
            int stringLength = string.length();
            for (int index = 0; index < stringLength; ++index) {
                if (!Character.isWhitespace(string.charAt(index))) {
                    ++numDigits;
                }
            }
            return numDigits;
        }
    }

    private StringToVector stringToVector = new StringToVector();

    public byte[] stringToByteVector(String stringValue) {
        return stringToVector.convert(stringValue);
    }

    // ByteVector conversions

    public String byteVectorToString(byte[] byteVector) {
        char[] hexChars = new char[byteVector.length * 2];
        for (int j = 0; j < byteVector.length; j++) {
            int v = byteVector[j] & 0xFF;
            hexChars[j * 2] = Character.forDigit(v >>> 4, 16);
            hexChars[j * 2 + 1] = Character.forDigit(v & 0x0F, 16);
        }
        return new String(hexChars);
    }

    private static ArithmeticException overflowException(String value, Class clazz) {
        return arithmeticException("%1$s overflows %2$s", value, clazz);
    }

    private static ArithmeticException cantConvertException(String value, Class clazz, Throwable cause) {
        return withCause(arithmeticException("%1$s can not be exactly represented in %2$s", value, clazz), cause);
    }

    private static ArithmeticException arithmeticException(String format, String value, Class clazz) {
        return new ArithmeticException(String.format(format, value, clazz.getSimpleName()));
    }

    private static NumberFormatException cantParseException(String value, Class clazz) {
        return numberFormatException("%1$s can not be converted to %2$s", value, clazz);
    }

    private static NumberFormatException cantParseException(String value, Class clazz, Throwable cause) {
        return withCause(cantParseException(value, clazz), cause);
    }

    private static NumberFormatException numberFormatException(String format, String value, Class clazz) {
        return new NumberFormatException(String.format(format, value, clazz.getSimpleName()));
    }

    private static <T extends Throwable> T withCause(T ex, Throwable cause) {
        ex.initCause(cause);
        return ex;
    }
}
