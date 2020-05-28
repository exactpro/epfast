/******************************************************************************
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
 ******************************************************************************/

package com.exactpro.epfast.decoder.message;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.template.Reference;

public abstract class InstructionWithDecoder<T extends IDecodeContext> implements NormalInstruction {

    protected Reference fieldName;

    protected T fieldDecoder;

    protected boolean decoderStarted;

    protected InstructionWithDecoder(Reference fieldName, T fieldDecoder) {
        assert fieldName != null; // TODO check that assertions are turned on during test;
        assert fieldDecoder != null; // XXX consider using lombok @NonNull to reduce boilerplate code

        this.fieldName = fieldName;
        this.fieldDecoder = fieldDecoder;
    }

    protected abstract void decode(ExecutionContext ec) throws OverflowException;

    protected abstract void continueDecode(ExecutionContext ec) throws OverflowException;

    public boolean isReady() {
        return fieldDecoder.isReady();
    }

    @Override
    public boolean execute(ExecutionContext ec) throws OverflowException {
        if (!ec.buffer.isReadable()) {
            return false;
        }
        if (!decoderStarted) {
            decode(ec);
        } else {
            continueDecode(ec);
        }
        if (isReady()) {
            ec.instructionIndex++;
            return true;
        }
        return false;
    }
}
