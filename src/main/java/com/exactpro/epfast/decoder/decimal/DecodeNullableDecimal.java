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

import com.exactpro.epfast.decoder.integer.DecodeNullableInt32;
import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public final class DecodeNullableDecimal extends DecodeDecimal {

    private DecodeNullableInt32 exponentDecoder = new DecodeNullableInt32();

    private int exponent;

    private boolean nullValue;

    public int decode(ByteBuf buf, UnionRegister register) {
        reset();
        inProgress = true;
        exponentDecoder.decode(buf, register);
        if (exponentDecoder.isReady()) {
            exponentReady = true;
            if (register.isOverflow) {
                exponentOverflow = true;
            } else {
                exponent = register.int32Value;
            }
            if (!register.isNull && buf.isReadable()) {
                mantissaDecoder.decode(buf, register);
                startedMantissa = true;
                if (mantissaDecoder.isReady()) {
                    ready = true;
                    if (register.isOverflow) {
                        mantissaOverflow = true;
                    } else {
                        mantissa = register.int64Value;
                    }
                }
            } else if (register.isNull) {
                nullValue = true;
                ready = true;
                setRegisterValue(register);
                return 1;
            }
        }
        if (ready) {
            setRegisterValue(register);
            return 1;
        } else {
            return 0;
        }
    }

    public int continueDecode(ByteBuf buf, UnionRegister register) {
        if (exponentReady && startedMantissa) {
            mantissaDecoder.continueDecode(buf, register);
            if (mantissaDecoder.isReady()) {
                ready = true;
                if (register.isOverflow) {
                    mantissaOverflow = true;
                } else {
                    mantissa = register.int64Value;
                }
            }
        } else if (exponentReady) {
            startedMantissa = true;
            mantissaDecoder.decode(buf, register);
            if (mantissaDecoder.isReady()) {
                ready = true;
                if (register.isOverflow) {
                    mantissaOverflow = true;
                } else {
                    mantissa = register.int64Value;
                }
            }
        } else {
            exponentDecoder.continueDecode(buf, register);
            if (exponentDecoder.isReady()) {
                exponentReady = true;
                if (register.isOverflow) {
                    exponentOverflow = true;
                } else {
                    exponent = register.int32Value;
                }
                if (!register.isNull && buf.isReadable()) {
                    mantissaDecoder.decode(buf, register);
                    startedMantissa = true;
                    if (mantissaDecoder.isReady()) {
                        ready = true;
                        if (register.isOverflow) {
                            mantissaOverflow = true;
                        } else {
                            mantissa = register.int64Value;
                        }
                    }
                } else if (register.isNull) {
                    nullValue = true;
                    ready = true;
                    setRegisterValue(register);
                    return 1;
                }
            }
        }
        if (ready) {
            setRegisterValue(register);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void setRegisterValue(UnionRegister register) {
        inProgress = false;
        if (exponentOverflow) {
            register.isOverflow = true;
            register.errorMessage = "exponent value range is int32";
        } else if (mantissaOverflow) {
            register.isOverflow = true;
            register.errorMessage = "mantissa value range is int64";
        } else if (nullValue) {
            register.isOverflow = false;
            register.decimalValue = null;
        } else if (exponent >= -63 && exponent <= 63) {
            register.isOverflow = false;
            register.isNull = false;
            register.decimalValue = new BigDecimal(mantissa).movePointRight(exponent);
        } else {
            register.isOverflow = true;
            register.errorMessage = "exponent value allowed range is -63 ... 63";
        }
    }

    @Override
    public boolean isOverlong() {
        return exponentDecoder.isOverlong() || mantissaDecoder.isOverlong();
    }
}
