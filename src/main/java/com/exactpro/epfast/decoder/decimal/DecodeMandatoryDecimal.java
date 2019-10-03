package com.exactpro.epfast.decoder.decimal;

import com.exactpro.epfast.decoder.integer.DecodeMandatoryInt32;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public class DecodeMandatoryDecimal extends DecodeDecimal {

    private DecodeMandatoryInt32 exponentDecoder = new DecodeMandatoryInt32();

    private int exponent;

    public void decode(ByteBuf buf) {
        exponentDecoder.decode(buf);
        if (exponentDecoder.isReady()) {
            exponentReady = true;
            exponent = exponentDecoder.getValue();
            mantissaDecoder.decode(buf);
            if (mantissaDecoder.isReady()) {
                mantissa = mantissaDecoder.getValue();
                ready = true;
            }
        }
    }

    public void continueDecode(ByteBuf buf) {
        if (exponentReady) {
            mantissaDecoder.continueDecode(buf);
            if (mantissaDecoder.isReady()) {
                ready = true;
                mantissa = mantissaDecoder.getValue();
            }
        } else {
            exponentDecoder.continueDecode(buf);
            if (exponentDecoder.isReady()) {
                exponentReady = true;
                exponent = exponentDecoder.getValue();
                mantissaDecoder.decode(buf);
                if (mantissaDecoder.isReady()) {
                    mantissa = mantissaDecoder.getValue();
                    ready = true;
                }
            }
        }
    }

    BigDecimal getValue() {
        if (exponent >= -63 && exponent <= 63) {
            return new BigDecimal(mantissa).movePointRight(exponent);
        } else {
            return null;
        }
    }
}
