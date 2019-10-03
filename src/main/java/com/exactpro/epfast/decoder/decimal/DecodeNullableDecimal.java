package com.exactpro.epfast.decoder.decimal;

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
            exponentReady = true;
            exponent = exponentDecoder.getValue();
            if (exponent != null && buf.isReadable()) {
                startedMantissa = true;
                mantissaDecoder.decode(buf);
                if (mantissaDecoder.isReady()) {
                    mantissa = mantissaDecoder.getValue();
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
                ready = true;
                mantissa = mantissaDecoder.getValue();
            }
        }
        else if(exponentReady){
            startedMantissa = true;
            mantissaDecoder.decode(buf);
            if (mantissaDecoder.isReady()) {
                ready = true;
                mantissa = mantissaDecoder.getValue();
            }
        }
        else {
            exponentDecoder.continueDecode(buf);
            if (exponentDecoder.isReady()) {
                exponentReady = true;
                exponent = exponentDecoder.getValue();
                if (exponent != null) {
                    mantissaDecoder.decode(buf);
                    if (mantissaDecoder.isReady()) {
                        mantissa = mantissaDecoder.getValue();
                        ready = true;
                    }
                } else {
                    nullValue = true;
                    ready = true;
                }
            }
        }
    }

    BigDecimal getValue() {
        if (nullValue) {
            return null;
        } else if(exponent >= -63 && exponent <= 63){
            return new BigDecimal(mantissa).movePointRight(exponent);
        }else {
            return  null;
        }
    }
}
