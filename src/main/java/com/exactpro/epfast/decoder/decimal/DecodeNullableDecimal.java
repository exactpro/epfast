package com.exactpro.epfast.decoder.decimal;

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.integer.DecodeNullableInt32;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public class DecodeNullableDecimal extends DecodeDecimal {

    private DecodeNullableInt32 exponentDecoder = new DecodeNullableInt32();

    private Integer exponent;

    private boolean nullValue;

    public void decode(ByteBuf buf) {
        exponentDecoder.decode(buf);
        if (exponentDecoder.isReady()) {
            exponent = exponentDecoder.getValue();
            exponentReady = true;
            exponentOverflow = exponentDecoder.isOverflow();
            if (exponent != null && buf.isReadable()) {
                mantissaDecoder.decode(buf);
                startedMantissa = true;
                if (mantissaDecoder.isReady()) {
                    mantissa = mantissaDecoder.getValue();
                    mantissaOverflow = mantissaDecoder.isOverflow();
                    ready = true;
                }
            } else if (exponent == null) {
                nullValue = true;
                ready = true;
            }
        }
    }

    public void continueDecode(ByteBuf buf) {
        if (exponentReady && startedMantissa) {
            mantissaDecoder.continueDecode(buf);
            if (mantissaDecoder.isReady()) {
                mantissa = mantissaDecoder.getValue();
                ready = true;
                mantissaOverflow = mantissaDecoder.isOverflow();
            }
        } else if (exponentReady) {
            startedMantissa = true;
            mantissaDecoder.decode(buf);
            if (mantissaDecoder.isReady()) {
                mantissa = mantissaDecoder.getValue();
                ready = true;
                mantissaOverflow = mantissaDecoder.isOverflow();
            }
        } else {
            exponentDecoder.continueDecode(buf);
            if (exponentDecoder.isReady()) {
                exponent = exponentDecoder.getValue();
                exponentReady = true;
                exponentOverflow = exponentDecoder.isOverflow();
                if (exponent != null && buf.isReadable()) {
                    mantissaDecoder.decode(buf);
                    startedMantissa = true;
                    if (mantissaDecoder.isReady()) {
                        mantissa = mantissaDecoder.getValue();
                        mantissaOverflow = mantissaDecoder.isOverflow();
                        ready = true;
                    }
                } else if (exponent == null) {
                    nullValue = true;
                    ready = true;
                }
            }
        }
    }

    public BigDecimal getValue() throws OverflowException {
        if (nullValue) {
            return null;
        } else if (exponent >= -63 && exponent <= 63) {
            return new BigDecimal(mantissa).movePointRight(exponent);
        } else if (exponentOverflow) {
            throw new OverflowException("exponent value range is int32");
        } else if (mantissaOverflow) {
            throw new OverflowException("mantissa value range is int64");
        } else {
            throw new OverflowException("exponent value allowed range is -63 ... 63");
        }
    }
}
