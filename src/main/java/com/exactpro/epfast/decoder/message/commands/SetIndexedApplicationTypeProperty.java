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

package com.exactpro.epfast.decoder.message.commands;

import com.exactpro.epfast.decoder.message.DecoderState;
import com.exactpro.epfast.decoder.message.DecoderCommand;
import com.exactpro.epfast.template.Reference;

import java.lang.reflect.Array;

public class SetIndexedApplicationTypeProperty implements DecoderCommand {

    private final Reference propertyId;

    public SetIndexedApplicationTypeProperty(Reference propertyId) {
        this.propertyId = propertyId;
    }

    @Override
    public void executeOn(DecoderState decoderState) {
        Object array = decoderState.activeMessage.getField(propertyId);
        Array.set(array, decoderState.loopIndex, decoderState.register.applicationValue);
        decoderState.nextCommandIndex++;
    }
}
