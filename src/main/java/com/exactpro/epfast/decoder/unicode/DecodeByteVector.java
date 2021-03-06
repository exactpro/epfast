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

package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public abstract class DecodeByteVector implements IDecodeContext {

    List<Byte> value;

    int counter;

    boolean lengthReady;

    boolean ready;

    boolean overflow;

    public abstract void decode(ByteBuf buf);

    public abstract void continueDecode(ByteBuf buf);

    public abstract byte[] getValue() throws OverflowException;

    public boolean isReady() {
        return ready;
    }

    public boolean isOverflow() {
        return overflow;
    }

    @Override
    public boolean isOverlong() {
        return false;
    }

    public final void reset() {
        lengthReady = false;
        ready = false;
        overflow = false;
        counter = 0;
        value = new ArrayList<>();
    }
}


