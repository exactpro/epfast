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

package com.exactpro.epfast.decoder;

import com.exactpro.epfast.decoder.message.DecoderCommand;
import com.exactpro.epfast.decoder.message.DecoderState;
import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

public abstract class StreamDecoderCommand implements DecoderCommand {

    public static final int FINISHED = 1;

    public static final int MORE_DATA_NEEDED = 0;

    protected static final int CLEAR_STOP_BIT_MASK = 0b01111111;

    protected abstract int decode(ByteBuf buf, UnionRegister register);

    @Override
    public int executeOn(DecoderState decoderState) {
        if (!decoderState.inputBuffer.isReadable()) {
            decoderState.canProceed = false;
            return MORE_DATA_NEEDED;
        }
        return decode(decoderState.inputBuffer, decoderState.register);
    }
}
