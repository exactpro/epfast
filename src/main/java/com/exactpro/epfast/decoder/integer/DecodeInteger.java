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

package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

public abstract class DecodeInteger implements IDecodeContext {

    static final int SIGN_BIT_MASK = 0b01000000;

    protected boolean ready;

    protected boolean overflow;

    boolean overlong;

    boolean checkForSignExtension = false;

    public abstract int decode(ByteBuf buf, UnionRegister register);

    public abstract int continueDecode(ByteBuf buf, UnionRegister register);

    public boolean isReady() {
        return ready;
    }

    public boolean isOverlong() {
        return overlong;
    }

    protected boolean inProgress;

    protected final void reset() {
        ready = false;
        overflow = false;
        overlong = false;
        checkForSignExtension = false;
    }

    static void longToBytes(long value, byte[] bytes) {
        for (int i = 7; i >= 0; --i) {
            bytes[i] = (byte) value;
            value >>>= 8;
        }
    }

    public abstract void setRegisterValue(UnionRegister register);

    @Override
    public boolean inProgress() {
        return inProgress;
    }
}
