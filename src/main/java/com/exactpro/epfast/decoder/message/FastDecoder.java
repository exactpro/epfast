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
import com.exactpro.epfast.decoder.message.instructions.ReadyMessage;
import com.exactpro.epfast.decoder.message.instructions.CallWithReference;
import com.exactpro.epfast.decoder.message.instructions.SetApplicationType;
import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.Template;
import io.netty.buffer.ByteBuf;

import java.util.*;

public class FastDecoder {

    private ExecutionContext executionContext;

    public FastDecoder(Collection<? extends Template> templates, Reference templateRef) {
        //TODO send bootstrapCode
        this.executionContext = new ExecutionContext(new Compiler().compile(templates), null);
        //TODO change templateRef with appropriate type reference
        executionContext.instructions.add(new SetApplicationType(templateRef));
        executionContext.instructions.add(new CallWithReference(templateRef));
        executionContext.instructions.add(new ReadyMessage());
    }

    public Collection<? extends Object> handle(ByteBuf buffer) throws OverflowException {
        executionContext.buffer = buffer;
        while (executionContext.nextStep()) {
        }
        return executionContext.fetchResults();
    }
}
