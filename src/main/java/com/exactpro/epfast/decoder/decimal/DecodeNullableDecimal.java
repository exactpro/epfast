package com.exactpro.epfast.decoder.decimal;

import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public class DecodeNullableDecimal extends DecodeDecimal {

    private boolean nullValue;

    public void decode(ByteBuf buf) {
        ready = readExponent(buf.readByte());
        if (!ready) {
            int firstMantissaByte = buf.readByte();
            positive = (firstMantissaByte & SIGN_BIT_MASK) == 0;
            if (positive) {
                accumulatePositive(firstMantissaByte);
                while (buf.isReadable() && !ready) {
                    accumulatePositive(buf.readByte());
                }
            } else {
                mantissa = -1;
                accumulateNegative(firstMantissaByte);
                while (buf.isReadable() && !ready) {
                    accumulateNegative(buf.readByte());
                }
            }
        } else {
            nullValue = true;
        }
    }

    public void continueDecode(ByteBuf buf) {
        if (positive) {
            while (buf.isReadable() && !ready) {
                accumulateNegative(buf.readByte());
            }
        } else {
            while (buf.isReadable() && !ready) {
                accumulatePositive(buf.readByte());
            }
        }
    }

    BigDecimal getValue() {
        if (nullValue) {
            return null;
        } else {
            return new BigDecimal(mantissa).movePointRight(exponent);
        }
    }

    private boolean readExponent(int exponentByte) {
        if ((exponentByte & SIGN_BIT_MASK) == 0) {
            if ((exponentByte & CLEAR_STOP_BIT_MASK) == 0) {
                return true;
            } else {
                exponent = (exponentByte & CLEAR_STOP_BIT_MASK) - 1;
            }
        } else {
            exponent = exponentByte;
        }
        return false;
    }

    private void accumulatePositive(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = (oneByte & CLEAR_STOP_BIT_MASK);
            ready = true;
        }
        if (mantissa <= POSITIVE_LIMIT) {
            mantissa = (mantissa << 7) + oneByte;
        } else {
            mantissa = 0;
            overflow = true;
        }
    }

    private void accumulateNegative(int oneByte) {
        if ((oneByte & CHECK_STOP_BIT_MASK) != 0) {
            oneByte = (oneByte & CLEAR_STOP_BIT_MASK);
            ready = true;
        }
        if (mantissa >= NEGATIVE_LIMIT) {
            mantissa = (mantissa << 7) + oneByte;
        } else {
            mantissa = 0;
            overflow = true;
        }
    }
}
