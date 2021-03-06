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

package com.exactpro.epfast.decoder.message.commands.integer;

import com.exactpro.epfast.decoder.message.DecoderState;
import com.exactpro.epfast.decoder.message.DecoderCommand;
import com.exactpro.epfast.template.Reference;

public class SetNullableInt32 implements DecoderCommand {

    private Reference propertyId;

    public SetNullableInt32(Reference propertyId) {
        this.propertyId = propertyId;
    }

    @Override
    public int executeOn(DecoderState decoderState) {
        decoderState.activeMessage.setField(propertyId, decoderState.register.optionalInt32Value);
        return 1;
    }
}
