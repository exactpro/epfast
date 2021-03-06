/*
 * Copyright 2019-2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exactpro.epfast.decoder.decimal;

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.integer.DecodeMandatoryInt32;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public final class DecodeMandatoryDecimal extends DecodeDecimal {

    private DecodeMandatoryInt32 exponentDecoder = new DecodeMandatoryInt32();

    private int exponent;

    public void decode(ByteBuf buf) {
        reset();
        exponentDecoder.decode(buf);
        if (exponentDecoder.isReady()) {
            exponentReady = true;
            try {
                exponent = exponentDecoder.getValue();
            } catch (OverflowException ex) {
                exponentOverflow = true;
            }
            if (buf.isReadable()) {
                mantissaDecoder.decode(buf);
                startedMantissa = true;
                if (mantissaDecoder.isReady()) {
                    ready = true;
                    try {
                        mantissa = mantissaDecoder.getValue();
                    } catch (OverflowException ex) {
                        mantissaOverflow = true;
                    }
                }
            }
        }
    }

    public void continueDecode(ByteBuf buf) {
        if (exponentReady && startedMantissa) {
            mantissaDecoder.continueDecode(buf);
            if (mantissaDecoder.isReady()) {
                ready = true;
                try {
                    mantissa = mantissaDecoder.getValue();
                } catch (OverflowException ex) {
                    mantissaOverflow = true;
                }
            }
        } else if (exponentReady) {
            mantissaDecoder.decode(buf);
            startedMantissa = true;
            if (mantissaDecoder.isReady()) {
                ready = true;
                try {
                    mantissa = mantissaDecoder.getValue();
                } catch (OverflowException ex) {
                    mantissaOverflow = true;
                }
            }
        } else {
            exponentDecoder.continueDecode(buf);
            if (exponentDecoder.isReady()) {
                exponentReady = true;
                try {
                    exponent = exponentDecoder.getValue();
                } catch (OverflowException ex) {
                    exponentOverflow = true;
                }
                if (buf.isReadable()) {
                    mantissaDecoder.decode(buf);
                    startedMantissa = true;
                    if (mantissaDecoder.isReady()) {
                        ready = true;
                        try {
                            mantissa = mantissaDecoder.getValue();
                        } catch (OverflowException ex) {
                            mantissaOverflow = true;
                        }
                    }
                }
            }
        }
    }

    public BigDecimal getValue() throws OverflowException {
        if (exponentOverflow) {
            throw new OverflowException("exponent value range is int32");
        } else if (mantissaOverflow) {
            throw new OverflowException("mantissa value range is int64");
        } else if (exponent >= -63 && exponent <= 63) {
            return new BigDecimal(mantissa).movePointRight(exponent);
        } else {
            throw new OverflowException("exponent value allowed range is -63 ... 63");
        }
    }

    @Override
    public boolean isOverlong() {
        return exponentDecoder.isOverlong() || mantissaDecoder.isOverlong();
    }
}
