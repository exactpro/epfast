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

package com.exactpro.epfast.decoder.message.commands;

import com.exactpro.epfast.decoder.IMessage;
import com.exactpro.epfast.decoder.message.DecoderState;
import com.exactpro.epfast.decoder.message.DecoderCommand;
import com.exactpro.epfast.template.Reference;

public class InitIndexedProperty implements DecoderCommand {

    private final Reference propertyId;

    public InitIndexedProperty(Reference propertyId) {
        this.propertyId = propertyId;
    }

    @Override
    public void executeOn(DecoderState decoderState) {
        if (decoderState.loopLimit > Integer.MAX_VALUE) {
            throw new RuntimeException("Sequence length " + decoderState.loopIndex
                + " exceeded maximum allowed length (" + Integer.MAX_VALUE + ")");
        }
        IMessage[] array = (decoderState.loopLimit < 0) ? null : new IMessage[(int) decoderState.loopLimit];
        decoderState.activeMessage.setField(propertyId, array);
        decoderState.loopIndex = 0;
        decoderState.nextCommandIndex++;
    }
}
