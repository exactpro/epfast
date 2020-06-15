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

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.message.commands.ReadyMessage;
import com.exactpro.epfast.decoder.message.commands.CallByReference;
import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.Template;
import io.netty.buffer.ByteBuf;

import java.util.*;

public class FastDecoder {

    private DecoderState decoderState;

    public FastDecoder(Collection<? extends Template> templates, Reference templateRef) {
        List<DecoderCommand> bootstrapCommands = Arrays.asList(
            new CallByReference(templateRef),
            // TODO change templateRef with appropriate type reference
            new ReadyMessage());
        this.decoderState = new DecoderState(FastCompiler.compile(templates), bootstrapCommands);
    }

    public Collection<?> process(ByteBuf buffer) throws OverflowException {
        decoderState.inputBuffer = Objects.requireNonNull(buffer, "buffer cannot be null");
        decoderState.canProceed = true;
        do {
            decoderState.nextCommand().executeOn(decoderState);
        } while (decoderState.canProceed);
        return decoderState.readMessages();
    }

}
