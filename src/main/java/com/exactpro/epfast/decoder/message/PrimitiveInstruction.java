/******************************************************************************
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
 ******************************************************************************/

package com.exactpro.epfast.decoder.message;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.IMessage;
import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.template.Reference;

public abstract class PrimitiveInstruction<T extends IDecodeContext> extends InstructionWithDecoder<T> {

    protected PrimitiveInstruction(Reference fieldName, T fieldDecoder) {
        super(fieldName, fieldDecoder);
    }

    protected void decode(ExecutionContext ec) throws OverflowException {
        decoderStarted = true;
        fieldDecoder.decode(ec.buffer);
        if (isReady()) {
            setMessageValue(ec.applicationMessage);
            decoderStarted = false;
        }
    }

    protected void continueDecode(ExecutionContext ec) throws OverflowException {
        fieldDecoder.continueDecode(ec.buffer);
        if (isReady()) {
            setMessageValue(ec.applicationMessage);
            decoderStarted = false;
        }
    }

    public abstract void setMessageValue(IMessage message) throws OverflowException;

    public void setNull(IMessage message) {
        message.setField(fieldName.getName(), null);
    }
}
