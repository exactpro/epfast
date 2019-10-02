package com.exactpro.epfast.decoder.decimal;

import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public class DecodeMandatoryDecimal extends DecodeDecimal {

    public void decode(ByteBuf buf) {
        exponent = readExponent(buf.readByte());
        int firstMantissaByte = buf.readByte();
        positive = (firstMantissaByte & SIGN_BIT_MASK) != 0;
        if (positive) {
            mantissa = -1;
            accumulateNegative(firstMantissaByte);
            while (buf.isReadable() && !ready) {
                accumulateNegative(buf.readByte());
            }
        } else {
            accumulatePositive(firstMantissaByte);
            while (buf.isReadable() && !ready) {
                accumulatePositive(buf.readByte());
            }
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
        return new BigDecimal(mantissa).movePointRight(exponent);
    }

    private int readExponent(int exponentByte) {
        return (exponentByte & SIGN_BIT_MASK) == 0 ? (exponentByte & CLEAR_STOP_BIT_MASK) : exponentByte;
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
