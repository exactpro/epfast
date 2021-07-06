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

import com.exactpro.epfast.decoder.integer.DecodeMandatoryInt32;
import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public final class DecodeMandatoryDecimal extends DecodeDecimal {

    private DecodeMandatoryInt32 exponentDecoder = new DecodeMandatoryInt32();

    private int exponent;

    @Override
    public int decode(ByteBuf buf, UnionRegister register) {
        if (!exponentReady) {
            if (exponentDecoder.decode(buf, register) == FINISHED) {
                exponentReady = true;
                if (register.isOverlong) {
                    exponentOverlong = true;
                }
                if (register.isOverflow) {
                    exponentOverflow = true;
                } else {
                    exponent = register.int32Value;
                }
                if (buf.isReadable()) {
                    if (mantissaDecoder.decode(buf, register) == FINISHED) {
                        ready = true;
                        if (register.isOverlong) {
                            mantissaOverlong = true;
                        }
                        if (register.isOverflow) {
                            mantissaOverflow = true;
                        } else {
                            mantissa = register.int64Value;
                        }
                        setResult(register);
                        return FINISHED;
                    }
                }
            }
        } else if (mantissaDecoder.decode(buf, register) == FINISHED) {
            ready = true;
            if (register.isOverlong) {
                mantissaOverlong = true;
            }
            if (register.isOverflow) {
                mantissaOverflow = true;
            } else {
                mantissa = register.int64Value;
            }
            setResult(register);
            return FINISHED;
        }
        return MORE_DATA_NEEDED;
    }

    public void setResult(UnionRegister register) {
        register.isOverlong = exponentOverlong || mantissaOverlong;
        register.isOverflow = exponentOverflow || mantissaOverflow;
        if (exponentOverflow) {
            register.infoMessage = "exponent value range is int32";
        } else if (mantissaOverflow) {
            register.infoMessage = "mantissa value range is int64";
        } else if (exponent >= -63 && exponent <= 63) {
            register.decimalValue = new BigDecimal(mantissa).movePointRight(exponent);
        } else {
            register.isOverflow = true;
            register.infoMessage = "exponent value allowed range is -63 ... 63";
        }
        reset();
    }
}
