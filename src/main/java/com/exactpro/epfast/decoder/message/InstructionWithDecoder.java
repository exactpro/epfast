/*
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
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

package com.exactpro.epfast.decoder.message;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;

import java.util.Objects;

public abstract class InstructionWithDecoder<T extends IDecodeContext> implements DecoderCommand {

    protected T fieldDecoder;

    protected boolean decoderStarted;

    protected InstructionWithDecoder(T fieldDecoder) {
        this.fieldDecoder = Objects.requireNonNull(fieldDecoder);
    }

    protected abstract void decode(DecoderState decoderState) throws OverflowException;

    protected abstract void continueDecode(DecoderState decoderState) throws OverflowException;

    public boolean isReady() {
        return fieldDecoder.isReady();
    }

    @Override
    public int executeOn(DecoderState decoderState) throws OverflowException {
        if (!decoderState.inputBuffer.isReadable()) {
            decoderState.canProceed = false;
            return 0;
        }
        if (!decoderStarted) {
            decode(decoderState);
        } else {
            continueDecode(decoderState);
        }
        if (isReady()) {
            return 1;
        } else {
            decoderState.canProceed = false;
            return 0;
        }
    }
}
