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

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.integer.DecodeMandatoryInt64;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public abstract class DecodeDecimal implements IDecodeContext {

    DecodeMandatoryInt64 mantissaDecoder = new DecodeMandatoryInt64();

    long mantissa;

    boolean exponentReady;

    boolean startedMantissa;

    boolean ready;

    boolean exponentOverflow;

    boolean mantissaOverflow;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public final void reset() {
        exponentReady = false;
        startedMantissa = false;
        ready = false;
        exponentOverflow = false;
        mantissaOverflow = false;
    }

    public abstract BigDecimal getValue() throws OverflowException;

    public boolean isReady() {
        return ready;
    }

    public abstract boolean isOverlong();
}
