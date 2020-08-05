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
import com.exactpro.epfast.decoder.integer.DecodeMandatoryInt64;
import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

public abstract class DecodeDecimal implements IDecodeContext {

    DecodeMandatoryInt64 mantissaDecoder = new DecodeMandatoryInt64();

    long mantissa;

    boolean exponentReady;

    boolean startedMantissa;

    boolean ready;

    boolean exponentOverflow;

    boolean mantissaOverflow;

    boolean exponentOverlong;

    boolean mantissaOverlong;

    protected boolean inProgress;

    public abstract int startDecode(ByteBuf buf, UnionRegister register);

    public abstract int continueDecode(ByteBuf buf, UnionRegister register);

    public final void reset() {
        exponentReady = false;
        startedMantissa = false;
        ready = false;
        exponentOverflow = false;
        mantissaOverflow = false;
    }

    public abstract void setRegisterValue(UnionRegister register);

    @Override
    public boolean inProgress() {
        return inProgress;
    }
}
